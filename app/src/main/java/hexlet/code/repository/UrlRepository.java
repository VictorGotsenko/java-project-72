package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass  // for Sonar warning
public final class UrlRepository extends BaseRepository {
    public static final String FIELD_CREATED_AT = "created_at";

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        String sql = "SELECT id, name, created_at FROM urls WHERE id = ?";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Url url = new Url(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp(FIELD_CREATED_AT).toLocalDateTime());
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT id, name, created_at FROM urls WHERE name = ?";
        try (Connection connection = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Url url = new Url(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp(FIELD_CREATED_AT).toLocalDateTime());
                url.setId(resultSet.getLong("id"));
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        //  String sql = "SELECT * FROM urls" - Sonar warning
        String sql = "SELECT id, name, created_at FROM urls";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Url> result = new ArrayList<>();
            while (resultSet.next()) {
                Url url = new Url(resultSet.getString("name"));
                url.setCreatedAt(resultSet.getTimestamp(FIELD_CREATED_AT).toLocalDateTime());
                url.setId(resultSet.getLong("id"));
                result.add(url);
            }
            return result;
        }
    }

    public static void clear() {
        String sql = "DELETE FROM urls;";
        try (Connection conn = BaseRepository.getDataSource().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            //    throw new RuntimeException("Failed to clear the Url database", e)
            log.info("Failed to clear the Url database", e);  // Compliant, output via logger
        }
    }
}
