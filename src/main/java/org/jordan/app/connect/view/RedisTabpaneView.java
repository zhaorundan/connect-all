package org.jordan.app.connect.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public abstract class RedisTabpaneView implements Initializable {
    @FXML
    protected TextField connName;
    @FXML
    protected TextField host;
    @FXML
    protected TextField password;
    @FXML
    protected TextField port;
    @FXML
    protected CheckBox isSSL;
    @FXML
    protected Label configId;
}
