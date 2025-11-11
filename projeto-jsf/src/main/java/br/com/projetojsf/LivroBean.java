package br.com.projetojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LivroBean implements Serializable {

    private List<Livro> livros;
    private Livro livroSelecionado;

    @PostConstruct
    public void init() {
        livros = new ArrayList<>();

        // 🧠 Ficção
        livros.add(new Livro(
            "1984",
            "George Orwell",
            45.9,
            "https://m.media-amazon.com/images/I/61t0bwt1s3L._AC_UF1000,1000_QL80_.jpg",
            "Um dos romances mais influentes do século XX, descreve uma sociedade totalitária onde o Estado controla até os pensamentos dos cidadãos. Winston Smith luta contra o sistema e busca a verdade em meio à manipulação e vigilância constante.",
            "Ficção"
        ));

        livros.add(new Livro(
            "A Revolução dos Bichos",
            "George Orwell",
            39.9,
            "https://m.media-amazon.com/images/I/612QiXA+FyL._AC_UF1000,1000_QL80_.jpg",
            "Uma sátira poderosa sobre poder e corrupção. Animais de uma fazenda se revoltam contra seus donos humanos, mas descobrem que a tirania pode assumir novas formas.",
            "Ficção"
        ));

        livros.add(new Livro(
            "Admirável Mundo Novo",
            "Aldous Huxley",
            49.9,
            "https://m.media-amazon.com/images/I/61hOp6UFvCL.jpg",
            "Um futuro onde a felicidade é obrigatória e a liberdade é uma ameaça. Huxley explora o controle social através da tecnologia e da engenharia genética.",
            "Ficção"
        ));

        // 🧙 Fantasia
        livros.add(new Livro(
            "O Senhor dos Anéis: A Sociedade do Anel",
            "J. R. R. Tolkien",
            79.9,
            "https://m.media-amazon.com/images/I/81SWBRKfExL._AC_UF1000,1000_QL80_.jpg",
            "O início da jornada épica pela Terra Média. Um grupo improvável deve destruir um anel poderoso antes que ele caia nas mãos do Senhor do Escuro, Sauron.",
            "Fantasia"
        ));

        livros.add(new Livro(
            "Harry Potter e a Pedra Filosofal",
            "J. K. Rowling",
            59.9,
            "https://m.media-amazon.com/images/I/81ibfYk4qmL._AC_UF1000,1000_QL80_.jpg",
            "O primeiro livro da saga de Harry Potter. Um garoto descobre ser um bruxo e entra em um mundo mágico cheio de perigos, amizades e mistérios.",
            "Fantasia"
        ));

        livros.add(new Livro(
            "O Nome do Vento",
            "Patrick Rothfuss",
            69.9,
            "https://m.media-amazon.com/images/I/51XB3PsNieL._UF1000,1000_QL80_.jpg",
            "A história de Kvothe, um jovem talentoso que se torna uma lenda viva. Uma narrativa rica e envolvente sobre magia, música e conhecimento.",
            "Fantasia"
        ));

        // 💞 Romance
        livros.add(new Livro(
            "Orgulho e Preconceito",
            "Jane Austen",
            42.9,
            "https://m.media-amazon.com/images/I/719esIW3D7L._AC_UF1000,1000_QL80_.jpg",
            "Um clássico sobre amor e convenções sociais. Elizabeth Bennet e Mr. Darcy enfrentam mal-entendidos e preconceitos em uma sociedade rígida do século XIX.",
            "Romance"
        ));

        livros.add(new Livro(
            "Como Eu Era Antes de Você",
            "Jojo Moyes",
            44.9,
            "https://m.media-amazon.com/images/I/81-P6oEm8cL.jpg",
            "Louisa Clark aceita cuidar de Will Traynor, um homem tetraplégico e amargurado. O que começa como um trabalho se transforma em uma lição sobre amor e liberdade.",
            "Romance"
        ));

        livros.add(new Livro(
            "A Culpa é das Estrelas",
            "John Green",
            39.9,
            "https://m.media-amazon.com/images/I/811ivBP1rsL._UF1000,1000_QL80_.jpg",
            "Dois adolescentes com câncer se conhecem em um grupo de apoio e vivem uma emocionante história de amor e superação.",
            "Romance"
        ));

        // 👻 Terror
        livros.add(new Livro(
            "O Iluminado",
            "Stephen King",
            59.9,
            "https://m.media-amazon.com/images/I/8147kKLLvOL._AC_UF1000,1000_QL80_.jpg",
            "Jack Torrance aceita um emprego como zelador de inverno no isolado Hotel Overlook. Mas forças sombrias transformam o local em um pesadelo psicológico e sobrenatural.",
            "Terror"
        ));

        livros.add(new Livro(
            "Drácula",
            "Bram Stoker",
            64.9,
            "https://m.media-amazon.com/images/I/61MgodE1s0L._AC_UF1000,1000_QL80_.jpg",
            "O clássico gótico que definiu o mito moderno do vampiro. O conde Drácula viaja da Transilvânia a Londres, espalhando medo e sedução.",
            "Terror"
        ));

        livros.add(new Livro(
            "Frankenstein",
            "Mary Shelley",
            49.9,
            "https://m.media-amazon.com/images/I/91KEmBm2GVL.jpg",
            "Victor Frankenstein cria vida a partir da morte, mas se torna prisioneiro de sua própria criação. Uma reflexão sobre ciência, ambição e humanidade.",
            "Terror"
        ));

        if (!livros.isEmpty()) {
            livroSelecionado = livros.get(0);
        }
    }


    // Getters e setters
    public List<Livro> getLivros() { return livros; }
    public Livro getLivroSelecionado() { return livroSelecionado; }
    public void setLivroSelecionado(Livro livroSelecionado) { this.livroSelecionado = livroSelecionado; }

    // Finalizar compra com popup
    public void finalizarCompra() {
        if (livroSelecionado == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                "Nenhum livro selecionado", "Por favor, selecione um livro antes de finalizar a compra.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        // Simula o salvamento no "banco"
        System.out.println("Compra salva no sistema: " + livroSelecionado.getNome());

        // Mostra popup de sucesso
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
            "Compra concluída!", 
            "Você comprou o livro \"" + livroSelecionado.getNome() + "\" com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        // (Opcional) "limpar" a seleção após compra
        livroSelecionado = null;
    }
}
