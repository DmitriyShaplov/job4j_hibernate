package ru.shaplov.ws;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@XmlRootElement(name = "getUserRequest")
public class GetUserRequest {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
