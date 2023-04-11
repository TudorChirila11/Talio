package client.scenes;

import static org.mockito.Mockito.*;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.ResourceBundle;

class TagCreatorCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private StompSession session;

    @Mock
    private Board currentBoard;

    @InjectMocks
    private TagCreatorCtrl controller;;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void subscriber() {
        controller.subscriber(session);
    }

    @Test
    void initialize() {
        controller.initialize((URL) null, (ResourceBundle) null);
    }

    @Test
    void testInitialize() {
        controller.initialize(new Board(), new Tag());
    }

    @Test
    void goBack() {
        controller.goBack();
    }
}