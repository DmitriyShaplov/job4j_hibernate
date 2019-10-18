package ru.shaplov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.shaplov.logic.ILogicItem;
import ru.shaplov.models.Item;
import ru.shaplov.models.Msg;

/**
 * @author shaplov
 * @since 17.10.2019
 */
@Controller
public class ChatController {

    private final ILogicItem logicItem;

    @Autowired
    public ChatController(ILogicItem logicItem) {
        this.logicItem = logicItem;
    }

    @GetMapping("/chat/{id}")
    public String chatController(@PathVariable int id, Model model) {
        Item item = new Item(id);
        item = logicItem.get(item);
        model.addAttribute("item", item);
        return "chat";
    }

//    @SubscribeMapping("/chat/{id}/save")
//    public Msg sa1ve(@DestinationVariable int id, Msg msg, Authentication auth) {
//        msg.setName(auth.getName());
//        return msg;
//    }

    @MessageMapping("/chat/{id}/save")
    @SendTo("/chat/{id}/save")
    public Msg save(@DestinationVariable int id, Msg msg, Authentication auth) {
        msg.setName(auth.getName());
        return msg;
    }
}
