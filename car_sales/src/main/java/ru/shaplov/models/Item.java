package ru.shaplov.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "items")
public class Item implements ITitledEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String picture;

    private boolean sold;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "car_users_id")
    private CarUser user;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "body_type_id")
    private BodyType body;

    @ManyToOne
    @JoinColumn(name = "engine_type_id")
    private EngineType engine;

    @ManyToOne
    @JoinColumn(name = "drive_type_id")
    private DriveType drive;

    @ManyToOne
    @JoinColumn(name = "trans_type_id")
    private TransType trans;

    public Item() {
    }

    public Item(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public boolean isSold() {
        return sold;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public CarUser getUser() {
        return user;
    }

    public Brand getBrand() {
        return brand;
    }

    public Model getModel() {
        return model;
    }

    public BodyType getBody() {
        return body;
    }

    public EngineType getEngine() {
        return engine;
    }

    public DriveType getDrive() {
        return drive;
    }

    public TransType getTrans() {
        return trans;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUser(CarUser user) {
        this.user = user;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setBody(BodyType body) {
        this.body = body;
    }

    public void setEngine(EngineType engine) {
        this.engine = engine;
    }

    public void setDrive(DriveType drive) {
        this.drive = drive;
    }

    public void setTrans(TransType trans) {
        this.trans = trans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Item item = (Item) o;
        LocalDateTime createdTrunc = null;
        LocalDateTime createdTruncObj = null;
        if (created != null && item.created != null) {
            createdTrunc = created.truncatedTo(ChronoUnit.MILLIS);
            createdTruncObj = item.created.truncatedTo(ChronoUnit.MILLIS);
        }
        return id == item.id && sold == item.sold
                && Objects.equals(title, item.title)
                && Objects.equals(createdTrunc, createdTruncObj)
                && Objects.equals(user, item.user)
                && Objects.equals(brand, item.brand)
                && Objects.equals(model, item.model)
                && Objects.equals(body, item.body)
                && Objects.equals(engine, item.engine)
                && Objects.equals(drive, item.drive)
                && Objects.equals(trans, item.trans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, sold, created, user, brand, model, body, engine, drive, trans);
    }
}
