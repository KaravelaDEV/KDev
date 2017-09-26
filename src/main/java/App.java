import br.com.kdev.task.TaskController;
import br.com.kdev.task.TaskConverter;
import br.com.kdev.task.TaskDAO;

import java.sql.SQLException;

import static spark.Spark.*;

class App {
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    private TaskDAO configDataBase(){
        TaskDAO taskDAO = null;
        try {
            taskDAO = new TaskDAO();
            taskDAO.createDatabase();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return taskDAO;
    }

    private void configRouters(TaskController taskController){
        port(3000);

        get("/tasks", taskController::fetchAllTasks);

        get("/tasks/:id", taskController::fetchTaskByID);

        post("/tasks", taskController::createTask);

        put("/tasks/:id", taskController::updateTask);

        delete("/tasks/:id", taskController::deleteTask);
    }

    void start(){
        TaskConverter taskConverter = new TaskConverter();
        TaskDAO taskDAO = configDataBase();
        TaskController taskController = new TaskController(taskConverter, taskDAO);
        configRouters(taskController);

        String origin = "*";
        String methods = "HEAD, GET, POST, DELETE, PUT";
        String headers = "origin, content-type, accept";
        enableCORS(origin, methods, headers);
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
}