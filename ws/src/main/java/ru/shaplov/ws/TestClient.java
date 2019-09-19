package ru.shaplov.ws;

/**
 * @author shaplov
 * @since 19.09.2019
 */
public class TestClient {
    public static void main(String[] args) {
        ItemWebServiceService itemService = new ItemWebServiceService();
        ItemWebService itemWeb = itemService.getItemWebServicePort();
        itemWeb.getItems().getItem().forEach(item -> System.out.println(item.getTitle()
                + " " + item.getCreated() + " " + item.getUser().getLogin()));
        UserWebServiceService userWebServiceService = new UserWebServiceService();
        UserWebService userWebService = userWebServiceService.getUserWebServicePort();
        System.out.println(userWebService.findByLogin("admin").getTel());
    }
}
