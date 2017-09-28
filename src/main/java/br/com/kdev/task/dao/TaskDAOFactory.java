package br.com.kdev.task.dao;

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

    public TaskDAO createSQLiteTaskDAO() throws SQLException, ClassNotFoundException, IOException {
        TaskDAO taskDAO = new TaskDAO(daoUtil.connectToSQLite());
        taskDAO.createDatabase();

        return taskDAO;
    }

    public TaskDAO createMySQLTaskDAO() throws SQLException, ClassNotFoundException, IOException {
        return new TaskDAO(daoUtil.connectToMysql());
    }
}
