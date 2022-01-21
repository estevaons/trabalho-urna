public class Partidos {
    private int num_partido;
    private int votos_legenda;
    private String nome;
    private String sigla;

    public Partidos(int num_partido, int votos_legenda, String nome, String sigla) {
        this.num_partido = num_partido;
        this.votos_legenda = votos_legenda;
        this.nome = nome;
        this.sigla = sigla;
    }

    public int getNum_partido() {
        return num_partido;
    }

    public int getVotos_legenda() {
        return votos_legenda;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }
}
