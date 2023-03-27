package server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    /**
     * @param message the message that is being transmitted
     * @return the message that was received
     * @throws InterruptedException an exception which happens when the process is interrupted
     */
    @MessageMapping("/receive")
    @SendTo("/api/broadcast")
    public String broadcast(String message) {
        return message;
    }

}