package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<UrlCheck> urlCheckList;

    public Url(String name) {
        this.name = name;
        this.urlCheckList = new ArrayList<>();
    }
}
