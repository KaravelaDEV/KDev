package br.com.kdev.task;

import java.util.Date;

class Task {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private Date date;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
