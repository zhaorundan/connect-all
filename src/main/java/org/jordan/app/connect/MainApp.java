package org.jordan.app.connect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/22下午3:44
 */
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();
        initRootLayoutForController();

    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/org/jordan/app/connect/Main.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            MainController mainController = loader.getController();
            mainController.setRootLayout(rootLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayoutForController() {

    }

    public static void main(String[] args) {
        launch(args);
    }

}
