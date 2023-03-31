package client.utils;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.CountDownLatch;

public class MySessionHandler extends StompSessionHandlerAdapter {

    private CountDownLatch latch;

    /**
     * The constructor that is used by the connect method
     * @param latch the latch that will make the javafx thread wait before the session gets connected
     */
    public MySessionHandler(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * the methods that lets the session get fully created before anything is done on the javafx thread
     * @param session          the client STOMP session
     * @param connectedHeaders the STOMP CONNECTED frame headers
     */
    @Override
    public void afterConnected(StompSession session,
                               StompHeaders connectedHeaders) {
        latch.countDown();
    }
}
