
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main{
    public static void main(String[] args){
        List<String> candidatosLinhas = new ArrayList<String>();

        try{
            candidatosLinhas = new LeituraCSV("candidatos.csv").lerArquivo();

        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }
        

        List<Candidato> candidatos = new ArrayList<Candidato>();

        for(String linha : candidatosLinhas){
            String[] dados = linha.split(",");
            Candidato candidato = new Candidato(
                Integer.parseInt(dados[0]),
                Integer.parseInt(dados[1]),
                dados[2],
                dados[3],
                dados[4],
                dados[5],
                LocalDate.parse(dados[6], DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dados[7],
                Integer.parseInt(dados[8])
            );
            candidatos.add(candidato);
        }

        // for(Candidato candidato : candidatos){
        //     System.out.println(candidato.getData_nasc());
        // } 
        

        List<String> partidosLinhas = new ArrayList<String>();

        try{
            partidosLinhas = new LeituraCSV("partidos.csv").lerArquivo();

        }catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }

        List<Partidos> partidos = new ArrayList<Partidos>();

        for(String linha : partidosLinhas){
            String[] dados = linha.split(",");
            Partidos partido = new Partidos(
                Integer.parseInt(dados[0]),
                Integer.parseInt(dados[1]),
                dados[2],
                dados[3]
            );
            partidos.add(partido);
        }
    }
}