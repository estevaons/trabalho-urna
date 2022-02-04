package src;

import java.util.List;
import java.util.ArrayList;

public class Partidos {
    private int num_partido;
    private int votos_legenda;
    private String nome;
    private String sigla;
    private int votosNominais_partido;
    private int votosTotais;
    List<Candidato> candidatosDoPartido = new ArrayList<Candidato>();

    public Partidos(int num_partido, int votos_legenda, String nome, String sigla) {
        this.num_partido = num_partido;
        this.votos_legenda = votos_legenda;
        this.nome = nome;
        this.sigla = sigla;
        this.votosNominais_partido = 0;
        this.votosTotais = 0;
    }

    public int getQtdEleitos(){
        int qtdEleitos = 0;
        for(Candidato candidato : candidatosDoPartido){
            if(candidato.getSituacao().equals("Eleito")){
                qtdEleitos++;
            }
        }
        return qtdEleitos;
    }

    public int getVotosLegenda() {
        return votos_legenda;
    }

    public void setVotosTotais(int votosTotais) {
        this.votosTotais = votosTotais;
    }

    public int getVotosTotais() {
        return votosTotais;
    }

    public int getVotosNominais_partido() {
        return votosNominais_partido;
    }
    
    public void setVotosNominais_partido(int votosNominais_partido) {
        this.votosNominais_partido = votosNominais_partido;
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
