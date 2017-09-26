package br.com.kdev.task;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

public class TaskDAOTest {
    @Test
    public void createTask() throws Exception {
        TaskDAO taskDAO= new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setTitle("Create Course");
        task.setDescription("GCP");
        task.setCompleted(false);
        task.setDate(new Date());
        taskDAO.save(task);

        assertEquals(1, task.getId());
        assertEquals("Create Course", task.getTitle());
    }

    @Test
    public void fetchTaskByID() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setTitle("Create Course");
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);

        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals("Create Course", task.getTitle());
        assertNull(task.getDate());

        task = new Task();
        task.setTitle("Create Course");
        task.setDate(new Date());
        taskDAO.save(task);

        task = taskDAO.fetchByID(2);

        assertNotNull(task);
        assertEquals(2, task.getId());
        assertEquals("Create Course", task.getTitle());
        assertNotNull(task.getDate());
    }

    @Test
    public void updateTask() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setTitle("Create Course A");
        task.setDate(new Date());
        taskDAO.save(task);

        task = new Task();
        task.setTitle("Create Course B");
        task.setDate(new Date());
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getId());
        assertEquals("Create Course A", task.getTitle());
        assertNotNull(task.getDate());

        task = taskDAO.fetchByID(2);
        assertEquals(2, task.getId());
        assertEquals("Create Course B", task.getTitle());
        assertNotNull(task.getDate());

        task = taskDAO.fetchByID(1);
        task.setTitle("Course A Updated");
        taskDAO.update(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getId());
        assertEquals("Course A Updated", task.getTitle());

        task = taskDAO.fetchByID(2);
        assertEquals(2, task.getId());
        assertEquals("Create Course B", task.getTitle());
    }

    @Test
    public void removeTask() throws Exception {
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setTitle("Create Course");
        taskDAO.save(task);

        task = taskDAO.fetchByID(1);
        assertEquals(1, task.getId());

        taskDAO.remove(task);
        task = taskDAO.fetchByID(1);
        assertNull(task);
    }

    @Test
    public void listTasks() throws Exception {
        TaskDAO taskDAO= new TaskDAO();
        taskDAO.createDatabase();

        Task task = new Task();
        task.setTitle("Create Course A");
        task.setDate(new Date());
        taskDAO.save(task);

        task = new Task();
        task.setTitle("Create Course B");
        taskDAO.save(task);

        task = new Task();
        task.setTitle("Create Course C");
        taskDAO.save(task);

        List<Task> list = taskDAO.list();
        assertEquals(3, list.size());
    }
}
