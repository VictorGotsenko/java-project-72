package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import static hexlet.code.util.AppSettings.FLASH;
import static hexlet.code.util.AppSettings.FLASH_DANGER;
import static hexlet.code.util.AppSettings.FLASH_INFO;
import static hexlet.code.util.AppSettings.FLASH_SUCCESS;
import static hexlet.code.util.AppSettings.FLASH_TYPE;
import static hexlet.code.util.AppSettings.FLASH_WARNING;
import static hexlet.code.util.AppSettings.PAGE_ADDED;
import static hexlet.code.util.AppSettings.PAGE_EXIST;
import static hexlet.code.util.AppSettings.URL_BAD;
import static hexlet.code.util.AppSettings.URL_EMPTY;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import static io.javalin.rendering.template.TemplateUtil.model;

import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;

@Slf4j
public final class UrlController {
    private UrlController() {
        // Sonar warning
        // Prevent instantiation
    }

    public static void build(Context ctx) {
        BasePage page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(FLASH_TYPE));
        ctx.render("urls/build.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException, URISyntaxException, MalformedURLException {
        String name = ctx.formParamAsClass("url", String.class).get();
        URL unifmResourceId;
        try {
            unifmResourceId = new URI(name).toURL();
        } catch (Exception e) {
            ctx.sessionAttribute(FLASH, URL_BAD);
            ctx.sessionAttribute(FLASH_TYPE, FLASH_DANGER);
            ctx.redirect(NamedRoutes.buildPath());
            return;
        }

        if (name.isEmpty()) {
            ctx.sessionAttribute(FLASH, URL_EMPTY);
            ctx.sessionAttribute(FLASH_TYPE, FLASH_WARNING);
            ctx.redirect(NamedRoutes.buildPath());
            return;
        }

        String protocol = unifmResourceId.getProtocol();
        int port = unifmResourceId.getPort();
        String host = unifmResourceId.getHost();

        URL url = new URIBuilder().setScheme(protocol).setHost(host).setPort(port).build().toURL();

        if (UrlRepository.findByName(String.valueOf(url)).isEmpty()) {
            Url newUrl = new Url(String.valueOf(url), LocalDateTime.now());
            UrlRepository.save(newUrl);
        } else {
            ctx.sessionAttribute(FLASH, PAGE_EXIST);
            ctx.sessionAttribute(FLASH_TYPE, FLASH_INFO);
            ctx.redirect(NamedRoutes.urlsPath());
            return;
        }

        ctx.sessionAttribute(FLASH, PAGE_ADDED);
        ctx.sessionAttribute(FLASH_TYPE, FLASH_SUCCESS);
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> latestChecks = UrlCheckRepository.getLastestChecks();

        UrlsPage page = new UrlsPage(urls, latestChecks);
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(FLASH_TYPE));
        ctx.render("urls/indexList.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        List<UrlCheck> urlCheckList = UrlCheckRepository.findById(id);
        if (!urlCheckList.isEmpty()) {
            url.setUrlCheckList(urlCheckList);
        }
        UrlPage page = new UrlPage(url);
        page.setFlash(ctx.consumeSessionAttribute(FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(FLASH_TYPE));
        ctx.render("urls/show.jte", model("page", page));
    }
}
