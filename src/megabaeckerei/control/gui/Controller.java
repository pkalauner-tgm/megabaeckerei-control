package megabaeckerei.control.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import megabaeckerei.control.sockets.ClientType;
import megabaeckerei.control.sockets.SocketHandler;
import megabaeckerei.control.sockets.Values;


public class Controller {
    private SocketHandler sh;

    @FXML
    public Button bLagerToWater;
    @FXML
    public Button bWaterToMischer;
    @FXML
    private Label labelLevelWater;

    public void updateLevelWater(int newLevel) {
        this.labelLevelWater.setText("Level: " + newLevel + "/" + Values.MAX_LEVEL_WATER);
    }

    @FXML
    protected void bLagerToWaterClick(ActionEvent e) {
        sh.sendToSocket(ClientType.WATERTANK, "toggle_lager_to_water_ventil");
    }

    @FXML
    protected void bWaterToMischerClick(ActionEvent e) {
        sh.sendToSocket(ClientType.WATERTANK, "toggle_water_to_mischer_ventil");
    }

    public void setSocketHandler(SocketHandler sh) {
        this.sh = sh;
    }

}
