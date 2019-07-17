package ru.shaplov.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngineXML engineXML = (EngineXML) o;
        return id == engineXML.id && Objects.equals(name, engineXML.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
