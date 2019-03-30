/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import Network.Matchmaking.Matchmaking;
import Network.Commands.*;
import Network.Network;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Thread který se stará a spouští příkazy odeslané klientem.
 * @author Miloslav Fico
 */
public class RunningClient implements Runnable {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private BufferedReader reader;
    private PrintWriter writer;
    private final Matchmaking matchmaking;
    private final CommandMap mapOfCommands;
    private final RegistredClients regClients;
    private boolean isThisThreadRunning = true;
    private ArrayList<Socket> socket;
    private String clientName;

    public RunningClient(ArrayList<Socket> socket, Network network, RegistredClients regClients, Matchmaking matchmaking) {
        this.socket = socket;
        try {
            writer = new PrintWriter(new OutputStreamWriter(socket.get(socket.size() - 1).getOutputStream(), "UTF-8"), true);
            reader = new BufferedReader(new InputStreamReader(socket.get(socket.size() - 2).getInputStream(), "UTF-8"));
            
        } catch (IOException e) {
           logger.error("Chyba navázání spojení s clientem: " + e);
        }

        this.regClients = regClients;
        this.matchmaking = matchmaking;
        mapOfCommands = new CommandMap();

    }

    public void start() {
        mapOfCommands.AddCommandToMap(new CommandAddUserToRegUsers());
        mapOfCommands.AddCommandToMap(new CommandStartCommunication());
        mapOfCommands.AddCommandToMap(new CommandEndGame());
        mapOfCommands.AddCommandToMap(new CommandMessage());
        mapOfCommands.AddCommandToMap(new CommandStartSearching());
        mapOfCommands.AddCommandToMap(new CommandField());
    }

    @Override
    public void run() {

        start();
        logger.debug("Spojení s clientem běží");
        String[] split_message;
        while (isThisThreadRunning) {
            try {
                String message = "";

                message = reader.readLine();
                System.out.println(message);
                if (!message.isEmpty()) {
                   
                    split_message = message.split("/");
                    if (mapOfCommands.isCommand(split_message[0])) {

                        message = mapOfCommands.getCommandClass(split_message[0]).doCommand(this,regClients,writer,reader,matchmaking,getValuesFromMessage(split_message));
                        if (!message.isEmpty()) {
                              writer.println(message);
                              writer.flush();
                        }
                      

                    }

                }

            } catch (NullPointerException e) {
                isThisThreadRunning = false;
                try {
                    matchmaking.removeActivePlayer(clientName);
                    regClients.getMesClients().removeMessageClient(clientName);
                   logger.error("Stracení komunikace s clientem: "+clientName+" "+ e);
                    for (Socket clients : socket) {
                        clients.close();
                    }
                    writer.close();
                    reader.close();
                } catch (IOException i) {
                    logger.error("Nepodařilo se ukončit všechna spojení.");
                }

            }catch (IOException i){
            logger.debug("Chyba při přijetí zprávy");
            }
        }
      
    }

//    private void sentWriter() {
//        if (!wMessage.isEmpty()) {
//            try {
//                System.out.println("Writer " + writer.toString());
//                writer.write(wMessage);
//                writer.newLine();
//                writer.flush();
//            } catch (IOException e) {
//
//            }
//
//        }
//
//    }
    private String[] getValuesFromMessage(String[] message) {
        message[message.length - 1] = message[message.length - 1].replace(";", ""); // oseka o ";"
        return message;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

  

}
