import org.junit.Test;

import static org.junit.Assert.*;

public class TaskDAOTest {
    @Test
    public void createTask() throws  Exception {
        Task task = new Task("Create Course");
        TaskDAO taskDAO= new TaskDAO();
        taskDAO.save(task);

        assertEquals(2000, task.getID());
        assertEquals("Create Course", task.getName());
    }

    @Test
    public void fetchTask() throws  Exception {
        TaskDAO taskDAO = new TaskDAO();
        Task task = taskDAO.fetch(2000);

        assertEquals(2000, task.getID());
        assertEquals("Create Course", task.getName());
    }
}