package org.jordan.app.connect.controller;

import org.jordan.app.connect.view.RedisTabpaneView;

import java.net.URL;
import java.util.ResourceBundle;

public class RedisTabpaneController extends RedisTabpaneView {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void clickview() {

    }

    public void addConfig() {
        resetConfig();
    }

    public void connectTest() {

    }
    public void saveConfig() {

    }


    public void resetConfig() {
        resetConfig(true);
    }

    /**
     * 重置按钮
     * @param exists
     */
    private void resetConfig(boolean exists) {
        connName.setText("");
        host.setText("");
        password.setText("");
        port.setText("");
        isSSL.setSelected(false);
        if (!exists) {
            configId.setText("");
        }
    }
}
