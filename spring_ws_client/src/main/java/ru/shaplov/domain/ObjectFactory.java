//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.24 at 03:40:25 AM MSK 
//


package ru.shaplov.domain;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the client.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName GET_ITEMS_REQUEST_QNAME = new QName("http://ws.shaplov.ru/", "getItemsRequest");
    private final static QName ITEM_QNAME = new QName("http://ws.shaplov.ru/", "item");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: client.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetItemRequest }
     * 
     */
    public GetItemRequest createGetItemRequest() {
        return new GetItemRequest();
    }

    /**
     * Create an instance of {@link GetItemResponse }
     * 
     */
    public GetItemResponse createGetItemResponse() {
        return new GetItemResponse();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link GetItemsRequest }
     *
     */
    public GetItemsRequest createGetItemsRequest() {
        return new GetItemsRequest();
    }

    /**
     * Create an instance of {@link GetItemsResponse }
     * 
     */
    public GetItemsResponse createGetItemsResponse() {
        return new GetItemsResponse();
    }

    /**
     * Create an instance of {@link CarUser }
     * 
     */
    public CarUser createCarUser() {
        return new CarUser();
    }

    /**
     * Create an instance of {@link Brand }
     * 
     */
    public Brand createBrand() {
        return new Brand();
    }

    /**
     * Create an instance of {@link Model }
     * 
     */
    public Model createModel() {
        return new Model();
    }

    /**
     * Create an instance of {@link BodyType }
     * 
     */
    public BodyType createBodyType() {
        return new BodyType();
    }

    /**
     * Create an instance of {@link EngineType }
     * 
     */
    public EngineType createEngineType() {
        return new EngineType();
    }

    /**
     * Create an instance of {@link DriveType }
     * 
     */
    public DriveType createDriveType() {
        return new DriveType();
    }

    /**
     * Create an instance of {@link TransType }
     * 
     */
    public TransType createTransType() {
        return new TransType();
    }

    /**
     * Create an instance of {@link ItemArray }
     * 
     */
    public ItemArray createItemArray() {
        return new ItemArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.shaplov.ru/", name = "getItemsRequest")
    public JAXBElement<Object> createGetItemsRequest(Object value) {
        return new JAXBElement<Object>(GET_ITEMS_REQUEST_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.shaplov.ru/", name = "item")
    public JAXBElement<Item> createItem(Item value) {
        return new JAXBElement<Item>(ITEM_QNAME, Item.class, null, value);
    }

}
