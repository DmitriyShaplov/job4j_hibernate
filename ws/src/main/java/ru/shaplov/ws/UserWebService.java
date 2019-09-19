
package ru.shaplov.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "UserWebService", targetNamespace = "http://ws.shaplov.ru/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UserWebService {


    /**
     * 
     * @param arg0
     * @return
     *     returns ru.shaplov.ws.CarUser
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://ws.shaplov.ru/UserWebService/saveRequest", output = "http://ws.shaplov.ru/UserWebService/saveResponse")
    public CarUser save(
        @WebParam(name = "arg0", partName = "arg0")
        CarUser arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns ru.shaplov.ws.CarUser
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://ws.shaplov.ru/UserWebService/findByLoginRequest", output = "http://ws.shaplov.ru/UserWebService/findByLoginResponse")
    public CarUser findByLogin(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns ru.shaplov.ws.CarUser
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://ws.shaplov.ru/UserWebService/authUserRequest", output = "http://ws.shaplov.ru/UserWebService/authUserResponse")
    public CarUser authUser(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1);

}
