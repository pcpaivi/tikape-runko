/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ppank
 */
public class Annos {
    
    private Integer id;
    private String nimi;
    private List<AnnosRaakaAine> raaka_aineet;

    public Annos(Integer id, String nimi, List<AnnosRaakaAine> raaka_aineet) {
        this.id = id;
        this.nimi = nimi;
        this.raaka_aineet = raaka_aineet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public List<AnnosRaakaAine> getRaaka_aineet() {
        return raaka_aineet;
    }

    public void setRaaka_aineet(List<AnnosRaakaAine> raaka_aineet) {
        this.raaka_aineet = raaka_aineet;
    }
    
}
