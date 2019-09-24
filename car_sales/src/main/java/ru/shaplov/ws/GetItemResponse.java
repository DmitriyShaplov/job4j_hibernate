package ru.shaplov.ws;

import ru.shaplov.models.Item;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@XmlRootElement(name = "getItemResponse")
public class GetItemResponse {

    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
