package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "engine_type")
public class EngineType implements ITitledEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    public EngineType() {
    }

    public EngineType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngineType that = (EngineType) o;
        return id == that.id && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
