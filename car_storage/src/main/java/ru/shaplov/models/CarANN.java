package ru.shaplov.models;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author shaplov
 * @since 08.07.2019
 */
@Entity
@Table(name = "car")
public class CarANN implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_body_id")
    private CarBodyANN carBody;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private EngineANN engine;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transmission_id")
    private TransmissionANN transmission;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CarBodyANN getCarBody() {
        return carBody;
    }

    public EngineANN getEngine() {
        return engine;
    }

    public TransmissionANN getTransmission() {
        return transmission;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCarBody(CarBodyANN carBody) {
        this.carBody = carBody;
    }

    public void setEngine(EngineANN engine) {
        this.engine = engine;
    }

    public void setTransmission(TransmissionANN transmission) {
        this.transmission = transmission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CarANN carANN = (CarANN) o;
        return id == carANN.id
                && Objects.equals(name, carANN.name)
                && Objects.equals(carBody, carANN.carBody)
                && Objects.equals(engine, carANN.engine)
                && Objects.equals(transmission, carANN.transmission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, carBody, engine, transmission);
    }
}
