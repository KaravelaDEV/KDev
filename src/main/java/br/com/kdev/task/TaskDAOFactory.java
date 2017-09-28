package br.com.kdev.task;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TaskDAOFactory {

    private Connection conn;

    public ITaskDAO getDataBase() throws Exception {
        ITaskDAO taskDAO = null;

        Properties props = new Properties();
        InputStream input = TaskDAOFactory.class.getClassLoader().getResourceAsStream("db.properties");
        props.load(input);

        String serverName = props.getProperty("servername");

        if(serverName.toLowerCase().equals("sqlite")) {
            taskDAO = createSQLiteTaskDAO(
                    props.getProperty("driver"),
                    props.getProperty("url"));
        }

        if(serverName.toLowerCase().equals("mysql")){
            taskDAO = createMySQLTaskDAO(
                    props.getProperty("driver"),
                    props.getProperty("url"),
                    props.getProperty("username"),
                    props.getProperty("password"));
        }

        return taskDAO;
    }

    public SQLiteTaskDAO createSQLiteTaskDAO(String driver, String url) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        conn = DriverManager.getConnection(url);
        SQLiteTaskDAO sqliteTaskDAO = new SQLiteTaskDAO(conn);
        sqliteTaskDAO.createDatabase();
        return sqliteTaskDAO;
    }

    public MySQLTaskDAO createMySQLTaskDAO(String driver, String url, String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        return new MySQLTaskDAO(conn);
    }
}
