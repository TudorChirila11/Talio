package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminLogInCtrlTest {

    private AdminLogInCtrl adminLogInCtrl;
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;


    @BeforeEach
    public void setUp() {
        serverUtils = new ServerUtils();
        mainCtrl = new MainCtrl();
        adminLogInCtrl = new AdminLogInCtrl(serverUtils, mainCtrl);
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
