package project.main;
import java.util.HashMap;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spark.ModelAndView;

public class Main {
    public static void main(String args[]){
        port(5000);
        get("/", (req,res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        get("/list", (req,res) -> "Recommendation list");
        get("/login", (req,res) -> "Login page");
        get("/signup", (req,res) -> "Signup page");
        get("/post", (req,res) -> "Post a new recommendation");
    }
}
