/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Network.Matchmaking.Matchmaking;
import Network.Client.RegistredClients;
import Network.Client.RunningClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Třída která každé příchozí spojení akceptuje a přidělí mui vlastni thread
 * clienta.
 *
 * @author Miloslav Fico
 */
public class Network implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RegistredClients regClients;
    private ServerSocket serSocket;
    private boolean isRunnig;
    private ArrayList<Thread> clients;
    private ArrayList<Socket> client;
    private Matchmaking matchmaking;

    public Network() {
        isRunnig = true;

        clients = new ArrayList<>();
        client = new ArrayList<>();
        regClients = new RegistredClients();
        try {
            serSocket = new ServerSocket(5010);

        } catch (IOException e) {
            //nepodarilo se vytvorit server
        }

        matchmaking = new Matchmaking(this);

    }

    @Override
    public void run() {

        startListening();

    }

    private void startListening() {
        logger.debug("Poslouchám");
        while (isRunnig) {

            if (serSocket.isBound()) {
                try {
                    client.add(serSocket.accept());
                    if (client.size() == 2) {
                        recClient();
                        logger.debug("Client prijat");
                        client.clear();
                    }
                } catch (IOException e) {
                    logger.error("Error: " + e);
                }

            }

        }

    }

    private void recClient() {

        clients.add(new Thread(new RunningClient(client, this, regClients, matchmaking)));
        logger.debug("Spuštěn thread pro clienta.");
        clients.get(clients.size() - 1).start();

    }

    public synchronized ArrayList<Thread> getClients() {
        return clients;
    }

    public ServerSocket getSerSocket() {
        return serSocket;
    }

    public synchronized boolean getIsRunning() {
        return isRunnig;
    }

    public void setIsRunnig(boolean isRunnig) {
        this.isRunnig = isRunnig;
    }

    public RegistredClients getRegClients() {
        return regClients;
    }

    public Matchmaking getMatchmaking() {
        return matchmaking;
    }

}
