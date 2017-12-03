package tikape.runko;

import java.util.HashMap;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.AnnosRaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:reseptit.db");
        database.init();
        
        AnnosRaakaAineDao annosraakaaineet = new AnnosRaakaAineDao(database);
        RaakaAineDao raaka_aineet = new RaakaAineDao(database);
        AnnosDao annokset = new AnnosDao(database);
        
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/annokset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annokset.findAll());

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());

        Spark.post("/annokset", (req, res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"), new ArrayList<AnnosRaakaAine>());
            annokset.saveOrUpdate(annos);

            res.redirect("/annokset");
            return "";
        });
        
        Spark.get("/raaka_aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raaka_aineet", raaka_aineet.findAll());

            return new ModelAndView(map, "raaka_aineet");
        }, new ThymeleafTemplateEngine());

        Spark.post("/raaka_aineet", (req, res) -> {
            RaakaAine raaka_aine = new RaakaAine(-1, req.queryParams("nimi"));
            raaka_aineet.saveOrUpdate(raaka_aine);

            res.redirect("/raaka_aineet");
            return "";
        });
        
        Spark.get("/annokset/:id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            HashMap map = new HashMap<>();
            map.put("annos", annokset.findOne(annosId));
            map.put("raaka_aineet", raaka_aineet.findAll());

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());

        Spark.post("/annokset/:id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            String action = req.queryParams("action");
            if (action.equals("add")){
                Integer jarjestys = annosraakaaineet.findMaxOrderingByAnnosId(annosId);
                AnnosRaakaAine annosraakaaine = new AnnosRaakaAine(-1, req.queryParams("ohje"),
                        jarjestys + 1, Double.parseDouble(req.queryParams("maara")),
                        raaka_aineet.findOne(Integer.parseInt(req.queryParams("raaka_aine"))));


                annosraakaaineet.save(annosraakaaine, annosId);

                res.redirect("/annokset/" + annosId);
            }
            
            else if (action.equals("delete")){
                annokset.delete(annosId);
                res.redirect("/annokset");
            }
            return "";
        });

        Spark.post("/annokset/:id/:annosraaka_aine_id", (req, res) -> {
            Integer annosId = Integer.parseInt(req.params(":id"));
            Integer annosraaka_aineId = Integer.parseInt(req.params(":annosraaka_aine_id"));
            annosraakaaineet.delete(annosraaka_aineId);
            
            res.redirect("/annokset/" + annosId);
            return "";
        });
    }
}
