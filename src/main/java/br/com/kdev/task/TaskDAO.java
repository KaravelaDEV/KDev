package br.com.kdev.task;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private Connection conn;

    public void createDatabase()throws SQLException{
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {

            String url = "jdbc:sqlite::memory:";
            String TABLE_TASK_DEFINITION = "CREATE TABLE TASK(ID integer PRIMARY KEY,  NAME text NOT NULL)";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(TABLE_TASK_DEFINITION);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void save(Task task) throws SQLException{
        try {
            String sqlInsert = "INSERT INTO TASK(name) VALUES(?)";
            PreparedStatement pstmt =
                    conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getTitle());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating task failed, no ID obtained.");
            }
            task.setId(generatedKeys.getInt(1));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public Task fetchByID(int ID) throws SQLException{
        Task task = null;
        try {
            String sqlQuery = "SELECT ID, NAME FROM TASK WHERE ID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, ID);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                task = new Task();
                task.setTitle(rs.getString("NAME"));
                task.setId(rs.getInt("ID"));
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return task;
    }

    public void update(Task task) throws SQLException {
        try {
            String sqlUpdate = "UPDATE TASK SET NAME = ? WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, task.getTitle());
            pstmt.setInt(2, task.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void remove(Task task) throws SQLException{
        try {
            String sqlDelete = "DELETE FROM TASK WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sqlDelete);
            pstmt.setInt(1, task.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public List<Task> list() throws SQLException {
        List<Task> list = new ArrayList<>();

        try {
            String sqlQuery = "SELECT ID, NAME FROM TASK";

            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                Task task = new Task();
                task.setTitle(rs.getString("NAME"));
                task.setId(rs.getInt("ID"));
                list.add(task);
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return list;
    }
}
