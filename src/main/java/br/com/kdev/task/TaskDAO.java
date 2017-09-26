package br.com.kdev.task;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDAO {
    private Connection conn;
    private SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

    public void createDatabase()throws SQLException{
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {

            String url = "jdbc:sqlite::memory:";
            String TABLE_TASK_DEFINITION = "CREATE TABLE TASK(ID integer PRIMARY KEY, TITLE text, DESCRIPTION text, COMPLETED integer, DATE text)";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(TABLE_TASK_DEFINITION);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void save(Task task) throws SQLException{
        try {
            String sqlInsert = "INSERT INTO TASK(TITLE, DESCRIPTION, COMPLETED, DATE) VALUES(?, ?, ?, ?)";
            PreparedStatement pstmt =
                    conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.isCompleted() ? 1 : 0);
            pstmt.setString(4, task.getDate() == null ? null : formatter.format(task.getDate()));

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

    public Task fetchByID(int ID) throws SQLException, ParseException {
        Task task = null;
        try {
            String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, COMPLETED, DATE FROM TASK WHERE ID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, ID);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                task = new Task();
                task.setId(rs.getInt("ID"));
                task.setTitle(rs.getString("TITLE"));
                task.setDescription(rs.getString("DESCRIPTION"));
                task.setCompleted(rs.getInt("COMPLETED") == 1);
                String date = rs.getString("DATE");
                if(date != null)
                    task.setDate(formatter.parse(rs.getString("DATE")));
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return task;
    }

    public void update(Task task) throws SQLException {
        try {
            String sqlUpdate = "UPDATE TASK SET TITLE = ?, DESCRIPTION = ?, COMPLETED = ?, DATE = ? WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.isCompleted() ? 1 : 0);
            pstmt.setString(4, task.getDate() == null ? null : formatter.format(task.getDate()));
            pstmt.setInt(5, task.getId());
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

    public List<Task> list() throws SQLException, ParseException {
        List<Task> list = new ArrayList<>();

        try {
            String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, COMPLETED, DATE FROM TASK";

            Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("ID"));
                task.setTitle(rs.getString("TITLE"));
                task.setDescription(rs.getString("DESCRIPTION"));
                task.setCompleted(rs.getInt("COMPLETED") == 1);
                String date = rs.getString("DATE");
                if(date != null)
                    task.setDate(formatter.parse(rs.getString("DATE")));
                list.add(task);
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return list;
    }
}
