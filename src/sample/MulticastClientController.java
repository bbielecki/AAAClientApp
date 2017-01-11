package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static sample.Main.PrintedStrings;

/**
 * Created by Bartłomiej on 11.01.2017.
 */
public class MulticastClientController implements Initializable{
    @FXML Button showButton;
    @FXML Button sendButton;

    @FXML TextArea commonTextArea;
    @FXML TextArea logTextArea;

    @FXML TextField sharedMessageText;

    private Storage st;
    private MulticastSocket mrec;
    private SecretKey originalKey;
    private Cipher cipher;
    private byte[] encodedKey;
    private  InetAddress group;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String groupIP = PrintedStrings.address;
        String keyString = PrintedStrings.key;

        PrintedStrings.address = null;
        try {
            st = new Storage();
            mrec = new MulticastSocket(3456);
            group = InetAddress.getByName(groupIP);
            mrec.joinGroup(group);
            sendRequestForData(group);

            encodedKey = keyString.getBytes("ASCII");
            originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            cipher = Cipher.getInstance("AES");

            Runnable listener = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            byte[] messageBuffer = new byte[1000];
                            DatagramPacket messagePack = new DatagramPacket(messageBuffer, messageBuffer.length);
                            mrec.receive(messagePack);
                            String lol1 = new String(messagePack.getData(), 0, messagePack.getLength());
                            if (lol1.equals("Hello")) {
                                DatagramPacket pack;
                                cipher.init(Cipher.ENCRYPT_MODE, originalKey);
                                if (!st.getStore().isEmpty()) {
                                    for (String s : st.getStore()) {
                                        String sEncrypted = "HelloRes " + AES.encrypt(s, cipher);
                                        pack = new DatagramPacket(sEncrypted.getBytes(), sEncrypted.length(), group, 3456);
                                        mrec.send(pack);
                                    }
                                }
                            } else if (lol1.contains("HelloRes ")) {
                                cipher.init(Cipher.DECRYPT_MODE, originalKey);
                                String splittedMessage = lol1.split("\\s+")[1];
                                String decryptedMessage = AES.decrypt(splittedMessage, cipher);
                                st.addToStore(decryptedMessage);
                                st.store = st.getStore().stream().distinct().collect(Collectors.toList());
                            } else {
                                cipher.init(Cipher.DECRYPT_MODE, originalKey);
                                System.err.println("Zaszyfrowana wiadomość odebrana: " + lol1);
                                lol1 = AES.decrypt(lol1, cipher);
                                st.addToStore(lol1);
                                System.err.println("Wiadomość: " + lol1);
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            };

            new Thread(listener).start();

        }
        catch (Exception e){
            e.getMessage();
        }


    }

    public void send(){
        if(!sharedMessageText.getText().equals("")) {
            try {
                String message = sharedMessageText.getText();
                cipher.init(Cipher.ENCRYPT_MODE, originalKey);
                message = AES.encrypt(message, cipher);
                System.out.println("Zaszyfrowana wiadomość: " + message);
                DatagramPacket pack = new DatagramPacket(message.getBytes(), message.length(), group, 3456);
                mrec.send(pack);
                System.out.println("Wysłałem pakiet!");

                sharedMessageText.clear();
            }catch (Exception e){
                e.getMessage();
            }
        }
    }

    public void show(){

        commonTextArea.setText(st.getStore().toString());

    }

    public void sendRequestForData(InetAddress group){
        try {
            String welcomeMessage = "Hello";
            System.out.println("Wysłalem hellooo");
            DatagramPacket pack = new DatagramPacket(welcomeMessage.getBytes(), welcomeMessage.length(), group, 3456);
            mrec.send(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
