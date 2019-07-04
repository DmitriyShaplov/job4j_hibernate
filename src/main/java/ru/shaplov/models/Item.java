package ru.shaplov.models;

import java.util.Calendar;

/**
 * Model for item table.
 *
 * @author shaplov
 * @since 05.07.2019
 */
public class Item {

    private int id;
    private String description;
    private Calendar created;
    private boolean done;

    public Item() {
    }

    public Item(int id, String description, Calendar created, boolean done) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCreated() {
        return created;
    }

    public boolean isDone() {
        return done;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
