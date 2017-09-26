package br.com.kdev.task;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaskConverterTest {
    @Test
    public void getTaskObject() throws Exception {
        String data = "{\"title\":\"Create Google Cloud course\",\"description\":\"New Google cloud course for programmers.\",\"date\":\"2017-09-26T18:50:33.633Z\",\"completed\":false,\"id\":1}";

        TaskConverter taskConverter = new TaskConverter();
        Task task = taskConverter.getTaskObject(data);

        assertNotNull(task);
        assertEquals(task.getTitle(), "Create Google Cloud course");
        assertEquals(task.getDescription(), "New Google cloud course for programmers.");
        assertEquals(task.isCompleted(), false);
        assertNotNull(task.getDate());
    }
}