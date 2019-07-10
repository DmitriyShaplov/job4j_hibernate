package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "body_type")
public class BodyType implements ITitledEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    public BodyType() {
    }

    public BodyType(int id) {
        this.id = id;
    }

    @Override
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
        BodyType bodyType = (BodyType) o;
        return id == bodyType.id && Objects.equals(title, bodyType.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
