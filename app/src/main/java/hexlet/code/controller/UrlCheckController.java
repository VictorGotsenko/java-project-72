package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import static hexlet.code.util.AppSettings.FLASH_TYPE;
import static hexlet.code.util.AppSettings.FLASH_DANGER;
import static hexlet.code.util.AppSettings.FLASH_SUCCESS;

import static hexlet.code.util.AppSettings.FLASH;
import static hexlet.code.util.AppSettings.CHECK_ERROR;
import static hexlet.code.util.AppSettings.PAGE_OK;
import static hexlet.code.util.AppSettings.URL_BAD;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
@UtilityClass  // for Sonar warning
public final class UrlCheckController {

    public static void check(Context ctx) throws SQLException {
        Long urlId = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + " not found"));
        log.info("Got URL ID: {}", urlId);
        try {
//            Unirest.config().proxy("127.0.0.1", 1080)
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document document = Jsoup.parse(response.getBody());

            int statusCode = response.getStatus();
            log.info("Response getStatus: {}", statusCode);
            String title = document.title();
            String h1 = document.select("h1").text();
            String description = document.select("meta[name=description]").attr("content");

            UrlCheck urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
            log.info("urlCheck created");
            UrlCheckRepository.save(urlCheck);
            log.info("check saved");
            ctx.sessionAttribute(FLASH, PAGE_OK);
            ctx.sessionAttribute(FLASH_TYPE, FLASH_SUCCESS);
        } catch (UnirestException e) {
            log.info("Error in UrlCheckController.check - UnirestException: ", e);
            ctx.sessionAttribute(FLASH, URL_BAD);
            ctx.sessionAttribute(FLASH_TYPE, FLASH_DANGER);
        } catch (SQLException e) {
            log.info("Error in UrlCheckController.check - SQLException", e);
            ctx.sessionAttribute(FLASH, CHECK_ERROR + e.getMessage());
            ctx.sessionAttribute(FLASH_TYPE, FLASH_DANGER);
        }
        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
