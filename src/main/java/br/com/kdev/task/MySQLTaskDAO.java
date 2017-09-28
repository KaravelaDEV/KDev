package br.com.kdev.task;

import java.sql.*;
import java.text.ParseException;
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
            PreparedStatement pstmt =
                    conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getStatus());
            //pstmt.setDate(4, task.getDate());

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
        return null;
    }

    @Override
    public void update(Task task) throws SQLException {

    }

    @Override
    public void remove(Task task) throws SQLException {

    }

    @Override
    public List<Task> filter(String query, int status) throws SQLException, ParseException {
        return null;
    }
}
