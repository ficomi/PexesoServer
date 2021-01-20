/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import Database.DatabaseController;
import Database.Entities.Client;
import Network.Messange.MessageClients;
import java.util.List;

import Security.Passwords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Třída kde jsou uloženy všechny registrování klienty. Stará se o potvrzovaní,
 * ukladání a přidávání nových clientů.
 *
 * @author Miloslav Fico
 */
public class RegistredClients {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private MessageClients mesClients;
    private Passwords psswd;
    private DatabaseController db;

    public RegistredClients() {
        psswd = new Passwords();
        db = new DatabaseController();
        mesClients = new MessageClients();
       

    }

    public synchronized boolean addClinetToRegClients(String name, String passwd) {
        if (!isRegisteredClient(name)) {
            byte[] salt = psswd.getNextSalt();     
            Client c = new Client(psswd.hash(passwd.toCharArray(), salt),salt,name);
            c.setElo(100);
            db.addClient(c);
            return true;
        }
        return false;
    }

    public synchronized Client getClientFromRegClientsById(int id) {
        return db.getClients().stream().filter(v -> v.getId() == id).findAny().get();
    }

    public synchronized boolean isRegisteredClient(int id) {
        return db.getClients().stream().anyMatch(v -> v.getId() == id);
    }

    public synchronized boolean isRegisteredClient(String name, String passwd) {
        return db.getClients().stream().anyMatch(v -> v.getName().equals(name) && psswd.isExpectedPassword(passwd.toCharArray(), v.getSalt() , v.getPasswd()));
    }

    public synchronized void setRegisteredClientElo(int ID, int Elo) {
        db.getClients().get(ID).setElo(Elo);
    }

    public synchronized int getIdByName(String name) {
        return db.getClients().stream().filter(v -> v.getName().equals(name)).findFirst().get().getId();
    }

    public synchronized boolean isRegisteredClient(String name) {
      return db.getClients().stream().anyMatch(v -> name.equals(v.getName()));
    }

    public synchronized MessageClients getMesClients() {
        return mesClients;
    }
    
    public synchronized void updateClientsAfterGame(Client[] clients){
        for (Client c : clients) {
            db.updateClientsElo(c, c.getElo());
        }
    }

    private synchronized List<Client> checkRegisteredCLientsFromDatabase() {
        return db.getClients();
    }

}
