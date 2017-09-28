package br.com.kdev.task.dao;

import br.com.kdev.task.model.Task;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public abstract class TaskDAOTest {
    static ITaskDAO taskDAO = null;

    @Test
    public void createTask() throws Exception {
        Task task = new Task();
        task.setTitle("Create Course");
        task.setDescription("GCP");
        task.setStatus(0);
        task.setDate(new Date());

        assertEquals(0, task.getId());

        taskDAO.save(task);

        assertTrue(task.getId() > 0);
        assertEquals("Create Course", task.getTitle());
    }

    @Test
    public void fetchTaskByID() throws Exception {
        Task task = new Task();
        task.setTitle("Create Course");
        taskDAO.save(task);

        assertTrue(task.getId() > 0);
        Task taskRecovered = taskDAO.fetchByID(task.getId());

        assertNotNull(taskRecovered);
        assertEquals(taskRecovered.getId(), task.getId());
        assertEquals("Create Course", taskRecovered.getTitle());
        assertNull(taskRecovered.getDate());

        task = new Task();
        task.setTitle("Create Course");
        task.setDate(new Date());
        taskDAO.save(task);

        assertTrue(task.getId() > 0);

        taskRecovered = taskDAO.fetchByID(task.getId());

        assertNotNull(taskRecovered);
        assertEquals(taskRecovered.getId(), task.getId());
        assertEquals("Create Course", taskRecovered.getTitle());
        assertNotNull(taskRecovered.getDate());
    }

    @Test
    public void updateTask() throws Exception {
        Task taskCourseA = new Task();
        taskCourseA.setTitle("Create Course A");
        taskCourseA.setDate(new Date());
        taskDAO.save(taskCourseA);

        Task taskCourseB = new Task();
        taskCourseB.setTitle("Create Course B");
        taskCourseB.setDate(new Date());
        taskDAO.save(taskCourseB);

        assertTrue(taskCourseA.getId() > 0);

        Task taskRecovered = taskDAO.fetchByID(taskCourseA.getId());
        assertEquals(taskRecovered.getId(), taskCourseA.getId());
        assertEquals("Create Course A", taskRecovered.getTitle());
        assertNotNull(taskRecovered.getDate());

        taskRecovered = taskDAO.fetchByID(taskCourseB.getId());
        assertEquals(taskRecovered.getId(), taskCourseB.getId());
        assertEquals("Create Course B", taskRecovered.getTitle());
        assertNotNull(taskRecovered.getDate());

        Task taskUpdated = taskDAO.fetchByID(taskCourseA.getId());
        taskUpdated.setTitle("Course A Updated");
        taskDAO.update(taskUpdated);

        taskRecovered = taskDAO.fetchByID(taskCourseA.getId());
        assertEquals(taskRecovered.getId(), taskCourseA.getId());
        assertEquals("Course A Updated", taskRecovered.getTitle());

        taskRecovered = taskDAO.fetchByID(taskCourseB.getId());
        assertEquals(taskRecovered.getId(), taskCourseB.getId());
        assertEquals("Create Course B", taskRecovered.getTitle());
    }

    @Test
    public void removeTask() throws Exception {
        Task task = new Task();
        task.setTitle("Create Course");
        taskDAO.save(task);

        Task taskRecovered = taskDAO.fetchByID(task.getId());
        assertEquals(taskRecovered.getId(), task.getId());

        taskDAO.remove(taskRecovered);
        taskRecovered = taskDAO.fetchByID(task.getId());
        assertNull(taskRecovered);
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
