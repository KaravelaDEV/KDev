package br.com.kdev.task;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MySQLTaskDAO implements ITaskDAO {
    private Connection conn;

    MySQLTaskDAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public void save(Task task) throws SQLException {
        try {
            String sqlInsert = "INSERT INTO TASKS(TITLE, DESCRIPTION, STATUS, DATE) VALUES(?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getStatus());
            if(task.getDate() != null)
                pstmt.setTimestamp(4, new java.sql.Timestamp(task.getDate().getTime()));
            else
                pstmt.setTimestamp(4,null);

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
                task.setDate(rs.getTimestamp("DATE"));
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

            if(task.getDate() != null)
                pstmt.setTimestamp(4, new java.sql.Timestamp(task.getDate().getTime()));
            else
                pstmt.setTimestamp(4,null);

            pstmt.setInt(5, task.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void remove(Task task) throws SQLException {
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
                task.setDate(rs.getTimestamp("DATE"));
                list.add(task);
            }

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        return list;
    }
}
