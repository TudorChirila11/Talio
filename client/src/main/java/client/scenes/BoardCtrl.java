package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

import javax.swing.plaf.synth.SynthTextAreaUI;

public class BoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    /**
     * Adding a card from the + button
     */
    public void addCard(){
        System.out.println("Can't add Card yet!");
    }







}
