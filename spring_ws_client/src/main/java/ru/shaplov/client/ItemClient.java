package ru.shaplov.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.shaplov.domain.GetItemRequest;
import ru.shaplov.domain.GetItemResponse;
import ru.shaplov.domain.GetItemsRequest;
import ru.shaplov.domain.GetItemsResponse;

/**
 * @author shaplov
 * @since 24.09.2019
 */
public class ItemClient extends WebServiceGatewaySupport {

    public GetItemResponse getItem(int id) {
        GetItemRequest request = new GetItemRequest();
        request.setId(id);
        GetItemResponse response = (GetItemResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        "http://localhost:8080/car_sales/ws/itemService", request,
                        new SoapActionCallback("http://ws.shaplov.ru/GetItemRequest")
                );
        return response;
    }

    public GetItemsResponse getItems() {
        GetItemsRequest request = new GetItemsRequest();
        GetItemsResponse response = (GetItemsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        "http://localhost:8080/car_sales/ws/itemService", request,
                        new SoapActionCallback("http://ws.shaplov.ru/GetItemsRequest")
                );
        return response;
    };
}
