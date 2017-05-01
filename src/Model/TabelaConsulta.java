/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * Entidade de TabelaConsulta para montar a tabela da Classe ConsultaArea
 * @author João Felipe
 */
public class TabelaConsulta {
    private String data;
    private String min;
    private String max;
    private String utah;
    private String carolina;
    private String ponderada;

    public TabelaConsulta() {
        
    }  

    public TabelaConsulta(String data, String min, String max, String utah, String carolina, String ponderada) {
        this.data = data;
        this.min = min;
        this.max = max;
        this.utah = utah;
        this.carolina = carolina;
        this.ponderada = ponderada;
    }
    
    
    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public String getMax() {
        return max;
    }

    /**
     * @return the utah
     */
    public String getUtah() {
        return utah;
    }

    /**
     * @return the carolina
     */
    public String getCarolina() {
        return carolina;
    }

    /**
     * @return the ponderada
     */
    public String getPonderada() {
        return ponderada;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @param min the min to set
     */
    public void setMin(String min) {
        this.min = min;
    }

    /**
     * @param max the max to set
     */
    public void setMax(String max) {
        this.max = max;
    }

    /**
     * @param utah the utah to set
     */
    public void setUtah(String utah) {
        this.utah = utah;
    }

    /**
     * @param carolina the carolina to set
     */
    public void setCarolina(String carolina) {
        this.carolina = carolina;
    }

    /**
     * @param ponderada the ponderada to set
     */
    public void setPonderada(String ponderada) {
        this.ponderada = ponderada;
    }   
}
