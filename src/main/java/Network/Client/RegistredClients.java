/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import Network.Messange.MessageClients;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Třída kde jsou uloženy všechny registrování klienty. Stará se o potvrzovaní, ukladání a přidávání nových clientů.
 * @author Miloslav Fico
 */

public class RegistredClients {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String path = "Clients.txt";
    private final File CLIENTS_FILE = new File(path);
    private BufferedReader reader;
    private BufferedWriter writer;
    private int clientID;
    private MessageClients mesClients;

    private HashMap<Integer, Client> regClients;

    public RegistredClients() {
        clientID = 0;
        regClients = new HashMap<>();
        regClients = checkRegisteredCLientsFromFile();
        mesClients = new MessageClients();

    }

    public synchronized void addClinetToRegClients(String name, String passwd) {
        if (!isRegisteredClient(name)) {
            regClients.put(clientID, new Client(name, passwd));
            clientID++;
            writeRegisteredClientsToText(regClients);
        }
    }

    public synchronized Client getClientFromRegClients(String name, String passwd) {
        if (isRegisteredClient(name, passwd)) {
            return regClients.entrySet().stream().filter(v -> v.getValue().getName().equals(name) && v.getValue().getPasswd().equals(passwd)).findAny().get().getValue();
        }
        return null;
    }

    public synchronized Client getClientFromRegClientsByName(String name) {
        return regClients.entrySet().stream().filter(v -> v.getValue().getName().equals(name)).findAny().get().getValue();
    }

    public synchronized boolean isRegisteredClient(String name) {
        return regClients.entrySet().stream().anyMatch(v -> v.getValue().getName().equals(name));
    }

    public synchronized boolean isRegisteredClient(String name, String passwd) {
        return regClients.entrySet().stream().anyMatch(v -> v.getValue().getName().equals(name) && v.getValue().getPasswd().equals(passwd));
    }

    public synchronized void setRegisteredClientElo(int ID, int Elo) {
        regClients.get(ID).setElo(Elo);
    }

    public MessageClients getMesClients() {
        return mesClients;
    }

    private HashMap<Integer, Client> checkRegisteredCLientsFromFile() {

        if (CLIENTS_FILE.exists() && !CLIENTS_FILE.isDirectory()) {
            try {

                reader = new BufferedReader(new FileReader(CLIENTS_FILE));
                String stringFromFile = "";
                HashMap<Integer, Client> clientsInFile = new HashMap<>();
                stringFromFile = reader.readLine();

                reader.close();

                if (stringFromFile != null && stringFromFile != "") {
                   logger.debug("Nacitani klientu ze souboru");
                    String[] data = stringFromFile.split("/");
                    String[] tempData = new String[3];
                    int position = 1;
                    for (int i = 0; i < data.length; i++) {
                        if (position % 3 == 0) {
                            tempData[2] = data[i];
                            clientsInFile.put(clientID, new Client(tempData[0], tempData[1], Integer.parseInt(tempData[2])));

                            clientID++;
                            position++;
                        } else if (position % 2 == 0) {
                            tempData[1] = data[i];
                            position++;
                        } else {
                            tempData[0] = data[i];
                            position++;
                        }
                    }
                    logger.debug("Načtení clientů ze souboru proběhlo úspěšně.");
                    return clientsInFile;
                }
                return new HashMap<>();

            } catch (IOException e) {
                logger.error("Chyba při čtení Clients souboru" + e);

                return new HashMap<>();
            }
        } else {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
                writer.write("");
                writer.flush();
                return null;
            } catch (IOException e) {
                logger.error("Chyba při vytvoření ID souboru " + e);
                return new HashMap<>();

            }
        }

    }

    public synchronized void writeRegisteredClientsToText(HashMap<Integer, Client> regClients) {

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
            for (int i = 0; i < clientID; i++) {
                writer.write(regClients.get(i).getName() + "/" + regClients.get(i).getPasswd() + "/" + regClients.get(i).getElo() + "/");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("Chyba při aktualizování Clients souboru: "+e);
        }

    }

    public int getClientID() {
        return clientID;
    }

    public HashMap<Integer, Client> getRegClients() {
        return regClients;
    }
    
    

}
