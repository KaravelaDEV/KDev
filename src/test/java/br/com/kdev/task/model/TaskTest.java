package br.com.kdev.task.model;

import br.com.kdev.task.model.Task;
import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {
    public TaskTest(){

    }

    @Test
    public void getName() throws Exception {
        Task task = new Task();
        task.setTitle("GCP Course");
        assertEquals("GCP Course", task.getTitle());
    }
}