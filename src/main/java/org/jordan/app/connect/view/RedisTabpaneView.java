package org.jordan.app.connect.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    @FXML
    protected ListView<Label> redisListview;
    @FXML
    protected TabPane redisTabpane;
}
