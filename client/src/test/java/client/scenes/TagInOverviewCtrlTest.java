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

public class TagInOverviewCtrlTest extends FXTest {

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

    @Test
    public void testSetTagText() {
        String text = "Test Text";
        tagInOverviewCtrl.tagText = new Label();
        tagInOverviewCtrl.setTagText(text);
        assertEquals(text, tagInOverviewCtrl.tagText.getText());
    }

    @Test
    public void testSetColor() {
        tagInOverviewCtrl.tagText = new Label();
        tagInOverviewCtrl.mainTagBody = new HBox();
        List<Double> colorValues = Arrays.asList(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
        tagInOverviewCtrl.setColor(colorValues);
        String expectedBackgroundStyle = "-fx-background-color: #808080ff; -fx-padding: 10 20 10 20; -fx-background-radius: 25";
        assertEquals(expectedBackgroundStyle, tagInOverviewCtrl.mainTagBody.getStyle());
        assertEquals("0x808080ff", tagInOverviewCtrl.tagText.getTextFill().toString());
    }
}