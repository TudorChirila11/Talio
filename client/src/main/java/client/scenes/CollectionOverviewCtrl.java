/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionOverviewCtrl implements Initializable {

    @FXML
    private FlowPane container ;

    private List<Label> labels ;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public CollectionOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    public void addCard() {
        mainCtrl.showAdd();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labels = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Label label = new Label("Label "+i);
            labels.add(label);
            container.getChildren().add(label);
        }
    }
}