package br.com.kdev.task;

import br.com.kdev.util.DAOUtil;

import java.io.IOException;
import java.sql.SQLException;

public class TaskDAOFactory {
    private DAOUtil daoUtil = null;

    public TaskDAOFactory(){
        this.daoUtil = new DAOUtil();
    }

    public TaskDAOFactory(DAOUtil daoUtil){
        this.daoUtil = daoUtil;
    }

    public ITaskDAO getDataBase() throws Exception {
        ITaskDAO taskDAO = null;

        String serverName = daoUtil.getServerName();

        if(serverName.toLowerCase().equals("sqlite")) {
            taskDAO = createSQLiteTaskDAO();
        }

        if(serverName.toLowerCase().equals("mysql")){
            taskDAO = createMySQLTaskDAO();
        }

        return taskDAO;
    }

    public SQLiteTaskDAO createSQLiteTaskDAO() throws SQLException, ClassNotFoundException, IOException {
        SQLiteTaskDAO sqliteTaskDAO = new SQLiteTaskDAO(daoUtil.connectToSQLite());
        sqliteTaskDAO.createDatabase();

        return sqliteTaskDAO;
    }

    public MySQLTaskDAO createMySQLTaskDAO() throws SQLException, ClassNotFoundException, IOException {
        return new MySQLTaskDAO(daoUtil.connectToMysql());
    }
}
