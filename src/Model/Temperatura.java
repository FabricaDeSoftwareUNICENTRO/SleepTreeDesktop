/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Controller.ConectandoBanco;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Aqui vai ter os calculos de interpolação
 * @author João Felipe
 */
public class Temperatura {
      
      
      private float Ty, Tv, Tz;
      ConectandoBanco conect = new ConectandoBanco();
      Unidade uni = new Unidade();
      DecimalFormat df = new DecimalFormat("0.00"); 
    public Temperatura() {
    }
    
    /**
     * Interpola temperaturas
     * @param T21a temperatura das 21h do dia anterior
     * @param Tm temperatura minima
     * @param Tx temperatura máxima
     * @param T21d temperatura das 21h do dia atual
     * @param codArea codigo da Area
     * @param gc variavel para usar o GregorianCalendar
     * @return ArrayList de ArrayList com um ArrayList das temperaturas do dia anterior das 21 as 24h e o outro ArrayList com as temperatuas da meia noite até as 21h do dia atual
     * @throws java.lang.Exception teste
     */
    public ArrayList<ArrayList> interpolaTempTres(float T21a, float Tm,float  Tx, float T21d, String codArea, GregorianCalendar gc) throws Exception{
        GregorianCalendar gcDiaAtual = gc;
       ArrayList<Float> Temp = new ArrayList<>();
       ArrayList<Float> Tempant = new ArrayList<>();
       // float T21a, Tm, Tx, T21d, Temp[24],Tempant[24], Ty, Tv, Tz;
       int cont=1, i;
       if(T21a == 99987){
           // SÓ PARA PEGAR A HORA 21 DO DIA ANTERIOR
           System.out.println("caiu no IF 99987");
           gc.add(Calendar.DAY_OF_MONTH, -1);
           String diaAnterior = conect.dataAntString(gc);
           
           System.out.println("Dia Anterior Temperatura Interpola: "+diaAnterior);
           conect.conecta();
           ResultSet rs = conect.execRetorna("SELECT hora21 FROM Temperatura WHERE codTemperatura='"+diaAnterior+"' and codArea='"+codArea+"'");
           System.out.println("passou do result 1");
           String hora21 = rs.getString("hora21");
           System.out.println("Hora21: "+hora21);
           float hora21DiaAnterior = Float.parseFloat(hora21);
           Tempant.add(hora21DiaAnterior);
           T21a = hora21DiaAnterior;
           gc.add(Calendar.DAY_OF_MONTH, +1);
           conect.desconecta();
       }
       else{
           Tempant.add(T21a);
       }
       
        Ty = T21a-Tm;
        Tv = Tx-Tm;
        Tz = Tx-T21d;
        for(i=21;i<24;i++){
		Tempant.add(T21a -((cont*Ty)/9));
		cont++;
		}
        for(i=0;i<5;i++){
		Temp.add(T21a -((cont*Ty)/9));
		cont++;
        }
        Temp.add(Tm);
        cont=1;

        for(i=6;i<15;i++){
            if (i>5 && i<14){
                Temp.add(Temp.get(5) + ((cont*Tv)/9));
            }
            if(i == 14){
                Temp.add(Tx);
            }
        cont++;
        }
        cont=1;      
        for(i=15;i<21;i++){
            if(i>14 && i<20){
                Temp.add(Temp.get(14) - ((cont*Tz)/6));
            }
            if(i==20){
                Temp.add(T21d);
            }
        cont++;
        }
        ArrayList<ArrayList> temps = new ArrayList<>();
        temps.clear();
        temps.add(Tempant);
        temps.add(Temp);

        return temps;
    }

    /**
     * SQLite aceita somente o padrão YYYY-MM-DD, mas o calendário retorna
     * no formato DD/MM/YYYY, portando é preciso converter.
     * @param data retornada pelo calendario no formato DD/MM/YYYY
     * @return data convertida no formato YYYY-MM-DD
     */
    public String converteData(String data){
        //Pega o dia
        String dia = data.substring(0, 2);
        //Pega o mes
        String mes = data.substring(3, 5);
        //Pega o ano
        String ano = data.substring(6, 10);
        
        //Junta tudo
        String novaData = ano+"-"+mes+"-"+dia;
        return novaData;
    }
}
