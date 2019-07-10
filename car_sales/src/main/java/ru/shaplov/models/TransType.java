package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "trans_type")
public class TransType implements ITitledEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    public TransType() {
    }

    public TransType(int id) {
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
        TransType transType = (TransType) o;
        return id == transType.id && Objects.equals(title, transType.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
