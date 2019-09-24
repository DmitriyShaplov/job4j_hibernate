package ru.shaplov.ws;

import ru.shaplov.models.Item;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author shaplov
 * @since 24.09.2019
 */
@XmlRootElement(name = "getItemsResponse")
public class GetItemsResponse {

    private List<Item> item;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}
