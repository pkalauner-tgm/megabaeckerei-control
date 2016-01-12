package megabaeckerei.control.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import megabaeckerei.control.sockets.ClientType;
import megabaeckerei.control.sockets.SocketHandler;
import megabaeckerei.control.sockets.Values;


public class Controller {
    private SocketHandler sh;
    @FXML
    private Button bLagerToWater;
    @FXML
    private Button bWaterToMischer;
    @FXML
    private Button bLagerToMix;
    @FXML
    private Button bMixToMixer;
    @FXML
    private Button bMixerToOven;
    @FXML
    private Button bTakeBread;
    @FXML
    private Label labelLevelWater;
    @FXML
    private Label labelLevelMixer;
    @FXML
    private Label labelLevelOven;
    @FXML
    private Label labelLevelMixtank;
    @FXML
    private Slider temperatureSlider;
    @FXML
    private Label labelSliderTemp;
    @FXML
    private Label labelMixtureStatus;
    @FXML
    private Label labelMixerStatus;
    @FXML
    private Label labelWaterStatus;
    @FXML
    private Label labelOvenStatus;

    public void initialize() {
        temperatureSlider.valueChangingProperty().addListener((obs, wasChanging, isNowChanging) -> {
            if (!isNowChanging) {
                int newVal = (int) temperatureSlider.getValue();
                labelSliderTemp.setText("Temperature: " + newVal + "\u00b0 C");
                // sh.sendToSocket(ClientType.OVEN, "set_temperature " + newVal);
            }
        });
    }

    public void updateLevelWater(int newLevel) {
        this.labelLevelWater.setText("Level: " + newLevel + "/" + Values.MAX_LEVEL_WATER);
    }

    public void updateLevelMixtank(int newLevel) {
        this.labelLevelMixtank.setText("Level: " + newLevel + "/" + Values.MAX_LEVEL_MIXTANK);
    }

    public void updateLevelMixer(int newLevel) {
        this.labelLevelMixer.setText("Level: " + newLevel + "/" + Values.MAX_LEVEL_MIXER);
    }

    public void updateLevelOven(int newLevel) {
        this.labelLevelOven.setText("Level: " + newLevel + "/" + Values.MAX_LEVEL_OVEN);
    }


    public void changeLabelMixture(boolean connected) {
        bLagerToMix.setDisable(!connected);
        bMixToMixer.setDisable(!connected);
        this.changeLabel(labelMixtureStatus, connected);
    }

    public void changeLabelMixer(boolean connected) {
        bMixerToOven.setDisable(!connected);
        this.changeLabel(labelMixerStatus, connected);
    }

    public void changeLabelWater(boolean connected) {
        bLagerToWater.setDisable(!connected);
        bWaterToMischer.setDisable(!connected);
        this.changeLabel(labelWaterStatus, connected);
    }

    public void changeLabelOven(boolean connected) {
        temperatureSlider.setDisable(!connected);
        bTakeBread.setDisable(!connected);
        if (connected)
            Platform.runLater(() -> labelSliderTemp.setText("Temperature: " + 0 + "\u00b0 C"));
        this.changeLabel(labelOvenStatus, connected);
    }

    private void changeLabel(Label label, boolean connected) {
        Platform.runLater(() -> {
            if (connected) {
                label.setTextFill(Color.GREEN);
                label.setText("Connected");
            } else {
                label.setTextFill(Color.RED);
                label.setText("Disconnected");
            }
        });
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
        sh.sendToSocket(ClientType.MIXER, "toggle_mixer_to_oven_ventil");
    }

    @FXML
    public void bMixToMixerClick(ActionEvent e) {
        sh.sendToSocket(ClientType.MIXTANK, "toggle_mixtank_to_mixer_ventil");
    }

    @FXML
    public void bLagerToMixClick(ActionEvent e) {
        sh.sendToSocket(ClientType.MIXTANK, "toggle_lager_to_mixtank_ventil");
    }

    @FXML
    public void bTakeBreadClick(ActionEvent e) {
        sh.sendToSocket(ClientType.OVEN, "take_bread");
    }
}
