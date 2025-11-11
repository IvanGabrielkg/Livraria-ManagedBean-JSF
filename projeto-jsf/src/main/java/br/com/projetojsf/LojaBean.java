package br.com.projetojsf;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ManagedBean
@SessionScoped
public class LojaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------------------------
    // Categorias da loja
    // ---------------------------
    private List<Categoria> categorias = Arrays.asList(
    		new Categoria("Ação", "fa-solid fa-gun"),
    	    new Categoria("Ficção", "fa-solid fa-atom"),          // Livros com crítica social e política
    	    new Categoria("Fantasia", "pi-star"),        // Aventuras mágicas e mundos imaginários
    	    new Categoria("Romance", "pi-heart"),        // Histórias de amor e relacionamentos
    	    new Categoria("Terror", "fa-solid fa-book-skull")           // Suspense e horror psicológico
    	);


    // ---------------------------
    // Lista de livros
    // ---------------------------
    private List<Livro> livros = new ArrayList<>();
    private Livro livroSelecionado;
    private List<Livro> carrinho = new ArrayList<>();
    private List<Livro> wishlist = new ArrayList<>();
    private String termoPesquisa = "";
    private List<Livro> resultadosPesquisa = new ArrayList<>();

    // ---------------------------
    // Inicializa os livros manualmente
    // ---------------------------
    @PostConstruct
    public void init() {
        livros = new ArrayList<>();

        // Ficção
        livros.add(new Livro("1984", "George Orwell", 45.90, "https://m.media-amazon.com/images/I/61t0bwt1s3L._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("A Revolução dos Bichos", "George Orwell", 39.90, "https://m.media-amazon.com/images/I/612QiXA+FyL._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("Admirável Mundo Novo", "Aldous Huxley", 49.90, "https://m.media-amazon.com/images/I/61hOp6UFvCL.jpg"));

        // Fantasia
        livros.add(new Livro("O Senhor dos Anéis: A Sociedade do Anel", "J. R. R. Tolkien", 79.90, "https://m.media-amazon.com/images/I/81SWBRKfExL._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("Harry Potter e a Pedra Filosofal", "J. K. Rowling", 59.90, "https://m.media-amazon.com/images/I/81ibfYk4qmL._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("O Nome do Vento", "Patrick Rothfuss", 69.90, "https://m.media-amazon.com/images/I/51XB3PsNieL._UF1000,1000_QL80_.jpg"));

        // Romance
        livros.add(new Livro("Orgulho e Preconceito", "Jane Austen", 42.90, "https://m.media-amazon.com/images/I/719esIW3D7L._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("Como Eu Era Antes de Você", "Jojo Moyes", 44.90, "https://m.media-amazon.com/images/I/81-P6oEm8cL.jpg"));
        livros.add(new Livro("A Culpa é das Estrelas", "John Green", 39.90, "https://m.media-amazon.com/images/I/811ivBP1rsL._UF1000,1000_QL80_.jpg"));

        // Terror
        livros.add(new Livro("O Iluminado", "Stephen King", 59.90, "https://m.media-amazon.com/images/I/8147kKLLvOL._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("Drácula", "Bram Stoker", 64.90, "https://m.media-amazon.com/images/I/61MgodE1s0L._AC_UF1000,1000_QL80_.jpg"));
        livros.add(new Livro("Frankenstein", "Mary Shelley", 49.90, "https://m.media-amazon.com/images/I/710p9SUfZtL._AC_UF1000,1000_QL80_.jpg"));

        // Seleciona o primeiro livro como padrão
        if (!livros.isEmpty()) {
            livroSelecionado = livros.get(0);
        }
    }


    // ---------------------------
    // Métodos de ação (carrinho, wishlist, pesquisa, navegação)
    // ---------------------------
    public void adicionarAoCarrinho(Livro livro) {
        if (!carrinho.contains(livro)) {
            carrinho.add(livro);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Adicionado ao carrinho!", livro.getNome()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Esse livro já está no carrinho!", livro.getNome()));
        }
    }

    public void removerDoCarrinho(Livro livro) {
        carrinho.remove(livro);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Removido do carrinho!", livro.getNome()));
    }

    public double getTotalCarrinho() {
        return carrinho.stream().mapToDouble(Livro::getPreco).sum();
    }

    public String formatarPreco(double preco) {
        return String.format("R$ %.2f", preco);
    }

    public void adicionarWishlist(Livro livro) {
        if (!wishlist.contains(livro)) {
            wishlist.add(livro);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Adicionado à sua lista de desejos!", livro.getNome()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Esse livro já está na sua wishlist!", livro.getNome()));
        }
    }

    public void removerWishlist(Livro livro) {
        wishlist.remove(livro);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Removido da lista de desejos!", livro.getNome()));
    }

    public void moverParaCarrinho(Livro livro) {
        if (wishlist.contains(livro)) {
            wishlist.remove(livro);
            adicionarAoCarrinho(livro);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Movido para o carrinho!", livro.getNome()));
        }
    }

    public void moverParaWishlist(Livro livro) {
        if (livro == null) return;
        if (!wishlist.contains(livro)) {
            wishlist.add(livro);
            carrinho.remove(livro);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Movido para a wishlist!", livro.getNome()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Já está na wishlist!", livro.getNome()));
        }
    }

    private boolean pesquisou = false;

    public void pesquisar() {
        pesquisou = true;
        if (termoPesquisa == null || termoPesquisa.trim().isEmpty()) {
            resultadosPesquisa = new ArrayList<>();
        } else {
            String termo = termoPesquisa.toLowerCase();
            resultadosPesquisa = livros.stream()
                .filter(l -> l.getNome().toLowerCase().contains(termo)
                          || l.getAutor().toLowerCase().contains(termo))
                .toList();
        }
    }
    public boolean isPesquisou() { return pesquisou; }


    public String verDetalhes(Livro livro) {
        this.livroSelecionado = livro;
        return "livroDetalhe.xhtml?faces-redirect=true";
    }

    public String verCarrinho() {
        return "carrinho.xhtml?faces-redirect=true";
    }

    public String verWishlist() {
        return "wishlist.xhtml?faces-redirect=true";
    }

    // ---------------------------
    // Getters e Setters
    // ---------------------------
    public List<Categoria> getCategorias() { return categorias; }
    public List<Livro> getLivros() { return livros; }
    public Livro getLivroSelecionado() { return livroSelecionado; }
    public void setLivroSelecionado(Livro livroSelecionado) { this.livroSelecionado = livroSelecionado; }
    public List<Livro> getCarrinho() { return carrinho; }
    public void setCarrinho(List<Livro> carrinho) { this.carrinho = carrinho; }
    public List<Livro> getWishlist() { return wishlist; }
    public void setWishlist(List<Livro> wishlist) { this.wishlist = wishlist; }
    public String getTermoPesquisa() { return termoPesquisa; }
    public void setTermoPesquisa(String termoPesquisa) { this.termoPesquisa = termoPesquisa; }
    public List<Livro> getResultadosPesquisa() { return resultadosPesquisa; }
    public void setResultadosPesquisa(List<Livro> resultadosPesquisa) { this.resultadosPesquisa = resultadosPesquisa; }

    // ---------------------------
    // Classes internas
    // ---------------------------
    public class Categoria {
        private String nome;
        private String icone;

        public Categoria(String nome, String icone) { this.nome = nome; this.icone = icone; }
        public Categoria() {}
        public String getNome() { return nome; }
        public String getIcone() { return icone; }
    }

    public class Livro {
        private String nome;
        private String autor;
        private double preco;
        private String imagem;

        public Livro(String nome, String autor, double preco, String imagem) {
            this.nome = nome;
            this.autor = autor;
            this.preco = preco;
            this.imagem = imagem;
        }

        public Livro() {}

        public String getNome() { return nome; }
        public String getAutor() { return autor; }
        public double getPreco() { return preco; }
        public String getImagem() { return imagem; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Livro)) return false;
            Livro livro = (Livro) o;
            return nome.equals(livro.nome) && autor.equals(livro.autor);
        }

        @Override
        public int hashCode() { return nome.hashCode() + autor.hashCode(); }
    }
}
