import org.junit.Test;

import static org.junit.Assert.*;

public class TaskDAOTest {
    @Test
    public void createTask() throws Exception {
        TaskDAO taskDAO= new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task("Create Course");
        taskDAO.save(task);

        assertEquals(1, task.getID());
        assertEquals("Create Course", task.getName());
    }

    @Test
    public void fetchTaskByID() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task("Create Course");
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);

        assertNotNull(task);
        assertEquals(1, task.getID());
        assertEquals("Create Course", task.getName());
    }

    @Test
    public void updateTask() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task("Create Course");
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getID());
        assertEquals("Create Course", task.getName());

        task.setName("Course Updated");
        taskDAO.update(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getID());
        assertEquals("Course Updated", task.getName());
    }

    @Test
    public void removeTask() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task("Create Course");
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getID());

        taskDAO.remove(task);
        task = taskDAO.fetchByID(1);
        assertNull(task);
    }
}
