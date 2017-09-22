import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {
    public TaskTest(){

    }

    @Test
    public void getName() throws Exception {
        Task task = new Task("GCP Course");
        assertEquals("GCP Course", task.getName());
    }

}