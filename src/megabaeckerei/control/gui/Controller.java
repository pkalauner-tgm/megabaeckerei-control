package megabaeckerei.control.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
    @FXML
    private Label labelLevelMixer;
    @FXML
    private Label labelTemperatureOven;
    @FXML
    private Label labelLevelMixtank;
    @FXML
    private Slider temperatureSlider;
    @FXML
    private Label labelSliderTemp;

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

    @FXML
    public void setSocketHandler(SocketHandler sh) {
        this.sh = sh;
    }

    @FXML
    public void bMixerToOvenClick(ActionEvent e) {
    }

    @FXML
    public void bMixToMixerClick(ActionEvent e) {
    }

    @FXML
    public void bLagerToMixClick(ActionEvent e) {
    }
}
