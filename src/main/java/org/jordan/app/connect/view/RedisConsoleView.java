package org.jordan.app.connect.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public abstract class RedisConsoleView implements Initializable {
    @FXML
    protected ComboBox<String> redisDatabases;
    @FXML
    protected TextField keysCount;
    @FXML
    protected TextField searchKey;
    @FXML
    protected TableView redisKeyView;
    @FXML
    protected CheckBox searchHit;
    @FXML
    protected TableView redisValueView;
    @FXML
    protected Tab keyTab;
    @FXML
    protected TextField keyName;
    @FXML
    protected TextField keyTTL;

}
