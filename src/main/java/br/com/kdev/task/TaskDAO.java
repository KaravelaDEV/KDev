package br.com.kdev.task;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaskDAO {
    private Connection conn;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public void createDatabase()throws SQLException{
        String url = "";
        try {
            Properties props = new Properties();
            InputStream input = TaskDAO.class.getClassLoader().getResourceAsStream("db.properties");

            props.load(input);

            String driver = props.getProperty("driver");
            url = props.getProperty("url");

            Class.forName(driver);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        try {
            String TABLE_TASK_DEFINITION = "CREATE TABLE TASK(ID integer PRIMARY KEY, TITLE text, DESCRIPTION text, STATUS integer, DATE text)";
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(TABLE_TASK_DEFINITION);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void save(Task task) throws SQLException{
        try {
            String sqlInsert = "INSERT INTO TASK(TITLE, DESCRIPTION, STATUS, DATE) VALUES(?, ?, ?, ?)";
            PreparedStatement pstmt =
                    conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getStatus());
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
            String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASK WHERE ID = ?";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, ID);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                task = new Task();
                task.setId(rs.getInt("ID"));
                task.setTitle(rs.getString("TITLE"));
                task.setDescription(rs.getString("DESCRIPTION"));
                task.setStatus(rs.getInt("STATUS"));
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
            String sqlUpdate = "UPDATE TASK SET TITLE = ?, DESCRIPTION = ?, STATUS = ?, DATE = ? WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getStatus());
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

    public List<Task> filter(String query, int status) throws SQLException, ParseException {
        List<Task> list = new ArrayList<>();

        try {
            String sqlQuery;
            ResultSet rs = null;
            PreparedStatement pstmt = null;

            if(query.length() > 0 && status >= 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASK WHERE (instr(TITLE, ?) > 0 OR instr(DESCRIPTION, ?) > 0) AND STATUS = ?";

                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(1, query);
                pstmt.setString(2, query);
                pstmt.setInt(3, status);
                rs  = pstmt.executeQuery();
            }

            if(query.length() > 0 && status < 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASK WHERE instr(TITLE, ?) > 0 OR instr(DESCRIPTION, ?) > 0";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(1, query);
                pstmt.setString(2, query);
                rs  = pstmt.executeQuery();
            }

            if(query.length() == 0 && status >= 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASK WHERE STATUS = ?";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setInt(1, status);
                rs  = pstmt.executeQuery();
            }

            if(query.length() == 0 && status < 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASK";
                Statement stmt = conn.createStatement();
                rs  = stmt.executeQuery(sqlQuery);
            }

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("ID"));
                task.setTitle(rs.getString("TITLE"));
                task.setDescription(rs.getString("DESCRIPTION"));
                task.setStatus(rs.getInt("STATUS"));
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
