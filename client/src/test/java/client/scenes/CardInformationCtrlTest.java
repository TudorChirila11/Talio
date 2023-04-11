package client.scenes;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import client.services.TagOverviewService;
import commons.Card;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.stomp.StompSession;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;

class CardInformationCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private StompSession session;

    @Mock
    private Board currentBoard;

    @InjectMocks
    private CardInformationCtrl controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void setState() {
        controller.setState(CardInformationCtrl.State.CREATE);
    }

    @Test
    void registerForUpdates() {
        controller.registerForUpdates();
    }

    @Test
    void stop() {
        controller.stop();
    }

    @Test
    void subscriber() {
        controller.subscriber(session);
    }

    @Test
    void setCard() {
        controller.setCard(new Card());
    }

    @Test
    void getCardById() {
        controller.getCardById(123L);
    }

    @Test
    void getBoard() {
        controller.getBoard();
    }

    @Test
    void setBoard() {
        controller.setBoard(new Board());
    }

    @Test
    void setTag() {
        controller.setTag();
    }

    @Test
    void deleteSubtasksOfCard() {
        controller.deleteSubtasksOfCard(123L);
    }
}