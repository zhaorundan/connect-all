package org.jordan.app.connect.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public abstract  class AddKeyDialogView implements Initializable {

    @FXML
    protected ComboBox<String> redisKeyType;
    @FXML
    protected TextField redisKey;
}
