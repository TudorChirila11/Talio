package client.scenes;

import static com.google.inject.Guice.createInjector;

import client.MyFXML;
import client.MyModule;
import com.google.inject.Injector;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import commons.Board;

class MainCtrlTest {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @Mock
    private Stage primaryStage;

    @Mock
    private javafx.util.Pair<BoardCtrl, Parent> board;

    @Mock
    private Pair<CardInformationCtrl, Parent> cardInformation;

    @Mock
    private Pair<WelcomePageCtrl, Parent> welcomePage;

    @Mock
    private Pair<KeyboardShortcutFCtrl, Parent> keyboardShortcutPage;

    @Mock
    private Pair<TagCreatorCtrl, Parent> tagCreator;

    @Mock
    private Pair<TagOverviewCtrl, Parent> tagOverview;

    @Mock
    private Pair<BoardOverviewCtrl, Parent> boardOverview;

    @Mock
    private Pair<AdminLogInCtrl, Parent> adminLogIn;

    @Mock
    private Pair<ColorManagementCtrl, Parent> colorManagement;

    @Mock
    private Board currentBoard;

    @InjectMocks
    private MainCtrl controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBoard() {
        controller.getBoard();
    }

    @Test
    void getCardInformation() {
        controller.getCardInformation();
    }

    @Test
    void getKeyboardShortcut() {
        controller.getKeyboardShortcut();
    }

    @Test
    void getWelcomePage() {
        controller.getWelcomePage();
    }

    @Test
    void getPrimaryStage() {
        controller.getPrimaryStage();
    }
}