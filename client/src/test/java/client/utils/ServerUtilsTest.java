package client.utils;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.stomp.StompSession;

class ServerUtilsTest {

    @Mock
    private StompSession session;

    @InjectMocks
    private ServerUtils controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void changeIP() {
        controller.changeIP("test");
    }

    @Test
    void getServer() {
        controller.getServer();
    }

    @Test
    void registerForUpdates() {
        controller.registerForUpdates(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
            }
        });
    }

    @Test
    void getCardInformationCtrl() {
        controller.getCardInformationCtrl();
    }

    @Test
    void stop() {
        controller.stop();
    }

    @Test
    void registerForCollections() {
        controller.registerForCollections(null, null, null, session);
    }

    @Test
    void send() {
        controller.send(null, null, session);
    }

    @Test
    void getControllers() {
        controller.getControllers(null, null, null, null, null, null, null);
    }
}