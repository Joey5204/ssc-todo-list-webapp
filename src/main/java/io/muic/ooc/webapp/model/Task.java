package io.muic.ooc.webapp.model;

public class Task {
    private long id;
    private String name;
    private String description;
    private boolean status;

    public Task(long id, String name, String description, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // method to edit task details
    public void editTask(String newName, String newDescription, boolean newStatus) {
        this.name = newName;
        this.description = newDescription;
        this.status = newStatus;
    }

    // method to delete task
    public void deleteTask() {
        // Code to delete the task
    }
}
