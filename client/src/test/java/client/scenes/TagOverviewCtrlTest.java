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
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class TagOverviewCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private StompSession session;

    @Mock
    private Board currentBoard;

    @InjectMocks
    private TagOverviewCtrl controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRefresh() {
        // Create a board and some tags
        Board board = new Board();

        new TagOverviewService(server).getTags(board.getId());

        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setName("tag2");
        tags.add(tag1);
        tags.add(tag2);

        // Mock the server to return the tags
        when(server.getTags(board.getId())).thenReturn(tags);

        // Initialize the controller with the mock objects
        controller = new TagOverviewCtrl(server, mainCtrl, new TagOverviewService(server));
        controller.initialize(currentBoard);
        controller.initialize(board);
        controller.subscriber(session);
    }

    @Test
    public void testScene() {
        controller.goBack();
        controller.createTag();
    }
}
