/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Messange;

import java.util.HashMap;


/**
 * V této tříde jsou uloženy všichni chatující klienti.
 * @author Miloslav Fico
 */
public class MessageClients {

    private HashMap<String, MessageClient> messageClients;

    public MessageClients() {
        messageClients = new HashMap<>();

    }

    public synchronized void addToMessageClients(String name, MessageClient client) {

        if (!messageClients.containsKey(name)) {
            messageClients.put(name, client);
        }

    }

    public  synchronized MessageClient getMessegeClient(String name) {
        if (messageClients.containsKey(name)) {
            return messageClients.get(name);
        }
        return null;
    }

    public  synchronized void removeMessageClient(String name) {
        if (messageClients.containsKey(name)) {
            messageClients.remove(name);
        }
    }

    public  synchronized void sentToAllMessageClients(String message){
        try {
            for (String name : messageClients.keySet()) {
                messageClients.get(name).sentMessage(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
}
