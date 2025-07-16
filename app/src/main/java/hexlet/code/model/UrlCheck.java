package hexlet.code.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlCheck {
    private Long id;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private Long urlId;
    private LocalDateTime createdAt;

    public UrlCheck(int statusCode, String title, String h1, String description, Long urlId) {
        this.urlId = urlId;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.statusCode = statusCode;
    }
}
