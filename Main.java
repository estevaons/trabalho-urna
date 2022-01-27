
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

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
        //criar mapa de partidos
        Map<Integer, Partidos> mapaPartidos = new HashMap<Integer, Partidos>();


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
        
        //para procurar o partido pelo numero
        for(Partidos partido : partidos){
            mapaPartidos.put(partido.getNum_partido(), partido);
        }

        //Criar relatórios

        //calcular numero de eleitos
        int numeroEleitos = 0;
        for(Candidato candidato : candidatos){
            if(candidato.getSituacao().equals("Eleito")){
                numeroEleitos++;
            }
        }
        // 1.imprimir candidatos eleitos
        System.out.print("Número de vagas: "+ numeroEleitos + "\n");

        // 2. criar lista de candidatos eleitos
        List<Candidato> candidatosEleitos = new ArrayList<Candidato>();

        for(Candidato candidato : candidatos){
            if(candidato.getSituacao().equals("Eleito")){        
                candidatosEleitos.add(candidato);
            }
        }
        
        //ordernar candidatos eleitos pelo numero de votos e por idade
        candidatosEleitos.sort((Candidato c1, Candidato c2) -> {
            if(c1.getVotos_nominais() == c2.getVotos_nominais()){
                return c1.getData_nasc().compareTo(c2.getData_nasc());
            };
            return c2.getVotos_nominais() - c1.getVotos_nominais();
        });
        //candidatosEleitos.sort((Candidato c1, Candidato c2) -> c2.getVotos_nominais() - c1.getVotos_nominais());

        //imprimir candidatos eleitos ordenados
        System.out.println("\nVereadores eleitos:");
        for(Candidato candidato : candidatosEleitos){
            System.out.printf("%s / %s (%s, %d votos)\n",candidato.getNome(),candidato.getNome_urna(),mapaPartidos.get(candidato.getNum_partido()).getSigla(),candidato.getVotos_nominais());
        }

        // 3.imprimir candidatos mais votados dentro do numero de vagas
        //ordenar candidatos pelo numero de votos
        
        candidatos.sort((Candidato c1, Candidato c2) -> {
            if(c1.getVotos_nominais() == c2.getVotos_nominais()){
                return c1.getData_nasc().compareTo(c2.getData_nasc());
            };
            return c2.getVotos_nominais() - c1.getVotos_nominais();
        });
        

        System.out.println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        int cont = 0;
        for(Candidato candidato : candidatos){
            if(cont < numeroEleitos){
                cont++;
                System.out.printf("%s / %s (%s, %d votos)\n",candidato.getNome(),candidato.getNome_urna(),mapaPartidos.get(candidato.getNum_partido()).getSigla(),candidato.getVotos_nominais());
            }
        }

        // 4. Candidatos não eleitos e que seriam eleitos se a votação fosse majoritária

        System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");

        int cont2 = 0;
        for(Candidato candidato : candidatos){
            if(cont2 < numeroEleitos){
                cont2++;
                if(candidato.getSituacao().equals("Não eleito") || candidato.getSituacao().equals("Suplente")){
                    System.out.printf("%s / %s (%s, %d votos)\n",candidato.getNome(),candidato.getNome_urna(),mapaPartidos.get(candidato.getNum_partido()).getSigla(),candidato.getVotos_nominais());
                }

            }
            
        }

        // 5. Candidatos eleitos no sistema proporcional vigente, e que não seriam eleitos se a votação fosse
        // majoritária, isto é, pelo número de votos apenas que um candidato recebe diretamente;

        int menorVotos;
           
        menorVotos = candidatos.get(numeroEleitos -1).getVotos_nominais();

        System.out.print("\nEleitos, que se beneficiaram do sistema proporcional:\n");

        for(Candidato candidatoEle : candidatosEleitos){
            if(candidatoEle.getVotos_nominais() < menorVotos){
                
                System.out.printf("%s / %s (%s, %d votos)\n",candidatoEle.getNome(),candidatoEle.getNome_urna(),mapaPartidos.get(candidatoEle.getNum_partido()).getSigla(),candidatoEle.getVotos_nominais());
            }
        }

        // 6.Votos totalizados por partido e número de candidatos eleitos;


        for(Partidos partido : partidos){  
            int votosPartido = 0;   
            for(Candidato candidato : candidatos){
                if(candidato.getNum_partido() == partido.getNum_partido()){                   
                    votosPartido += candidato.getVotos_nominais();
                    partido.candidatosDoPartido.add(candidato);
                }
            }
            partido.setVotosNominais_partido(votosPartido);       
            partido.setVotosTotais(partido.getVotosNominais_partido() + partido.getVotosLegenda());

            // System.out.printf("%s - %d: %d votos (%d nominais e %d de legenda), %d candidatos eleitos\n",partido.getSigla(),partido.getNum_partido(),partido.getVotosTotais(),partido.getVotosNominais_partido(),partido.getVotos_legenda(),partido.candidatosDoPartido.size());          
        }

        //ordernar partidos por votos
        partidos.sort((Partidos p1, Partidos p2) -> {
            if(p1.getVotosTotais() == p2.getVotosTotais()){
                return p1.getNum_partido() - p2.getNum_partido();
            };
            return p2.getVotosTotais() - p1.getVotosTotais();
        });
        
        //imprimir partidos ordenados
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:\n");

        for(Partidos partido : partidos){
            System.out.printf("%s - %d: %d votos (%d nominais e %d de legenda), %d candidatos eleitos\n",partido.getSigla(),partido.getNum_partido(),partido.getVotosTotais(),partido.getVotosNominais_partido(),partido.getVotos_legenda(),partido.candidatosDoPartido.size());
        }
      

        //7. Votos de legenda (com a porcentagem destes votos frente ao total de votos no partido)
        

        //ordernar partidos por votos de legenda
        
        partidos.sort((Partidos p1,Partidos p2) -> {
            if(p1.getVotos_legenda() == p2.getVotos_legenda()){
                if(p1.getNum_partido() == p2.getNum_partido()){
                    return p1.getVotosNominais_partido() - p2.getVotosNominais_partido();
                }
                return p1.getVotosNominais_partido() - p2.getVotosNominais_partido();
            };
            return p2.getVotos_legenda() - p1.getVotos_legenda();
        });
        
        System.out.print("\nVotação dos partidos (apenas votos de legenda):\n");

        for(Partidos partido : partidos){
            float porcentagem = (float)partido.getVotos_legenda() / partido.getVotosTotais() * 100;

            if(porcentagem == 0){
                System.out.printf("%s - %d, %d votos de legenda (proporção não calculada, 0 voto no partido)\n",partido.getSigla(),partido.getNum_partido(),partido.getVotos_legenda());
                continue;
            }
            
            System.out.printf("%s - %d, %d votos de legenda (%.2f%% do total do partido)\n",partido.getSigla(),partido.getNum_partido(),partido.getVotos_legenda(),porcentagem);
        }

        //      8.8. Primeiro e último colocados de cada partido (com nome da urna, número do candidato e total de votos
        // nominais). Partidos que não possuírem candidatos com um número positivo de votos válidos devem ser
        // ignorados;

        System.out.print("\nPrimeiro e último colocados de cada partido:\n");
    

        for(Partidos partido : partidos){
            partido.candidatosDoPartido.sort((Candidato c1, Candidato c2) -> {
                if(c1.getVotos_nominais() == c2.getVotos_nominais()){
                    return c1.getIdade() - c2.getIdade();
                }                
                return c2.getVotos_nominais() - c1.getVotos_nominais();

            });
        }

        partidos.sort((Partidos p1,Partidos p2) -> {
            if(p1.candidatosDoPartido.get(0).getVotos_nominais() == p2.candidatosDoPartido.get(0).getVotos_nominais()){
                return p1.getNum_partido() - p2.getNum_partido();
            }

            return p2.candidatosDoPartido.get(0).getVotos_nominais() - p1.candidatosDoPartido.get(0).getVotos_nominais();
        });

        cont = 1;
        for(Partidos partido : partidos){
            Candidato maisVotado = partido.candidatosDoPartido.get(0);
            Candidato menosVotado = partido.candidatosDoPartido.get(partido.candidatosDoPartido.size() - 1);

            if(partido.getVotosLegenda() == 0){
                continue;
            }

            System.out.printf("%d - %s - %d, %s (%d, %d votos) / %s (%d, %d votos)\n",cont,partido.getSigla(),partido.getNum_partido(),maisVotado.getNome_urna(),maisVotado.getNumero(),maisVotado.getVotos_nominais(),menosVotado.getNome_urna(),menosVotado.getNumero(),menosVotado.getVotos_nominais());
            cont++;
        }



    }
}

        