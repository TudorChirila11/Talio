package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class BoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    /**
     * Board Ctrl constructor
     * @param server serverUtils ref
     * @param mainCtrl mainCtrl controller ref
     */
    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Adding a card from the + button
     */
    public void addCard(){
        mainCtrl.showCardInformation();
    }





}
