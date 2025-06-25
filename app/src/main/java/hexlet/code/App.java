package hexlet.code;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class App {
    public static Javalin getApp() { //throws SQLException

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx ->{ctx.result("Hello World");});
        return app;
    }

    public static void main(String[] args) { //throws SQLException
        Javalin app = getApp();
        app.start(7070);
    }
}
