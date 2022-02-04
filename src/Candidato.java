
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;


public class Candidato {
    private int numero;
    private int votos_nominais;
    private String situacao;
    private String nome;
    private String nome_urna;
    private String sexo;
    private LocalDate data_nasc;
    private String destino_voto;
    private int num_partido;
    private int idade;
    private int rank;


    public Candidato(int numero,int votos_nominais,String situacao,String nome, String nome_urna,String sexo,LocalDate data_nasc,String destino_voto,int num_partido){
        
        //verifica se o nome_urna termina com ' '
        if(nome_urna.charAt(nome_urna.length()-1)==' '){
            nome_urna = nome_urna.substring(0,nome_urna.length()-1);
        }


        this.numero = numero;
        this.votos_nominais = votos_nominais;
        this.situacao = situacao;
        this.nome = nome;
        this.nome_urna = nome_urna;
        this.sexo = sexo;
        this.data_nasc = data_nasc;
        this.destino_voto = destino_voto;
        this.num_partido = num_partido;
    }

    public int getIdade(LocalDate dataEleicao){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data_nasc = LocalDate.parse(this.data_nasc.format(formatter), formatter);
        Period periodo = Period.between(data_nasc, dataEleicao);

        return periodo.getYears();
    }

    public int getRank(){
        return this.rank;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public int getNumero() {
        return numero;
    }

    public int getVotos_nominais() {
        if(this.destino_voto.equals("Válido")){
            return votos_nominais;
        }else{
            return 0;
        }
        
    }

    public String getSituacao() {
        return situacao;
    }

    public String getNome() {
        return nome;
    }

    public String getNome_urna() {
        return nome_urna;
    }

    public String getSexo() {
        return sexo;
    }

    public LocalDate getData_nasc() {
        return data_nasc;
    }

    public String getDestino_voto() {
        return destino_voto;
    }

    public int getNum_partido() {
        return num_partido;
    }

}


