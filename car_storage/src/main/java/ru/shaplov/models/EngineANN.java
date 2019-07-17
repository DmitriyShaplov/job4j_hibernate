package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author shaplov
 * @since 08.07.2019
 */
@Entity
@Table(name = "engine")
public class EngineANN implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        EngineANN engineANN = (EngineANN) o;
        return id == engineANN.id && Objects.equals(name, engineANN.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
