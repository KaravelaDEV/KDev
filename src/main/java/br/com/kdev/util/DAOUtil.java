package br.com.kdev.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DAOUtil {
    private Connection conn = null;
    private Properties props = null;
    private InputStream input = DAOUtil.class.getClassLoader().getResourceAsStream("db.properties");

    public String getServerName() throws IOException {
        if(props == null) {
            props = new Properties();
            props.load(input);
        }

        return props.getProperty("servername");
    }

    public Connection connectToMysql() throws ClassNotFoundException, SQLException, IOException {
        if(props == null) {
            props = new Properties();
            props.load(input);
        }

        if(conn == null){
            String driver = props.getProperty("mysql-driver");
            Class.forName(driver);

            String url = props.getProperty("mysql-url");
            String username = props.getProperty("mysql-username");
            String password = props.getProperty("mysql-password");
            conn = DriverManager.getConnection(url, username, password);
        }

        return conn;
    }

    public Connection connectToSQLite() throws SQLException, ClassNotFoundException, IOException {
        if(props == null) {
            props = new Properties();
            props.load(input);
        }

        if(conn == null) {
            String driver = props.getProperty("sqlite-driver");
            Class.forName(driver);

            String url = props.getProperty("sqlite-url");
            conn = DriverManager.getConnection(url);
        }

        return conn;
    }

    public void clean() throws SQLException {
        String sqlDelete = "DELETE FROM TASKS";
        PreparedStatement pstmt = conn.prepareStatement(sqlDelete);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void close() throws SQLException {
        conn.close();
        conn = null;
    }
}
