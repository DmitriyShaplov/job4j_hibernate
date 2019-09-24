package ru.shaplov.ws;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@XmlRootElement(name = "getItemRequest")
public class GetItemRequest {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
