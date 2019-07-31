package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author shaplov
 * @since 13.07.2019
 */
@Entity
@Table(name = "unifying")
public class Unifying implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @Override
    public int getId() {
        return id;
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
        Unifying unifying = (Unifying) o;
        return id == unifying.id
                && Objects.equals(model, unifying.model)
                && Objects.equals(body, unifying.body)
                && Objects.equals(engine, unifying.engine)
                && Objects.equals(drive, unifying.drive)
                && Objects.equals(trans, unifying.trans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, body, engine, drive, trans);
    }
}
