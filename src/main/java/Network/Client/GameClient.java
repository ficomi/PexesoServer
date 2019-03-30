/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;

/**
 *  Třída GameClienta ve kterém je uložen client a jeho BufferedReader příjmu zpráv a PrintWriter pro odesílaní zprav.
 * @author Miloslav Fico
 */
public class GameClient implements Comparable<GameClient>{

    Client client;
    PrintWriter writer;
    BufferedReader reader;
    Socket socketIn;
    
    
    
    public GameClient(Client client,PrintWriter writer,BufferedReader reader) {
       this.client=client;
        this.writer = writer;
        this.reader = reader; 
     
    }
    
   

 
    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Client getClient() {
        return client;
    }
    
  @Override
  public int compareTo(GameClient u) {
      int compereElo = u.getClient().getElo();
    return compereElo - this.client.getElo(); // Od nejvyssiho
  }
}  
    
    
    
    
    
    
    
    
    
    


    
    
    
    
    
    
    
    
    
    

