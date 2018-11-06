package org.jordan.app.connect.controller;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.service.RedisServiceImpl;
import org.jordan.app.connect.view.AddKeyDialogView;

import java.net.URL;
import java.util.ResourceBundle;
@Slf4j
public class AddKeyDialogController extends AddKeyDialogView implements ControllerInit {

    @Setter
    @Getter
    private RedisConsoleController redisConsoleController;
    @Getter
    @Setter
    private Stage stage;

    private String configId;
    private int dbIndex;
    final ContextMenu keyValidate = new ContextMenu();
    final ContextMenu keytypeValidate = new ContextMenu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        keyValidate.setAutoHide(false);
        keytypeValidate.setAutoHide(false);
        redisKey.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                keyValidate.hide();
            }
        });
        redisKeyType.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                keytypeValidate.hide();
            }
        });
    }


    @Override
    public void initAction() {

    }

    public void initParams(String configId, int dbIndex) {
        this.configId = configId;
        this.dbIndex = dbIndex;
    }

    public void addKeyButton() {
        String key = redisKey.getText();
        String keytype = redisKeyType.getSelectionModel().getSelectedItem();
        if (StringUtils.isBlank(key)) {
            this.keyValidate.getItems().clear();
            this.keyValidate.getItems().add(new MenuItem("Please enter username"));
            this.keyValidate.show(redisKey, Side.RIGHT, 10, 0);
            return;
        }
        if (StringUtils.isBlank(keytype)) {
            this.keytypeValidate.getItems().clear();
            this.keytypeValidate.getItems().add(new MenuItem("Please enter username"));
            this.keytypeValidate.show(redisKeyType, Side.RIGHT, 10, 0);
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("add new key , key :{},key type:{}",key,keytype);
        }
        boolean result = RedisServiceImpl.getInstance().addKey(keytype, key, configId, dbIndex);
        redisConsoleController.searchForKey();
        stage.close();
    }

    public void cancelButton() {
        stage.close();
    }
}
