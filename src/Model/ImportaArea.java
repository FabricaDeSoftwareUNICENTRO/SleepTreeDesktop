/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 *
 * @author João Felipe
 */
public class ImportaArea {
    private String data;
    private String hora21ant;
    private String min;
    private String max;
    private String hora21;

    public ImportaArea() {
    }

    public ImportaArea(String data, String hora21ant, String min, String max, String hora21) {
        this.data = data;
        this.hora21ant = hora21ant;
        this.min = min;
        this.max = max;
        this.hora21 = hora21;
    }
    
    

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
    
    
}
