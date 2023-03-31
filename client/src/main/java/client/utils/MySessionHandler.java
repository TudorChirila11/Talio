package client.utils;

import client.scenes.BoardCtrl;
import client.scenes.BoardOverviewCtrl;
import client.scenes.TagOverviewCtrl;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.CountDownLatch;

public class MySessionHandler extends StompSessionHandlerAdapter {

    private BoardCtrl boardCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;

    private TagOverviewCtrl tagOverviewCtrl;

    private CountDownLatch latch;

    public MySessionHandler(BoardCtrl boardCtrl, BoardOverviewCtrl boardOverviewCtrl, TagOverviewCtrl tagOverviewCtrl,
                            CountDownLatch latch) {
        this.boardCtrl = boardCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.latch = latch;
    }

    @Override
    public void afterConnected(StompSession session,
                               StompHeaders connectedHeaders) {
        System.out.println("Counting down!");
        latch.countDown();
        System.out.println("Stopped counting down!");
        System.out.println("Doing this method!");
        boardCtrl.subscriber();
        System.out.println("Doing this method!");
        boardOverviewCtrl.subscriber();
        System.out.println("Doing this method!");
        tagOverviewCtrl.subscriber();
        System.out.println("Doing this method!");
    }
}
