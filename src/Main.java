package src;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

public class Main{
    public static void main(String[] args){
        
        //criar data 15/11/2020
        LocalDate data = LocalDate.parse(args[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<String> candidatosLinhas = new ArrayList<String>();

        try{
            candidatosLinhas = new LeituraCSV(args[0]).lerArquivo();

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
            partidosLinhas = new LeituraCSV(args[1]).lerArquivo();

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
        

        //imprimir candidatos eleitos ordenados
        System.out.println("\nVereadores eleitos:");
        int cont = 1;
        for(Candidato candidato : candidatosEleitos){
            System.out.printf("%d - %s / %s (%s, %d votos)\n",cont,candidato.getNome(),candidato.getNome_urna(),mapaPartidos.get(candidato.getNum_partido()).getSigla(),candidato.getVotos_nominais());
            cont++;
        }

        // 3.imprimir candidatos mais votados dentro do numero de vagas
        //ordenar candidatos pelo numero de votos
        
        candidatos.sort((Candidato c1, Candidato c2) -> {
            if(c1.getVotos_nominais() == c2.getVotos_nominais()){
                return c1.getData_nasc().compareTo(c2.getData_nasc());
            };
            return c2.getVotos_nominais() - c1.getVotos_nominais();
        });

        // setando o rank de cada candidato de acordo com seu numero de votos
        int cont1 = 1;
        for(Candidato candidato : candidatos){
            candidato.setRank(cont1);
            cont1++;
        }
        

        System.out.println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        cont = 0;
        for(Candidato candidato : candidatos){
            if(cont < numeroEleitos){
                cont++;
                System.out.printf("%d - %s / %s (%s, %d votos)\n",cont,candidato.getNome(),candidato.getNome_urna(),mapaPartidos.get(candidato.getNum_partido()).getSigla(),candidato.getVotos_nominais());
            }
        }

        // 4. Candidatos não eleitos e que seriam eleitos se a votação fosse majoritária

        System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");

        int cont2 = 0;
        for(Candidato candidato : candidatos){
            if(cont2 < numeroEleitos){
                cont2++;
                if(candidato.getSituacao().equals("Não eleito") || candidato.getSituacao().equals("Suplente")){                 
                    System.out.printf("%d - %s / %s (%s, %d votos)\n",candidato.getRank(),candidato.getNome(),candidato.getNome_urna(),mapaPartidos.get(candidato.getNum_partido()).getSigla(),candidato.getVotos_nominais());
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
                System.out.printf("%d - %s / %s (%s, %d votos)\n",candidatoEle.getRank(),candidatoEle.getNome(),candidatoEle.getNome_urna(),mapaPartidos.get(candidatoEle.getNum_partido()).getSigla(),candidatoEle.getVotos_nominais());
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

        cont = 0;
        for(Partidos partido : partidos){
            cont++;
            System.out.printf("%d - %s - %d: %d votos (%d nominais e %d de legenda), %d candidatos eleitos\n",cont,partido.getSigla(),partido.getNum_partido(),partido.getVotosTotais(),partido.getVotosNominais_partido(),partido.getVotos_legenda(),partido.candidatosDoPartido.size());
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

        cont = 0;

        for(Partidos partido : partidos){
            float porcentagem = (float)partido.getVotos_legenda() / partido.getVotosTotais() * 100;

            if(porcentagem == 0){
                cont++;
                System.out.printf("%d - %s - %d, %d votos de legenda (proporção não calculada, 0 voto no partido)\n",cont,partido.getSigla(),partido.getNum_partido(),partido.getVotos_legenda());               
                continue;
            }
            cont++;
            System.out.printf("%d - %s - %d, %d votos de legenda (%.2f%% do total do partido)\n",cont,partido.getSigla(),partido.getNum_partido(),partido.getVotos_legenda(),porcentagem);
        }

        //      8.8. Primeiro e último colocados de cada partido (com nome da urna, número do candidato e total de votos
        // nominais). Partidos que não possuírem candidatos com um número positivo de votos válidos devem ser
        // ignorados;

        System.out.print("\nPrimeiro e último colocados de cada partido:\n");
    

        for(Partidos partido : partidos){
            partido.candidatosDoPartido.sort((Candidato c1, Candidato c2) -> {
                if(c1.getVotos_nominais() == c2.getVotos_nominais()){
                    return c1.getIdade(data) - c2.getIdade(data);
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

        //9. Distribuição de eleitos por faixa etária, considerando a idade do candidato no dia da eleição;

        //percorrer lista de candidatos eleitos
        
        int menorQue30 = 0;
        int entre30e40 = 0;
        int entre40e50 = 0;
        int entre50e60 = 0;
        int maiorQue60 = 0;
        
        for(Candidato candidato : candidatosEleitos){
            if(candidato.getIdade(data) < 30){
                menorQue30++;
            }
            if(candidato.getIdade(data) >=30 && candidato.getIdade(data) < 40){
                entre30e40++;
            }
            if(candidato.getIdade(data) >=40 && candidato.getIdade(data) < 50){
                entre40e50++;
            }
            if(candidato.getIdade(data) >=50 && candidato.getIdade(data) < 60){
                entre50e60++;
            }
            if(candidato.getIdade(data) >=60){
                maiorQue60++;
            }
        }

        float porcentagemMenorQue30 = (float)menorQue30 / candidatosEleitos.size() * 100;
        float porcentagemEntre30e40 = (float)entre30e40 / candidatosEleitos.size() * 100;
        float porcentagemEntre40e50 = (float)entre40e50 / candidatosEleitos.size() * 100;
        float porcentagemEntre50e60 = (float)entre50e60 / candidatosEleitos.size() * 100;
        float porcentagemMaiorQue60 = (float)maiorQue60 / candidatosEleitos.size() * 100;
        
        System.out.printf("\nEleitos, por faixa etária (na data da eleiçaõ):\n");
        System.out.printf("Idade < 30 : %d (%.2f%%)\n",menorQue30,porcentagemMenorQue30);
        System.out.printf("30 <= Idade < 40 : %d (%.2f%%)\n",entre30e40,porcentagemEntre30e40);
        System.out.printf("40 <= Idade < 50 : %d (%.2f%%)\n",entre40e50,porcentagemEntre40e50);
        System.out.printf("50 <= Idade < 60 : %d (%.2f%%)\n",entre50e60,porcentagemEntre50e60);
        System.out.printf("Idade <= 60 : %d (%.2f%%)\n\n",maiorQue60,porcentagemMaiorQue60);

        //10. Distribuição de eleitos por sexo;
        int homens = 0;
        int mulheres = 0;

        for(Candidato candidato : candidatosEleitos){
            if(candidato.getSexo().equals("M")){
                homens++;
            }
            if(candidato.getSexo().equals("F")){
                mulheres++;
            }
        }

        //imprimir resultados
        float porcentagemHomens = (float)homens / candidatosEleitos.size() * 100;
        float porcentagemMulheres = (float)mulheres / candidatosEleitos.size() * 100;

        System.out.printf("Eleitos, por sexo:\n");
        System.out.printf("Feminino: %d (%.2f%%)\n",mulheres,porcentagemMulheres);
        System.out.printf("Masculino: %d (%.2f%%)\n\n",homens,porcentagemHomens);

        // 11. total de votos válidos
        int totalVotosValidos = 0;
        int totaldeVotosNominais = 0;
        int totaldeVotosLegenda = 0;

        for(Candidato candidato : candidatos){
            if(candidato.getDestino_voto().equals("Válido")){
                totaldeVotosNominais += candidato.getVotos_nominais();
            }
        }
        for(Partidos partido : partidos){
            totaldeVotosLegenda += partido.getVotosLegenda();
        }

        totalVotosValidos = totaldeVotosNominais + totaldeVotosLegenda;

        //calcular porcentagem

        float porcetagemNominais = (float)totaldeVotosNominais / totalVotosValidos * 100;
        float porcetagemLegenda = (float)totaldeVotosLegenda / totalVotosValidos * 100;

        System.out.printf("Total de votos válidos: %d\n",totalVotosValidos);
        System.out.printf("Total de votos nominais: %d (%.2f%%)\n",totaldeVotosNominais,porcetagemNominais);
        System.out.printf("Total de votos legenda: %d (%.2f%%)\n",totaldeVotosLegenda,porcetagemLegenda);
    }
}

        