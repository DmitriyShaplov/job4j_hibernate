package ru.shaplov.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.shaplov.logic.ILogicUser;
import ru.shaplov.models.CarUser;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author shaplov
 * @since 19.09.2019
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class UserWebService extends SpringBeanAutowiringSupport {

    @Autowired
    private ILogicUser logicUser;

    @WebMethod(exclude = true)
    public void setLogicUser(ILogicUser logicUser) {
        this.logicUser = logicUser;
    }

    @WebMethod
    public CarUser save(CarUser user) {
        return logicUser.save(user);
    }

    @WebMethod
    public CarUser authUser(String login, String password) {
        return logicUser.authUser(login, password);
    }

    @WebMethod
    public CarUser findByLogin(String login) {
        return logicUser.findByLogin(login);
    }
}
