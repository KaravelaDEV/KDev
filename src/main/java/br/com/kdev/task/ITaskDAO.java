package br.com.kdev.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface ITaskDAO {

    void save(Task task) throws SQLException;

    Task fetchByID(int ID) throws SQLException, ParseException;

    void update(Task task) throws SQLException;

    void remove(Task task) throws SQLException;

    List<Task> filter(String query, int status) throws SQLException, ParseException;
}
