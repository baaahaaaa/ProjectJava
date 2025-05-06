package ranim.projetpidev.services;

import ranim.projetpidev.entites.Ressource;
import ranim.projetpidev.tools.MyDataBase;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class RessourceService implements IService<Ressource> {
    private Connection connection;

    public RessourceService() {
        connection = MyDataBase.getInstance().getCnx();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS ressource (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "description TEXT," +
                "format VARCHAR(50) NOT NULL," +
                "creation_date DATE NOT NULL," +
                "file_path VARCHAR(255) NOT NULL," +
                "courses_id INT NOT NULL," +
                "FOREIGN KEY (courses_id) REFERENCES course(id) ON DELETE CASCADE" +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating ressource table: " + e.getMessage());
        }
    }

    @Override
    public void ajouter(Ressource ressource) {
        String query = "INSERT INTO ressource (title, description, format, creation_date, file_path, courses_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ressource.getTitle());
            preparedStatement.setString(2, ressource.getDescription());
            preparedStatement.setString(3, ressource.getFormat());
            preparedStatement.setDate(4, ressource.getCreationDate());
            preparedStatement.setString(5, ressource.getFilePath());
            preparedStatement.setInt(6, ressource.getCoursesId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ressource.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Ressource ressource) {
        String query = "UPDATE ressource SET title = ?, description = ?, format = ?, creation_date = ?, file_path = ?, courses_id = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ressource.getTitle());
            preparedStatement.setString(2, ressource.getDescription());
            preparedStatement.setString(3, ressource.getFormat());
            preparedStatement.setDate(4, ressource.getCreationDate());
            preparedStatement.setString(5, ressource.getFilePath());
            preparedStatement.setInt(6, ressource.getCoursesId());
            preparedStatement.setInt(7, ressource.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Ressource ressource) {
        String query = "DELETE FROM ressource WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ressource.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ressource> rechercher() {
        List<Ressource> ressources = new ArrayList<>();
        String query = "SELECT * FROM ressource";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Ressource ressource = new Ressource();
                ressource.setId(resultSet.getInt("id"));
                ressource.setTitle(resultSet.getString("title"));
                ressource.setDescription(resultSet.getString("description"));
                ressource.setFormat(resultSet.getString("format"));
                ressource.setCreationDate(resultSet.getDate("creation_date"));
                ressource.setFilePath(resultSet.getString("file_path"));
                ressource.setCoursesId(resultSet.getInt("courses_id"));
                ressources.add(ressource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ressources;
    }

    public List<Ressource> getRessourcesByCourseId(int courseId) {
        List<Ressource> ressources = new ArrayList<>();
        String query = "SELECT * FROM ressource WHERE courses_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ressource ressource = new Ressource();
                ressource.setId(resultSet.getInt("id"));
                ressource.setTitle(resultSet.getString("title"));
                ressource.setDescription(resultSet.getString("description"));
                ressource.setFormat(resultSet.getString("format"));
                ressource.setCreationDate(resultSet.getDate("creation_date"));
                ressource.setFilePath(resultSet.getString("file_path"));
                ressource.setCoursesId(resultSet.getInt("courses_id"));
                ressources.add(ressource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ressources;
    }
}
