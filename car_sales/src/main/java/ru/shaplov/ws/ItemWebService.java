package ru.shaplov.ws;

import org.springframework.beans.factory.annotation.Autowired;
import ru.shaplov.logic.ILogicItem;
import ru.shaplov.models.Item;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author shaplov
 * @since 19.09.2019
 */
//@WebService
//@SOAPBinding(style = SOAPBinding.Style.RPC)
public class ItemWebService {

    @Autowired
    private ILogicItem logicItem;

    @WebMethod(exclude = true)
    public void setLogicItem(ILogicItem logicItem) {
        this.logicItem = logicItem;
    }

    @WebMethod
    public Item save(Item entity) {
        return logicItem.save(entity, null);
    }

    @WebMethod
    public Item update(Item entity) {
        return logicItem.update(entity);
    }

    @WebMethod
    public void delete(Item entity) {
        logicItem.delete(entity);
    }

    @WebMethod
    public Item get(Item entity) {
        return logicItem.get(entity);
    }

    @WebMethod
    public Item[] getItems() {
        return logicItem.getItems().toArray(new Item[0]);
    }
}
