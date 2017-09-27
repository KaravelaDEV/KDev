package br.com.kdev.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

public class TaskConverter {
    private ObjectMapper mapper;

    public TaskConverter(){
        this.mapper = new ObjectMapper();
    }

    Task getTaskObject(String taskJSON) throws IOException {
        return mapper.readValue(taskJSON, Task.class);
    }

    String getTaskJSON(Task task) throws JsonProcessingException {
        return mapper.writeValueAsString(task);
    }

    String getTaskListJSON(List<Task> list) throws JsonProcessingException {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(list);
    }
}
