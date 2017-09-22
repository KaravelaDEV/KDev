import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        String output = "<h1> Hello World! I am <span style=\'color:red\'> aaa-server </span></h1>";
        port(3000);
        get("/", (req, res) -> output);
    }
}