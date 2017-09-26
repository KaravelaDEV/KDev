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

    Task getTaskObject(String taskJSON){
        Task task = null;

        try {
            task = mapper.readValue(taskJSON, Task.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return task;
    }

    String getTaskJSON(Task task){
        String data = "";

        try {
            data = mapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return data;
    }

    String getTaskListJSON(List<Task> list){
        String data = "";

        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            data = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return data;
    }
}
