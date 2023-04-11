package client.scenes;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import client.services.TagOverviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.stomp.StompSession;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;

class ColorManagementCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private StompSession session;

    @Mock
    private Board currentBoard;

    @InjectMocks
    private ColorManagementCtrl controller;

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
        controller.initialize(null, null);
    }
}