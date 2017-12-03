/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;


/**
 *
 * @author ppank
 */
public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer>{
    
    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    
}   
    public Integer findMaxOrderingByAnnosId(Integer key) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                    "SELECT MAX (jarjestys) AS maxjarjestys  "
                            + "FROM AnnosRaakaAine "
                            + "WHERE annos_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return 0;
        }
        
        Integer jarjestys = rs.getInt("maxjarjestys");
        
        rs.close();
        stmt.close();        
        connection.close();
        return jarjestys;
    }
    
@Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                    "SELECT AnnosRaakaAine.id, ohje, jarjestys, maara, raaka_aine_id, RaakaAine.nimi as raaka_aine_nimi, annos_id "
                            + "FROM AnnosRaakaAine, RaakaAine "
                            + "WHERE AnnosraakaAine.raaka_aine_id = RaakaAine.id and AnnosRaakaAine.id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String ohje = rs.getString("ohje");
        Integer jarjestys = rs.getInt("jarjestys");
        Double maara = rs.getDouble("maara");
        Integer raaka_aine_id = rs.getInt("raaka_aine_id");
        String raaka_aine_nimi = rs.getString("raaka_aine_nimi");
        
        RaakaAine raakaaine = new RaakaAine(raaka_aine_id, raaka_aine_nimi);

        AnnosRaakaAine ara = new AnnosRaakaAine(id, ohje, jarjestys, maara, raakaaine);

        rs.close();
        stmt.close();
        connection.close();

        return ara;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {

       return null;
    }

    public void save(AnnosRaakaAine annosraakaaine, Integer annos_id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine (raaka_aine_id, annos_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, annosraakaaine.getRaaka_aine().getId());
            stmt.setInt(2, annos_id);
            stmt.setInt(3, annosraakaaine.getJarjestys());
            stmt.setDouble(4, annosraakaaine.getMaara());
            stmt.setString(5, annosraakaaine.getOhje());
            stmt.executeUpdate();
            
            stmt.close();
        }
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE id = ?");
            stmt.setInt(1, key);
            
            stmt.executeUpdate();
            
            stmt.close();
        }
    }
    
}
