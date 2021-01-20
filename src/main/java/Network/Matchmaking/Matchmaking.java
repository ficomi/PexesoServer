/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Matchmaking;


import Database.Entities.Client;
import Network.Client.GameClient;
import Network.Client.RegistredClients;

import Network.Network;
import Security.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;


/**
 * Třída která se stárá o clienty hledající hru, aktuálně přihlášené clienty a thready spuštěných her.
 *
 * @author Miloslav Fico
 */


public class Matchmaking {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ArrayList<GameClient> searchingClients;
    private ArrayList<GameClient> playingClients;
    private ArrayList<String> activeClient;
    private ConcurrentHashMap<String, Thread> threadMap;
    private Network network;
    private RegistredClients regClients;

    public Matchmaking(Network network) {
        this.network = network;
        searchingClients = new ArrayList<>();
        activeClient = new ArrayList<>();
        regClients = network.getRegClients();
        threadMap = new ConcurrentHashMap<>();
        playingClients = new ArrayList<>();
        searchForGame();
    }


    public synchronized void addClientToSearch(Client client, PrintWriter writer, BufferedReader reader) {
        if (!searchingClients.stream().anyMatch(v -> v.getName().equals(client.getName()))) {
            searchingClients.add(new GameClient(writer, reader, client.getPasswd(),client.getElo(), client.getSalt(), client.getName()));
            sortClients();
        }

    }

    public synchronized int getIdByName(String name) {
        return searchingClients.stream().filter(v -> name.equals(v.getName())).findAny().get().getId();

    }

    public synchronized GameClient getClientFromSearch(String name) {
        return searchingClients.stream().filter(v -> v.getName().equals(name)).findAny().get();
    }

    public synchronized void removeClientFromSearch(GameClient client) {
        searchingClients.remove(client);
        sortClients();
    }

    public synchronized GameClient getPlayingClientByName(String name) {

        return playingClients.stream().filter(v -> name.equals(v.getName())).findAny().get();

    }

    public synchronized boolean isClientPlaying(String name) {
        return playingClients.stream().anyMatch(v -> v.equals(name));
    }

    public synchronized void removePlayingClient(String name) {

        playingClients.remove(getPlayingClientByName(name));

    }

    public synchronized Thread getGameThread(String name) {
        return threadMap.get(name);
    }

    private synchronized void sortClients() {
        Collections.sort(searchingClients);
    }

    public void searchForGame() {
        Thread searchingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (network.getIsRunning()) {
                    try {
                        if (searchingClients.size() % 2 == 0 && searchingClients.size() != 0) {
                            String name = searchingClients.get(0).getName() + "x" + searchingClients.get(1).getName();

                            logger.debug("Spuštěna hra kde hrají: " + name);

                            playingClients.add(searchingClients.get(0));
                            playingClients.add(searchingClients.get(1));

                            searchingClients.get(0).getWriter().println(Communication.stringEncrypt("SGAME/" + searchingClients.get(1).getName() + "/" + searchingClients.get(1).getElo() + "/true/;"));
                            searchingClients.get(1).getWriter().println(Communication.stringEncrypt("SGAME/" + searchingClients.get(0).getName() + "/" + searchingClients.get(0).getElo() + "/false/;"));
                            regClients.getMesClients().removeMessageClient(searchingClients.get(0).getName());
                            regClients.getMesClients().removeMessageClient(searchingClients.get(1).getName());

                            searchingClients.remove(0);
                            searchingClients.remove(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        searchingThread.start();

    }

    public synchronized ArrayList<GameClient> getSearchingClients() {
        return searchingClients;
    }

    public synchronized ConcurrentHashMap<String, Thread> getThreadMap() {
        return threadMap;
    }

    public synchronized void addToActiveClients(String name) {
        activeClient.add(name);
    }

    public synchronized boolean isActivePlayer(String name) {
        return activeClient.stream().anyMatch(v -> v.equals(name));
    }

    public synchronized void removeActivePlayer(String Name) {
        if (isActivePlayer(Name)) {
            activeClient.remove(Name);
        }
    }

    public synchronized ArrayList<String> getActiveClients() {
        return activeClient;
    }

    public ArrayList<GameClient> getPlayingClients() {
        return playingClients;
    }

}
