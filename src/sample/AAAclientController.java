package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        if (secretTextField.getText().length() != 16) {
            info += "Secret must be 16 characters length \n";
            ok = false;
        }
        if (!started && ok) {
            started = true;
            ClientStarter.start(nameTextField.getText(), passwordTextField.getText(), secretTextField.getText());
            passwordTextField.setText("");
            passwordTextField.setText("");
            passwordTextField.setText("");
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

        if(!PrintedStrings.stringsToPrint.isEmpty()){
            for (String str : PrintedStrings.stringsToPrint) {
                text += str + "\n";
            }
        }
        else {
            text = "Bład połączenia....";
        }

        logsTextArea.setText(text);
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
}
