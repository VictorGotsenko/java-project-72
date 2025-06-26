package hexlet.code;

import hexlet.code.repository.BaseRepository;
// import io.hexlet.component.DataInitializer;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import lombok.extern.slf4j.Slf4j;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;


@Slf4j
public class App {


    private static String getDatabaseUrl() {
        // Получаю url базы данных из переменной окружения DATABASE_URL
        // Если она не установлена, используем базу в памяти
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }


    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static Javalin getApp() throws IOException, SQLException { //
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        var sql = readResourceFile("schema.sql");

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;


        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx -> {
            ctx.result("Hello World");
        });
        return app;
    }


    public static void main(String[] args) throws IOException, SQLException { //throws SQLException
        Javalin app = getApp();
        app.start(7070);
    }
}
