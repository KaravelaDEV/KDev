package br.com.kdev.task;

import br.com.kdev.util.StandardResponse;
import br.com.kdev.util.StatusResponse;

import spark.Request;
import spark.Response;

import java.sql.SQLException;
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

        } catch (SQLException e) {
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "{}");
            response.status(HTTP_BAD_REQUEST);
        }

        response.type("application/json");
        return sr.getResponse();
    }

    public Object fetchAllTasks(Request request, Response response){
        StandardResponse sr;

        try {
            List<Task> list = taskDAO.list();
            String data = taskConverter.getTaskListJSON(list);

            sr = new StandardResponse(StatusResponse.SUCCESS, "Successfully Listed", data);
            response.status(HTTP_OK_REQUEST);

        } catch (ParseException | SQLException e){
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "[]");
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

        } catch (ParseException | SQLException e){
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

        } catch (SQLException e) {
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

        } catch (SQLException e){
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage(), "{}");
            response.status(HTTP_BAD_REQUEST);
        }

        response.type("application/json");
        return sr.getResponse();
    }
}
