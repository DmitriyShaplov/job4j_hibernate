package ru.shaplov.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "brand")
@JsonIgnoreProperties(value = { "models" })
public class Brand implements ITitledEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @OneToMany(mappedBy = "brand", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models = new ArrayList<>();

    public Brand() {
    }

    public Brand(int id) {
        this.id = id;
    }

    public void addModel(Model model) {
        models.add(model);
        model.setBrand(this);
    }

    public void removeModel(Model model) {
        models.remove(model);
        model.setBrand(null);
    }

    public List<Model> getModels() {
        return models;
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

    public void setModels(List<Model> models) {
        this.models = models;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Brand brand = (Brand) o;
        return id == brand.id && Objects.equals(title, brand.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
