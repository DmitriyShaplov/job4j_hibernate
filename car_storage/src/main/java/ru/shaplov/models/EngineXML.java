package ru.shaplov.models;

/**
 * @author shaplov
 * @since 08.07.2019
 */
public class EngineXML implements IEntity {
    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
