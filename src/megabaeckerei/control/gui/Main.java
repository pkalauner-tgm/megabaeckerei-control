package megabaeckerei.control.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import megabaeckerei.control.sockets.SocketHandler;

public class Main extends Application {

    private static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        initSocketHandler();
        primaryStage.setTitle("MegaBaeckerei Control");
        primaryStage.setScene(new Scene(root, 813, 311));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnHiding(event -> Platform.runLater(() -> System.exit(0)));
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static void initSocketHandler() {
        SocketHandler sh = new SocketHandler(controller);
        controller.setSocketHandler(sh);
        sh.start();
    }
}
