package sample;

import com.sun.javafx.tk.Toolkit;
import dk.i1.diameter.node.EmptyHostNameException;
import dk.i1.diameter.node.UnsupportedTransportProtocolException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Bartłomiej on 06.01.2017.
 */
public class ClientStarter {

    private static String[] args;
    static String host_id;
    static String realm;
    static String dest_host;

    public ClientStarter(String[] args){
        this.args = args;
        host_id = args[0];
        realm = args[1];
        dest_host = args[2];
    }

    public static void start(String name, String password, String secret){
        Runnable t = () -> {

            if(args.length!=4) {
                System.out.println("Usage: <host-id> <realm> <peer> <peer-port>");
                return;
            }

            String host_id = args[0];
            String realm = args[1];
            String dest_host = args[2];
            int dest_port = Integer.parseInt(args[3]);
            DiameterClient client = new DiameterClient(host_id, realm, dest_host, dest_port);

            client.setPassword(password);
            client.setUsername(name);
            client.setSecret(secret);

            try {

                client.start();

            } catch (EmptyHostNameException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedTransportProtocolException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            client.runAaProcess();

            client.stop();
        };
        Thread thread = new Thread(t);
        thread.start();
    }
}
