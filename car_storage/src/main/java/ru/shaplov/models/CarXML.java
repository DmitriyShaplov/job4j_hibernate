package ru.shaplov.models;

/**
 * @author shaplov
 * @since 08.07.2019
 */
public class CarXML implements IEntity {
    private int id;

    private String name;

    private CarBodyXML carBody;

    private EngineXML engine;

    private TransmissionXML transmission;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CarBodyXML getCarBody() {
        return carBody;
    }

    public EngineXML getEngine() {
        return engine;
    }

    public TransmissionXML getTransmission() {
        return transmission;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCarBody(CarBodyXML carBody) {
        this.carBody = carBody;
    }

    public void setEngine(EngineXML engine) {
        this.engine = engine;
    }

    public void setTransmission(TransmissionXML transmission) {
        this.transmission = transmission;
    }
}
