package br.com.kdev.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskConverter {
    private ObjectMapper mapper;

    public TaskConverter(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.mapper = new ObjectMapper();
        mapper.setDateFormat(formatter);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    Task getTaskObject(String taskJSON) throws IOException {
        return mapper.readValue(taskJSON, Task.class);
    }

    String getTaskJSON(Task task) throws JsonProcessingException {
        return mapper.writeValueAsString(task);
    }

    String getTaskListJSON(List<Task> list) throws JsonProcessingException {
        return mapper.writeValueAsString(list);
    }
}
