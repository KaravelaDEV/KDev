public class Task {
    private String name;
    private int ID;

    public Task(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }
}
