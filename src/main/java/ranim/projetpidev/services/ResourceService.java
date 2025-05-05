package ranim.projetpidev.services;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.entites.Resource;
import ranim.projetpidev.tools.MyDataBase;
import java.util.List;
import java.util.ArrayList;

import java.sql.*;

public class ResourceService implements IService<Resource> {
    private Connection connection;

    public ResourceService() {
        connection = MyDataBase.getInstance().getCnx();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS resource (" +
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
            System.err.println("Error creating resource table: " + e.getMessage());
        }
    }

    @Override
    public void ajouter(Resource resource) {
        String query = "INSERT INTO resource (title, description, format, creation_date, file_path, courses_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, resource.getTitle());
            preparedStatement.setString(2, resource.getDescription());
            preparedStatement.setString(3, resource.getFormat());
            preparedStatement.setDate(4, resource.getCreationDate());
            preparedStatement.setString(5, resource.getFilePath());
            preparedStatement.setInt(6, resource.getCoursesId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                resource.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Resource resource) {
        String query = "UPDATE resource SET title = ?, description = ?, format = ?, creation_date = ?, file_path = ?, courses_id = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, resource.getTitle());
            preparedStatement.setString(2, resource.getDescription());
            preparedStatement.setString(3, resource.getFormat());
            preparedStatement.setDate(4, resource.getCreationDate());
            preparedStatement.setString(5, resource.getFilePath());
            preparedStatement.setInt(6, resource.getCoursesId());
            preparedStatement.setInt(7, resource.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Resource resource) {
        String query = "DELETE FROM resource WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, resource.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Resource> rechercher() {
        List<Resource> resources = new ArrayList<>();
        String query = "SELECT * FROM resource";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Resource resource = new Resource();
                resource.setId(resultSet.getInt("id"));
                resource.setTitle(resultSet.getString("title"));
                resource.setDescription(resultSet.getString("description"));
                resource.setFormat(resultSet.getString("format"));
                resource.setCreationDate(resultSet.getDate("creation_date"));
                resource.setFilePath(resultSet.getString("file_path"));
                resource.setCoursesId(resultSet.getInt("courses_id"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }
}