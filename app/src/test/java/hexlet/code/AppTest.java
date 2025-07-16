package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;

import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AppTest {
    Javalin app;
    private static MockWebServer mockServer;

    public static String readFixture(String fileName) throws IOException {
        Path filePath = Paths.get("src/test/resources/fixtures", fileName);
        return new String(Files.readAllBytes(filePath));
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse().setBody(readFixture("test.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @BeforeEach
    final void beforeEach() throws SQLException, IOException {
        app = App.getApp();
        // Очистка базы данных перед каждым тестом
        UrlRepository.clear();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
    }


    //        Main page
    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            // Response response = client.get("/")
            assertThat(client.get("/").code()).isEqualTo(200);
            assertThat(client.get("/").body().string()).contains("Анализатор страниц");
        });
    }

    //save URL in BD
    @Test
    void testUrlSave() throws SQLException {
        Url url = new Url("https://mail.ru/");
        url.setCreatedAt(LocalDateTime.now());
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            String requestedBody = "url=https://ya.ru/";
            Response response = client.post("/urls", requestedBody);
            Optional<Url> url = UrlRepository.findByName("https://ya.ru");
            assertThat(url.get().getName()).isEqualTo("https://ya.ru");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://ya.ru");
        });
    }

    @Test
    void testBuidPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/urls/build");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });

    }

    @Test
    void testAllUrls() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCheck() throws SQLException {
        String mockUrl = mockServer.url("/").toString();
        Url url = new Url(mockUrl);
        url.setCreatedAt(LocalDateTime.now());
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            Url savedUrl = UrlRepository.findByName(mockServer.url("/").toString()).orElseThrow();
            Response response = client.post(NamedRoutes.urlPathForChecks(savedUrl.getId()));
            assertThat(response.code()).isEqualTo(200);
            List<UrlCheck> urlCheckList = UrlCheckRepository.findById(savedUrl.getId());
            assertThat(!urlCheckList.isEmpty());
        });
    }

    @Test
    void testNoExistEntry() throws SQLException {
        Optional<Url> url = UrlRepository.find(999L);
        assertThat(url.isEmpty());
    }
}
