package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass  // for Sonar warning
public final class UrlCheckRepository {

    public static void save(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        } catch (SQLException e) {
            log.info("Error in UrlCheckRepository.save ", e);
        }
    }

    public static List<UrlCheck> findById(Long urlId) throws SQLException {
        List<UrlCheck> result = new ArrayList<>();
        // Sonar warning " * "
        String sql = "SELECT id, status_code, title, h1, description, url_id, created_at"
                + " FROM url_checks WHERE url_id = ?";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UrlCheck urlCheck = new UrlCheck(
                        resultSet.getInt("status_code"),
                        resultSet.getString("title"),
                        resultSet.getString("h1"),
                        resultSet.getString("description"),
                        urlId);
                urlCheck.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                urlCheck.setId(resultSet.getLong("id"));
                result.add(urlCheck);
            }
        } catch (SQLException e) {
            log.info("Error in UrlCheckRepository.findById ", e);
        }
        return result;
    }

    public static Map<Long, UrlCheck> getLatestChecks() throws SQLException {
        Map<Long, UrlCheck> result = new HashMap<>();
        String sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UrlCheck urlCheck = new UrlCheck(
                        resultSet.getInt("status_code"),
                        resultSet.getString("title"),
                        resultSet.getString("h1"),
                        resultSet.getString("description"),
                        resultSet.getLong("url_id"));
                urlCheck.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                urlCheck.setId(resultSet.getLong("id"));
                result.put(resultSet.getLong("url_id"), urlCheck);
            }
        } catch (SQLException e) {
            log.info("Error in UrlCheckRepository.getLatestChecks ", e);
        }
        return result;
    }
}
