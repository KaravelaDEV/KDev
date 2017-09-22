import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.StringWriter;

class App {
    private static final int HTTP_BAD_REQUEST = 400;
    private TaskDAO taskDAO = null;

    void start() {
        try {
            this.taskDAO = new TaskDAO();
            this.taskDAO.createDatabase();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        port(3000);

        get("/tasks", this::handleGetTasks);

        post("/tasks", this::handlePostTask);
    }

    private int handlePostTask(Request request, Response response){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Task task = mapper.readValue(request.body(), Task.class);

            taskDAO.save(task);

            response.status(200);
            response.type("application/json");
            return task.getID();

        } catch (IOException | SQLException ios) {
            response.status(HTTP_BAD_REQUEST);
            return 0;
        }
    }

    private String handleGetTasks(Request request, Response response){
        try {
            List<Task> list = taskDAO.list();
            StringWriter sw = new StringWriter();

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(sw, list);

            response.status(200);
            response.type("application/json");
            return sw.toString();

        } catch (IOException | SQLException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}