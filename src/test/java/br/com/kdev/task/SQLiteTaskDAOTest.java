package br.com.kdev.task;

import org.junit.Before;

public class SQLiteTaskDAOTest extends TaskDAOTest{

    @Before
    public void setup() throws Exception {
        TaskDAOFactory taskDAOFactory = new TaskDAOFactory();
        taskDAO = taskDAOFactory.createSQLiteTaskDAO("org.sqlite.JDBC", "jdbc:sqlite::memory:");
    }
}
