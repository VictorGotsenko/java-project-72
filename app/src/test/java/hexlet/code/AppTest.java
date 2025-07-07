package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AppTest {
    Javalin app;

    @BeforeEach
    final void setUp() throws SQLException, IOException {
        app = App.getApp();
        // Очистка базы данных перед каждым тестом
        UrlRepository.clear();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    //save URL in BD
    @Test
    void testUrlSave() throws SQLException {
        var url = new Url("https://mail.ru/", LocalDateTime.now());
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            String requestedBody = "url=https://ya.ru/";
            var response = client.post("/urls", requestedBody);
            var url = UrlRepository.findByName("https://ya.ru");
            assertThat(url.get().getName()).isEqualTo("https://ya.ru");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://ya.ru");
        });
    }

    @Test
    void testAllUrls() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }
}
