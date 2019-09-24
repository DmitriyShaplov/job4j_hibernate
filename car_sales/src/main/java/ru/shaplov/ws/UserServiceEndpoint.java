package ru.shaplov.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.shaplov.logic.ILogicUser;
import ru.shaplov.models.CarUser;

/**
 * @author shaplov
 * @since 23.09.2019
 */
@Endpoint
public class UserServiceEndpoint {

    @Autowired
    private ILogicUser logicUser;

    @PayloadRoot(
            namespace = "http://ws.shaplov.ru/",
            localPart = "getUserRequest"
    )
    @ResponsePayload
    public GetUserResponse findByLogin(@RequestPayload GetUserRequest request) {
        GetUserResponse response = new GetUserResponse();
        response.setCarUser(logicUser.findByLogin(request.getLogin()));
        return response;
    }
}
