import org.junit.Test;

import static org.junit.Assert.*;
import java.util.List;

public class TaskDAOTest {
    @Test
    public void createTask() throws Exception {
        TaskDAO taskDAO= new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setName("Create Course");
        taskDAO.save(task);

        assertEquals(1, task.getID());
        assertEquals("Create Course", task.getName());
    }

    @Test
    public void fetchTaskByID() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setName("Create Course");
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

        Task task = new Task();
        task.setName("Create Course");
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

        Task task = new Task();
        task.setName("Create Course");
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getID());

        taskDAO.remove(task);
        task = taskDAO.fetchByID(1);
        assertNull(task);
    }

    @Test
    public void listTasks() throws Exception {
        TaskDAO taskDAO= new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setName("Create Course A");
        taskDAO.save(task);

        task = new Task();
        task.setName("Create Course B");
        taskDAO.save(task);

        task = new Task();
        task.setName("Create Course C");
        taskDAO.save(task);

        List<Task> list = taskDAO.list();
        assertEquals(3, list.size());
    }
}
