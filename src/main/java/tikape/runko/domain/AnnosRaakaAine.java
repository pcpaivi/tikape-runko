/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author ppank
 */
public class AnnosRaakaAine {
    private Integer id;
    private String ohje;
    private Integer jarjestys;
    private Double maara;
    private RaakaAine raaka_aine;

    public AnnosRaakaAine(Integer id, String ohje, Integer jarjestys, Double maara, RaakaAine raakaaine) {
        this.id = id;
        this.ohje = ohje;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.raaka_aine =raakaaine;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
    
    public Integer getJarjestys() {
        return jarjestys;
    }

    public void setJarjestys(Integer jarjestys) {
        this.jarjestys = jarjestys;
    }
    
    public Double getMaara() {
        return maara;
    }

    public void setMaara(Double maara) {
        this.maara = maara;
    }
    
    public RaakaAine getRaaka_aine() {
        return raaka_aine;
    }

    public void setRaaka_aine(RaakaAine raakaaine) {
        this.raaka_aine = raakaaine;
    }
    
}
