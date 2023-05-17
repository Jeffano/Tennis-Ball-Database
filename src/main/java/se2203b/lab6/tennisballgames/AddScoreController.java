package se2203b.lab6.tennisballgames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddScoreController implements Initializable {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> comboBoxMatch;

    @FXML
    private TextField txtHomeScore;

    @FXML
    private TextField txtVisitorScore;

    final ObservableList<String> data = FXCollections.observableArrayList();

    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;
    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }

    @FXML
    public void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        }
        catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage()); }
    }

    private void displayAlert(String msg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/se2203b/lab6/tennisballgames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save() {

        String match = comboBoxMatch.getValue();
        String[] info = match.split("\\s*-\\s*");

        int matchNumber = Integer.parseInt(info[0]);
        int hScore = Integer.parseInt(txtHomeScore.getText());
        int vScore = Integer.parseInt(txtVisitorScore.getText());
        String hTeam = info[1];
        String vTeam = info[2];

        try {
            matchesAdapter.setTeamsScore(matchNumber, hScore, vScore);
            teamsAdapter.setStatus(hTeam,vTeam,hScore,vScore);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxMatch.setItems(data);
    }

}
