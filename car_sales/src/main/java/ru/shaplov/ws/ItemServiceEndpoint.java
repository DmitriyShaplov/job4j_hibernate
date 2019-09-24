package ru.shaplov.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.shaplov.logic.ILogicItem;
import ru.shaplov.models.Item;

/**
 * @author shaplov
 * @since 23.09.2019
 */
@Endpoint
public class ItemServiceEndpoint {

    private final ILogicItem logicItem;

    @Autowired
    public ItemServiceEndpoint(ILogicItem logicItem) {
        this.logicItem = logicItem;
    }

    @PayloadRoot(
            namespace = "http://ws.shaplov.ru/",
            localPart = "getItemRequest"
    )
    @ResponsePayload
    public GetItemResponse getItem(@RequestPayload GetItemRequest request) {
        Item item = new Item();
        item.setId(request.getId());
        GetItemResponse response = new GetItemResponse();
        response.setItem(logicItem.get(item));
        return response;
    }

    @PayloadRoot(
            namespace = "http://ws.shaplov.ru/",
            localPart = "getItemsRequest"
    )
    @ResponsePayload
    public GetItemsResponse getItems() {
        GetItemsResponse response = new GetItemsResponse();
        response.setItem(logicItem.getItems());
        return response;
    }
}
