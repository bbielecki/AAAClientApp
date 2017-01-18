package sample;

import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static sample.Main.PrintedStrings;
/**
 * Created by Bartłomiej on 06.01.2017.
 */
public class AAAclientController {
    @FXML TextField nameTextField;
    @FXML TextField secretTextField;
    @FXML TextField passwordTextField;

    @FXML CheckBox defaultCheck;

    @FXML Button connectButton;
    @FXML Button printLogsButton;
    @FXML Button infoExchangeButton;

    @FXML Label resultLabel;
    @FXML TextArea logsTextArea;
    private boolean started = false, ok = false;

    public void connectionAction(ActionEvent event) {

        ok = true;
        String info = "";
        if (nameTextField.getText().equals("")) {
            info += "Please enter the name \n";
            ok = false;
        }
        if (secretTextField.getText().equals("")) {
            info += "Please enter the secret \n";
            ok = false;
        }
        if (secretTextField.getText().length() != 16) {
            info += "Secret must be 16 characters length \n";
            ok = false;
        }
        if (passwordTextField.getText().equals("")) {
            info += "Please enter the password \n";
            ok = false;
        }
        if (!started && ok) {
            started = true;
            PrintedStrings.key = secretTextField.getText();
            ClientStarter.start(nameTextField.getText(), passwordTextField.getText(), secretTextField.getText());


            Runnable task = () -> {

                while (PrintedStrings.address == null) {
                    infoExchangeButton.setDisable(false);
                    infoExchangeButton.setVisible(true);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            };

            Thread t = new Thread(task);
            t.start();


            passwordTextField.setText("");
            passwordTextField.setText("");
            passwordTextField.setText("");
            logsTextArea.clear();
            ok = false;
            started = false;
            printLogsButton.setVisible(true);
        }

        resultLabel.setText(info);

    }

    public void printAction(ActionEvent event){
        String text = "";
        logsTextArea.setVisible(true);
        logsTextArea.setEditable(false);
        int i = 1;

        if(!PrintedStrings.stringsToPrint.isEmpty()){
            for (String str : PrintedStrings.stringsToPrint) {
                text += i + " " + str + "\n";
                i++;
                if(i>7) {
                    text += "-------------------------------------------" + "\n";
                    i = 1;
                }
            }
            PrintedStrings.stringsToPrint.clear();
            logsTextArea.setText(text);
            i = 1;
        }
        if(PrintedStrings.stringsToPrint.isEmpty() && logsTextArea.getText().equals("")){
            logsTextArea.setText("Bład połączenia....");
        }
    }

    public void checkAction(){
        if(defaultCheck.isSelected()){
            nameTextField.setText("user");
            secretTextField.setText("czescczescczesc1");
            passwordTextField.setText("aaa");
        }
        else {
            nameTextField.clear();
            secretTextField.clear();
            passwordTextField.clear();
        }
    }

    public void startExchangeAction() throws IOException {
        Parent multicast = FXMLLoader.load(getClass().getResource("MulticastClient.fxml"));
        Scene multicastScene = new Scene(multicast);
        Stage multicastStage = new Stage();
        multicastStage.setTitle("Communication");
        multicastStage.setScene(multicastScene);
        multicastStage.show();

        multicastStage.setOnCloseRequest(e -> Platform.exit());

        infoExchangeButton.setDisable(true);
    }
}
