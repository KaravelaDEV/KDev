package br.com.kdev.task;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public abstract class TaskDAOTest {
    ITaskDAO taskDAO = null;

    @Test
    public void createTask() throws Exception {
        Task task = new Task();
        task.setTitle("Create Course");
        task.setDescription("GCP");
        task.setStatus(0);
        task.setDate(new Date());
        taskDAO.save(task);

        assertEquals(1, task.getId());
        assertEquals("Create Course", task.getTitle());
    }

    @Test
    public void fetchTaskByID() throws Exception {
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

        List<Task> list = taskDAO.filter("", -1);
        assertEquals(3, list.size());
    }

    @Test
    public void filter() throws Exception {
        Task task = new Task();
        task.setTitle("Create Course AAA");
        task.setDescription("Course of some letters A");
        task.setStatus(0);
        taskDAO.save(task);

        task = new Task();
        task.setTitle("Create Course A");
        task.setDescription("Course of some letters AAA");
        task.setStatus(1);
        taskDAO.save(task);

        task = new Task();
        task.setTitle("Create Course BBB");
        task.setDescription("Course of some letters B");
        task.setStatus(0);
        taskDAO.save(task);

        task = new Task();
        task.setTitle("Create Course B");
        task.setDescription("Course of some letters BBB");
        task.setStatus(1);
        taskDAO.save(task);

        List<Task> filteredTasks = taskDAO.filter("", 0);

        assertNotNull(filteredTasks);
        assertEquals(2, filteredTasks.size());
        assertEquals(0, filteredTasks.get(0).getStatus());
        assertEquals(0, filteredTasks.get(1).getStatus());

        filteredTasks = taskDAO.filter("AAA", -1);

        assertNotNull(filteredTasks);
        assertEquals(2, filteredTasks.size());

        filteredTasks = taskDAO.filter("BBB", 1);

        assertNotNull(filteredTasks);
        assertEquals(1, filteredTasks.size());
        assertEquals("Create Course B", filteredTasks.get(0).getTitle());
        assertEquals(1, filteredTasks.get(0).getStatus());
    }
}
