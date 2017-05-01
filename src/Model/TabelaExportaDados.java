/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 *
 * @author joao
 */
public class TabelaExportaDados {
    private String data;
    private String hora21ant;
    private String min;
    private String max;
    private String hora21;
    private String totalUtah;
    private String totalCarolina;
    private String totalPonderada; // total do dia

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the hora21ant
     */
    public String getHora21ant() {
        return hora21ant;
    }

    /**
     * @param hora21ant the hora21ant to set
     */
    public void setHora21ant(String hora21ant) {
        this.hora21ant = hora21ant;
    }

    /**
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(String min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public String getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(String max) {
        this.max = max;
    }

    /**
     * @return the hora21
     */
    public String getHora21() {
        return hora21;
    }

    /**
     * @param hora21 the hora21 to set
     */
    public void setHora21(String hora21) {
        this.hora21 = hora21;
    }

    /**
     * @return the totalUtah
     */
    public String getTotalUtah() {
        return totalUtah;
    }

    /**
     * @param totalUtah the totalUtah to set
     */
    public void setTotalUtah(String totalUtah) {
        this.totalUtah = totalUtah;
    }

    /**
     * @return the totalCarolina
     */
    public String getTotalCarolina() {
        return totalCarolina;
    }

    /**
     * @param totalCarolina the totalCarolina to set
     */
    public void setTotalCarolina(String totalCarolina) {
        this.totalCarolina = totalCarolina;
    }

    /**
     * @return the totalPonderada
     */
    public String getTotalPonderada() {
        return totalPonderada;
    }

    /**
     * @param totalPonderada the totalPonderada to set
     */
    public void setTotalPonderada(String totalPonderada) {
        this.totalPonderada = totalPonderada;
    }
}
