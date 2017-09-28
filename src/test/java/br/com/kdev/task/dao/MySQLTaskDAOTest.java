package br.com.kdev.task.dao;

import br.com.kdev.util.DAOUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.sql.SQLException;

public class MySQLTaskDAOTest extends TaskDAOTest {
    private static DAOUtil daoUtil;

    @BeforeClass
    public static  void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        daoUtil = new DAOUtil();
        TaskDAOFactory taskDAOFactory = new TaskDAOFactory(daoUtil);
        taskDAO = taskDAOFactory.createMySQLTaskDAO();
    }

    @Before
    public void setup() throws SQLException {
        daoUtil.clean();
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        daoUtil.close();
    }
}
