package br.com.kdev.task;

import br.com.kdev.util.DAOUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.sql.SQLException;

public class SQLiteTaskDAOTest extends TaskDAOTest{
    private static DAOUtil daoUtil;

    @BeforeClass
    public static void setupClass(){
        daoUtil = new DAOUtil();
    }

    @Before
    public void setup() throws SQLException, IOException, ClassNotFoundException {
        TaskDAOFactory taskDAOFactory = new TaskDAOFactory(daoUtil);
        taskDAO = taskDAOFactory.createSQLiteTaskDAO();
    }

    @After
    public void tearDown() throws SQLException {
        daoUtil.close();
    }
}
