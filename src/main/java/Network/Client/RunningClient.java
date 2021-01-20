/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import Network.Matchmaking.Matchmaking;
import Network.Commands.*;
import Network.Network;
import Security.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;

/**
 * Thread který se stará a spouští příkazy odeslané klientem.
 *
 * @author Miloslav Fico
 */
public class RunningClient implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BufferedReader reader;
    private PrintWriter writer;
    private final Matchmaking matchmaking;
    private final CommandMap mapOfCommands;
    private final RegistredClients regClients;
    private boolean isThisThreadRunning = true;
    private ArrayList<Socket> socket;
    private String clientName;
    private boolean isEncrypted = false;

    public RunningClient(ArrayList<Socket> socket, Network network, RegistredClients regClients, Matchmaking matchmaking) {
        this.socket = socket;
        try {
            writer = new PrintWriter(new OutputStreamWriter(socket.get(socket.size() - 1).getOutputStream(), "UTF-8"), true);
            reader = new BufferedReader(new InputStreamReader(socket.get(socket.size() - 2).getInputStream(), "UTF-8"));

        } catch (IOException e) {
            logger.error( "Chyba navázání spojení s clientem: ",e);
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
        mapOfCommands.AddCommandToMap(new CommandLogin());
        mapOfCommands.AddCommandToMap(new CommandStartEncryption());
    }

    @Override
    public void run() {
        start();
        try {

        writer.flush();
        logger.debug("Spojení s clientem běží");
        String[] split_message;
        while (isThisThreadRunning) {
           
                String message = reader.readLine();
            System.out.println("Přijatá orig msg: "+message);
                if (isEncrypted){message = Communication.stringDecrypt(message);}
            System.out.println("Přijatá dešifrovaná msg: "+message+" "+isEncrypted);




                if (!message.isEmpty()) {

                    split_message = message.split("/");
                    if (mapOfCommands.isCommand(split_message[0])) {

                        message = mapOfCommands.getCommandClass(split_message[0]).doCommand(this, regClients, writer, reader, matchmaking, getValuesFromMessage(split_message));


                        if (!message.isEmpty()) {
                            try {
                                if (isEncrypted){
                                    message = Communication.stringEncrypt(message);
                                }
                            writer.println(message);
                            writer.flush();
                            } catch (Exception e) {
                                logger.error("Neodeslána zpráva klientovi "+clientName+" ",e);
                                e.printStackTrace();
                            }
                            
                            
                        }

                    }

                }

            }} catch (Exception i) {

                isThisThreadRunning = false;
                matchmaking.removeActivePlayer(clientName);
                regClients.getMesClients().removeMessageClient(clientName);
                logger.error("Stracení komunikace s clientem: " + clientName,i);
                logger.error("Nepodařilo se ukončit všechna spojení.");

            }
        }

    


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


    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }
}
