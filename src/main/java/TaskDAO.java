import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TaskDAO {
    private Task task;

    public void createDatabase(){
        String url = "jdbc:sqlite::memory:";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(Task task){
        this.createDatabase();

        this.task = task;
        this.task.setID(2000);
    }

    public Task fetch(int ID) {
        Task task = new Task("Create Course");
        task.setID(ID);
        return task;
    }
}
