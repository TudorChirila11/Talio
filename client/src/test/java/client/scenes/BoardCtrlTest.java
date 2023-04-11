package client.scenes;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import client.services.TagOverviewService;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.stomp.StompSession;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;

class BoardCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private StompSession session;

    @Mock
    private Board currentBoard;

    @InjectMocks
    private BoardCtrl controller;;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCard() {
        controller.addCard();
    }

    @Test
    void passwordCheck() {
        controller.passwordCheck();
    }

    @Test
    void subscriber() {
        controller.subscriber(session);
    }

    @Test
    void boardOverview() {
        controller.boardOverview();
    }

    @Test
    void writeNewPassword() {
        controller.writeNewPasswordToFile();
    }

    @Test
    void setAdmin() {
        controller.setAdmin(true);
    }

    @Test
    void showColorManagement() {
        controller.showColorManagement();
    }

    @Test
    void getBoardPane() {
        controller.getBoardPane();
    }
}