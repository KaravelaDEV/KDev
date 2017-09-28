package br.com.kdev.task;

import br.com.kdev.util.StandardResponse;
import br.com.kdev.util.StatusResponse;

import br.com.kdev.util.ValidateParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class TaskController {
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_OK_REQUEST = 200;

    private TaskConverter taskConverter;
    private TaskDAO taskDAO;

    public TaskController(TaskConverter taskConverter, TaskDAO taskDAO){
        this.taskConverter = taskConverter;
        this.taskDAO = taskDAO;
    }

    public Object createTask(Request request, Response response){
        StandardResponse sr;

        try {
            String data = request.body();

            Task task = taskConverter.getTaskObject(data);
            taskDAO.save(task);
            data = taskConverter.getTaskJSON(task);

            sr = new StandardResponse(StatusResponse.SUCCESS, "Successfully Created", data);
            response.status(HTTP_OK_REQUEST);

        } catch (IOException | SQLException e) {
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "{}");
            response.status(HTTP_BAD_REQUEST);
        }

        response.type("application/json");
        return sr.getResponse();
    }

    public Object fetchTaskByID(Request request, Response response){
        StandardResponse sr;

        try {
            int ID = Integer.parseInt(request.params(":id"));

            Task task = this.taskDAO.fetchByID(ID);
            String data = taskConverter.getTaskJSON(task);

            sr = new StandardResponse(StatusResponse.SUCCESS, "Successfully Fetched", data);
            response.status(HTTP_OK_REQUEST);

        } catch(JsonProcessingException | ParseException | SQLException e){
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "{}");
            response.status(HTTP_BAD_REQUEST);
        }

        response.type("application/json");
        return sr.getResponse();
    }

    public Object updateTask(Request request, Response response){
        StandardResponse sr;

        try {
            String data = request.body();

            Task task = taskConverter.getTaskObject(data);
            taskDAO.update(task);
            data = taskConverter.getTaskJSON(task);

            sr = new StandardResponse(StatusResponse.SUCCESS, "Successfully Updated", data);
            response.status(HTTP_OK_REQUEST);

        } catch (IOException| SQLException e) {
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "[]");
            response.status(HTTP_BAD_REQUEST);
        }

        response.type("application/json");
        return sr.getResponse();
    }

    public Object deleteTask(Request request, Response response){
        StandardResponse sr;

        try {
            String data = request.body();

            Task task = taskConverter.getTaskObject(data);
            this.taskDAO.remove(task);

            sr = new StandardResponse(StatusResponse.SUCCESS, "Successfully Deleted", "{}");
            response.status(HTTP_OK_REQUEST);

        } catch(IOException | SQLException e){
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "{}");
            response.status(HTTP_BAD_REQUEST);
        }

        response.type("application/json");
        return sr.getResponse();
    }

    public Object filterTasks(Request request, Response response){
        response.type("application/json");
        StandardResponse sr;

        String query = null;
        int status = -1;

        ValidateParams validate = new ValidateParams(request);
        if(validate.hasParams()){

            if (validate.hasParamQuery()) {
                query = validate.getQuery();
            }

            if (validate.hasParamStatus()){
                if(!validate.isStatusValid()) {
                    sr = new StandardResponse(StatusResponse.ERROR, "Param status not valid", "{}");
                    response.status(HTTP_BAD_REQUEST);
                    return sr.getResponse();
                }
                status = validate.getParamStatus();
            }
        }

        try {
            List<Task> list = this.taskDAO.filter(query, status);
            String data = taskConverter.getTaskListJSON(list);

            sr = new StandardResponse(StatusResponse.SUCCESS, "Successfully Filtered", data);
            response.status(HTTP_OK_REQUEST);

        } catch(JsonProcessingException | ParseException | SQLException e){
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "{}");
            response.status(HTTP_BAD_REQUEST);
        }

        return sr.getResponse();
    }
}
