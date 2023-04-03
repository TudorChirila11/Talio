package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminLogInCtrlTest {

    private AdminLogInCtrl adminLogInCtrl;
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;

    @BeforeEach
    public void setUp() {
        serverUtils = new ServerUtils();
        mainCtrl = new MainCtrl();
        boardOverviewCtrl = new BoardOverviewCtrl(serverUtils, mainCtrl, adminLogInCtrl);
        adminLogInCtrl = new AdminLogInCtrl(serverUtils, mainCtrl, boardOverviewCtrl);
    }

    @Test
    public void testGetAdmin() {
        assertFalse(adminLogInCtrl.getAdmin());
    }

    @Test
    public void testGetServer() {
        assertEquals(serverUtils, adminLogInCtrl.getServer());
    }
}
