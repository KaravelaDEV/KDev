package br.com.kdev.task.dao;

import br.com.kdev.task.model.Task;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements ITaskDAO {
    private Connection conn;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public TaskDAO(Connection conn){
        this.conn = conn;
    }

    public void createDatabase() throws SQLException {
        String TABLE_TASK_DEFINITION = "CREATE TABLE TASKS(ID integer PRIMARY KEY, TITLE text, DESCRIPTION text, STATUS integer, DATE text)";

        Statement stmt = conn.createStatement();
        stmt.execute(TABLE_TASK_DEFINITION);
    }

    @Override
    public void save(Task task) throws SQLException{
        try {
            String sqlInsert = "INSERT INTO TASKS(TITLE, DESCRIPTION, STATUS, DATE) VALUES(?, ?, ?, ?)";
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

    @Override
    public Task fetchByID(int ID) throws SQLException, ParseException {
        Task task = null;
        try {
            String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASKS WHERE ID = ?";

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

    @Override
    public void update(Task task) throws SQLException {
        try {
            String sqlUpdate = "UPDATE TASKS SET TITLE = ?, DESCRIPTION = ?, STATUS = ?, DATE = ? WHERE ID = ?";
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

    @Override
    public void remove(Task task) throws SQLException{
        try {
            String sqlDelete = "DELETE FROM TASKS WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sqlDelete);
            pstmt.setInt(1, task.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public List<Task> filter(String query, int status) throws SQLException, ParseException {
        List<Task> list = new ArrayList<>();

        try {
            String sqlQuery;
            ResultSet rs = null;
            PreparedStatement pstmt = null;

            if(query.length() > 0 && status >= 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASKS WHERE (instr(TITLE, ?) > 0 OR instr(DESCRIPTION, ?) > 0) AND STATUS = ?";

                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(1, query);
                pstmt.setString(2, query);
                pstmt.setInt(3, status);
                rs  = pstmt.executeQuery();
            }

            if(query.length() > 0 && status < 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASKS WHERE instr(TITLE, ?) > 0 OR instr(DESCRIPTION, ?) > 0";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setString(1, query);
                pstmt.setString(2, query);
                rs  = pstmt.executeQuery();
            }

            if(query.length() == 0 && status >= 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASKS WHERE STATUS = ?";
                pstmt = conn.prepareStatement(sqlQuery);
                pstmt.setInt(1, status);
                rs  = pstmt.executeQuery();
            }

            if(query.length() == 0 && status < 0){
                sqlQuery = "SELECT ID, TITLE, DESCRIPTION, STATUS, DATE FROM TASKS";
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
