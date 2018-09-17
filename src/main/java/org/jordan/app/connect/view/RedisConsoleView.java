package org.jordan.app.connect.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.*;

public abstract class RedisConsoleView implements Initializable {
    @FXML
    protected ComboBox<String> redisDatabases;
    @FXML
    protected TextField keysCount;
    @FXML
    protected TextField searchKey;
    @FXML
    protected Checkbox searchAccurate;
    @FXML
    protected TableView redisKeyView;

}
