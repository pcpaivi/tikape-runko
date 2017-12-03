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
import tikape.runko.domain.Annos;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.AnnosRaakaAine;

/**
 *
 * @author ppank
 */
public class AnnosDao implements Dao<Annos , Integer> {
    
    private Database database;

    public AnnosDao(Database database) {
        this.database = database;
    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        
        stmt = connection.prepareStatement("SELECT AnnosRaakaAine.id, ohje, jarjestys, maara, raaka_aine_id, RaakaAine.nimi as raaka_aine_nimi, annos_id "
                + "FROM AnnosRaakaAine, RaakaAine "
                + "WHERE AnnosraakaAine.raaka_aine_id = RaakaAine.id and annos_id = ? "
                + "ORDER BY jarjestys");
        stmt.setObject(1, key);

        rs = stmt.executeQuery();
        List<AnnosRaakaAine> raaka_aineet = new ArrayList<>();
        while (rs.next()) {
            Integer id2 = rs.getInt("id");
            String ohje = rs.getString("ohje");
            Integer jarjestys = rs.getInt("jarjestys");
            Double maara = rs.getDouble("maara");
            Integer raaka_aine_id = rs.getInt("raaka_aine_id");

            String raaka_aine_nimi = rs.getString("raaka_aine_nimi");
        
            RaakaAine raaka_aine = new RaakaAine(raaka_aine_id, raaka_aine_nimi);
            
            raaka_aineet.add(new AnnosRaakaAine(id2, ohje, jarjestys, maara, raaka_aine));
        }

        Annos a = new Annos(id, nimi, raaka_aineet);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Annos> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            annokset.add(new Annos(id, nimi, null));
        }

        rs.close();
        stmt.close();
        connection.close();

        return annokset;
    }

    public Annos saveOrUpdate(Annos object) throws SQLException {
        // simply support saving -- disallow saving if task with 
        // same name exists
        Annos byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        }

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
            stmt.setString(1, object.getNimi());
            stmt.executeUpdate();
        }

        return findByName(object.getNimi());

    }

    private Annos findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Annos WHERE nimi = ?");
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Annos(result.getInt("id"), result.getString("nimi"), null);
        }
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
            stmt.setInt(1, key);
            
            stmt.executeUpdate();
            
            stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");
            stmt.setInt(1, key);
            
            stmt.executeUpdate();
            
            stmt.close();
        }
    }
}
