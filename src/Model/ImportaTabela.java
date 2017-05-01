/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author Fedacz Desktop
 */
public class ImportaTabela {

    public ArrayList<ArrayList> carregaLinha(String endereco) throws IOException, BiffException {
        ArrayList<ArrayList> importa = new ArrayList<>();
//        Workbook workbook = Workbook.getWorkbook(new File("exemplo.xls"));
        Workbook workbook = Workbook.getWorkbook(new File(endereco));
        Sheet sheet = workbook.getSheet(0);
        int linhas = sheet.getRows();
        for (int i = 1; i < (linhas); i++) {                 
           ArrayList<String> dia = new ArrayList<>();        
            for (int j = 0; j < 5; j++) {
                Cell celula1 = sheet.getCell(j, i);
                dia.add(celula1.getContents());
            }
            importa.add(dia);
        } 
        for (ArrayList importa1 : importa) {    //TESTE 
            for (Object dia1 : importa1) {
                System.out.println(dia1);
            }
            System.out.println("/-------------------------------/");
        }
      
        return (importa);
    }

}
