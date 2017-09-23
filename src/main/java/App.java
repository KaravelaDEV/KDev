import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;

import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import java.io.StringWriter;

import static spark.Spark.*;

class App {
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_OK_REQUEST = 200;
    private TaskDAO taskDAO = null;

    void configDataBase(){
        try {
            this.taskDAO = new TaskDAO();
            this.taskDAO.createDatabase();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void configRouters(){
        port(3000);

        get("/tasks", this::handleGetTasks);

        get("/tasks/:id", this::handleGetTask);

        post("/tasks", this::handlePostTask);

        put("/tasks/:id", this::handlePutTask);

        delete("/tasks/:id", this::handleDeleteTask);
    }

    private Object handlePostTask(Request request, Response response){
        ObjectMapper mapper = new ObjectMapper();
        StandardResponse sr;

        try {
            Task task = mapper.readValue(request.body(), Task.class);

            taskDAO.save(task);

            response.status(HTTP_OK_REQUEST);
            sr = new StandardResponse(StatusResponse.SUCCESS, task);

        } catch (IOException | SQLException e) {
            response.status(HTTP_BAD_REQUEST);
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage());
        }

        String sras;

        try{
            response.type("application/json");
            sras = mapper.writeValueAsString(sr);

        } catch (JsonProcessingException jpe) {
            sras = jpe.getMessage();
            System.out.print(sras);
        }

        return sras;
    }

    private Object handleGetTasks(Request request, Response response){
        StandardResponse sr;

        try {
            List<Task> list = taskDAO.list();

            response.status(HTTP_OK_REQUEST);
            sr = new StandardResponse(StatusResponse.SUCCESS, list);

        } catch (SQLException e){
            response.status(HTTP_BAD_REQUEST);
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage());
        }

        String sras;

        try{
            response.type("application/json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            sras = mapper.writeValueAsString(sr);

        } catch (JsonProcessingException jpe) {
            sras = jpe.getMessage();
            System.out.print(sras);
        }

        return sras;
    }

    private Object handleGetTask(Request request, Response response){
        StandardResponse sr;

        try {
            int ID = Integer.parseInt(request.params(":id"));
            Task task = this.taskDAO.fetchByID(ID);

            response.status(HTTP_OK_REQUEST);
            sr = new StandardResponse(StatusResponse.SUCCESS, task);

        } catch (SQLException e){
            response.status(HTTP_BAD_REQUEST);
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage());
        }

        String sras;

        try{
            response.type("application/json");

            ObjectMapper mapper = new ObjectMapper();
            sras = mapper.writeValueAsString(sr);

        } catch (JsonProcessingException jpe) {
            sras = jpe.getMessage();
            System.out.print(sras);
        }

        return sras;
    }

    private Object handlePutTask(Request request, Response response){
        ObjectMapper mapper = new ObjectMapper();
        StandardResponse sr;

        try {
            Task task = mapper.readValue(request.body(), Task.class);

            taskDAO.update(task);

            response.status(HTTP_OK_REQUEST);
            sr = new StandardResponse(StatusResponse.SUCCESS, task);

        } catch (IOException | SQLException e) {
            response.status(HTTP_BAD_REQUEST);
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage());
        }

        String sras;

        try{
            response.type("application/json");
            sras = mapper.writeValueAsString(sr);

        } catch (JsonProcessingException jpe) {
            sras = jpe.getMessage();
            System.out.print(sras);
        }

        return sras;
    }

    private Object handleDeleteTask(Request request, Response response){
        ObjectMapper mapper = new ObjectMapper();
        StandardResponse sr;

        try {
            Task task = mapper.readValue(request.body(), Task.class);

            this.taskDAO.remove(task);

            response.status(HTTP_OK_REQUEST);
            sr = new StandardResponse(StatusResponse.SUCCESS, "Task Successfully deleted.");

        } catch (IOException | SQLException e) {
            response.status(HTTP_BAD_REQUEST);
            sr = new StandardResponse(StatusResponse.ERROR, e.getMessage());
        }

        String sras;

        try{
            response.type("application/json");
            sras = mapper.writeValueAsString(sr);

        } catch (JsonProcessingException jpe) {
            sras = jpe.getMessage();
            System.out.print(sras);
        }

        return sras;
    }
}