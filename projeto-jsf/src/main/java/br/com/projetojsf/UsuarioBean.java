package br.com.projetojsf;

import com.google.gson.Gson;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ManagedBean(name = "usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario = new Usuario();
    private String confirmarSenha;
    private String token;
    private String role = "USER";
    private static final String BASE_URL = "http://localhost:8080/auth";

    // GETTERS / SETTERS	
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getConfirmarSenha() { return confirmarSenha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Propriedade utilizada no template: #{usuarioBean.logado}
    public boolean isLogado() {
        // Preferível checar token (mais confiável que nome)
        return token != null && !token.isEmpty();
    }

    // LOGIN
    public String login() {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_URL + "/login");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}",
                    safe(usuario.getEmail()), safe(usuario.getSenha()));

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = con.getResponseCode();
            if (status == 200) {
                try (InputStreamReader reader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)) {
                    Gson gson = new Gson();
                    TokenResponse resp = gson.fromJson(reader, TokenResponse.class);
                    this.token = resp.getToken();
                    usuario.setToken(this.token);
                    // Preenche nome/email a partir do token (se não vier no response)
                    hydrateUserFromToken();
                }
                // Limpa a senha por segurança
                usuario.setSenha(null);
                return "loja.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falhou", "Verifique seu e-mail e senha"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível realizar login"));
            return null;
        } finally {
            if (con != null) con.disconnect();
        }
    }

    // CADASTRO
    public String cadastrar() {
        if (usuario.getSenha() == null || !usuario.getSenha().equals(confirmarSenha)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "As senhas não coincidem"));
            return null;
        }

        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_URL + "/register");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            String json = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
                    safe(usuario.getNome()), safe(usuario.getEmail()), safe(usuario.getSenha()), safe(role));

            try (OutputStream os = con.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = con.getResponseCode();
            if (status == 200 || status == 201) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cadastro realizado com sucesso!"));
                usuario.setSenha(null);
                confirmarSenha = null;
                return "login.xhtml?faces-redirect=true";
            } else if (status == 400) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "E-mail já cadastrado"));
                return null;
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha (status " + status + ")"));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível realizar cadastro"));
            return null;
        } finally {
            if (con != null) con.disconnect();
        }
    }

    public String logout() {
        token = null;
        usuario = new Usuario();
        confirmarSenha = null;
        return "login.xhtml?faces-redirect=true";
    }

    private String safe(String v) {
        return v == null ? "" : v;
    }

    // Decodifica JWT para extrair 'sub' (email) e opcionalmente 'name'
    private void hydrateUserFromToken() {
        if (token == null) return;
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return;
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            // Extrai email (sub)
            String email = extractJsonValue(payloadJson, "sub");
            if (email != null && (usuario.getEmail() == null || usuario.getEmail().isEmpty())) {
                usuario.setEmail(email);
            }

            // Se você incluir "name" como claim no backend
            String name = extractJsonValue(payloadJson, "name");
            if (name != null && (usuario.getNome() == null || usuario.getNome().isEmpty())) {
                usuario.setNome(name);
            }

            // fallback: se não houver nome, usa o email para exibir algo
            if ((usuario.getNome() == null || usuario.getNome().isEmpty()) && usuario.getEmail() != null) {
                usuario.setNome(usuario.getEmail());
            }

        } catch (Exception e) {
            // falha silenciosa para não quebrar o login
            e.printStackTrace();
        }
    }

    // Método simples (não robusto) para pegar valor de uma chave do JSON sem libs
    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int start = idx + pattern.length();
        int end = json.indexOf('"', start);
        if (end > start) return json.substring(start, end);
        return null;
    }

    // Classe para desserializar resposta do login
    public static class TokenResponse {
        private String token;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}