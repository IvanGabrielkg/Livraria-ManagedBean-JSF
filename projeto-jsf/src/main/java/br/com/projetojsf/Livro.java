package br.com.projetojsf;

import java.io.Serializable;
import java.util.Objects;

public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String autor;
    private double preco;
    private String imagem;

    // 🔹 Novos campos
    private String genero;
    private int anoPublicacao;
    private String editora;
    private int paginas;
    private String descricao;

    // 🔸 Construtor padrão (necessário para o JSF)
    public Livro() {}

    // 🔸 Construtor simples (para preencher rápido no init)
    public Livro(String nome, String autor, double preco, String imagem, String descricao, String genero) {
        this.nome = nome;
        this.autor = autor;
        this.preco = preco;
        this.imagem = imagem;
        this.descricao = descricao;
        this.genero = genero;
    }

    // 🔸 Construtor completo (caso queira dados detalhados)
    public Livro(String nome, String autor, double preco, String imagem,
                 String genero, int anoPublicacao, String editora, int paginas, String descricao) {
        this.nome = nome;
        this.autor = autor;
        this.preco = preco;
        this.imagem = imagem;
        this.genero = genero;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.paginas = paginas;
        this.descricao = descricao;
    }

    // ✅ Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public int getPaginas() { return paginas; }
    public void setPaginas(int paginas) { this.paginas = paginas; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    // ✅ equals e hashCode (importante para evitar duplicações em listas)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;
        Livro livro = (Livro) o;
        return Objects.equals(nome, livro.nome) &&
               Objects.equals(autor, livro.autor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, autor);
    }

    // ✅ toString para depuração
    @Override
    public String toString() {
        return "Livro{" +
               "nome='" + nome + '\'' +
               ", autor='" + autor + '\'' +
               ", preco=" + preco +
               ", genero='" + genero + '\'' +
               '}';
    }
}
