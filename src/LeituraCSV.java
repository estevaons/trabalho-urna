package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LeituraCSV {
    String arquivoCSV;

    public List<String> listaLinhas;

    public LeituraCSV(String nomeArq){
        this.arquivoCSV = nomeArq;
        this.listaLinhas = new ArrayList<String>();
    }
    
    public List<String> lerArquivo() throws FileNotFoundException, IOException{
        InputStream is = new FileInputStream(this.arquivoCSV);
        InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String titulos = br.readLine();
        String linha = br.readLine();
        while(linha != null){
            this.listaLinhas.add(linha);
            linha = br.readLine();
            if(linha == null){
                br.close();
            }
        }
        return this.listaLinhas;
       
    }

}