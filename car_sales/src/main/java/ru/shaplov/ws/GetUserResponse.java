package ru.shaplov.ws;

import ru.shaplov.models.CarUser;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@XmlRootElement(name = "getUserResponse")
public class GetUserResponse {

    private CarUser carUser;

    public CarUser getCarUser() {
        return carUser;
    }

    public void setCarUser(CarUser carUser) {
        this.carUser = carUser;
    }
}
