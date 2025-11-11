package br.com.projetojsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private String token;
    private String mensagem;

    public String login() {
        try {
            // Endpoint da sua API Spring Boot (ajuste se a porta for diferente)
            URL url = new URL("http://localhost:8080/auth/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Cria o JSON da requisição
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", senha);

            // Envia os dados
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(json.toString());
            }

            // Lê a resposta da API
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Converte resposta para JSON
                JSONObject resposta = new JSONObject(response.toString());
                token = resposta.optString("token", null);

                if (token != null) {
                    addMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Login realizado com sucesso!");
                    return "index.xhtml?faces-redirect=true";
                } else {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Token não retornado pela API!");
                    return null;
                }
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Credenciais inválidas!");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao conectar com o servidor!");
            return null;
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
