package br.com.kdev.task;

import org.junit.Before;

public class MySQLTaskDAOTest extends TaskDAOTest {
    @Before
    public void setUp() throws Exception {
        TaskDAOFactory taskDAOFactory = new TaskDAOFactory();
        taskDAO = taskDAOFactory.createMySQLTaskDAO(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/ktasks",
                "root",
                "Kdev_2017");
    }
}
