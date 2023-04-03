package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.stomp.StompSession;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;

public class TagInOverviewCtrlTest {

    @Mock
    private StompSession session;

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private Board currentBoard;

    private TagInOverviewCtrl tagInOverviewCtrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tagInOverviewCtrl = new TagInOverviewCtrl();
    }

    @Test
    public void testDeleteTag() {
        Tag tag = new Tag();
        tagInOverviewCtrl.subscriber(session, server, tag, mainCtrl, currentBoard);
        tagInOverviewCtrl.deleteTag();
        verify(server).send("/app/tagsDelete", tag, session);
    }

    @Test
    public void testEditTag() {
        tagInOverviewCtrl.subscriber(session, server, new Tag(), mainCtrl, currentBoard);
        tagInOverviewCtrl.editTag();
        verify(mainCtrl).showTagCreation(currentBoard, new Tag());
    }
}