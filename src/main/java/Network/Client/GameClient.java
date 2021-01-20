/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import Database.Entities.Client;
import java.io.PrintWriter;
import java.io.BufferedReader;

/**
 *  Třída GameClienta ve kterém je uložen client a jeho BufferedReader příjmu zpráv a PrintWriter pro odesílaní zprav.
 * @author Miloslav Fico
 */
public class GameClient extends Client implements Comparable<GameClient>{


    PrintWriter writer;
    BufferedReader reader;


    public GameClient(PrintWriter writer, BufferedReader reader, byte[] passwd,int elo, byte[] salt, String name) {
        super(passwd, salt, name);
        this.writer = writer;
        this.reader = reader;
        setElo(elo);
    } 
    
    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
 
    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }


    
  @Override
  public int compareTo(GameClient u) {
      int compereElo = u.getElo();
    return compereElo - this.getElo(); // Od nejvyssiho
  }
}  
    
    
    
    
    
    
    
    
    
    


    
    
    
    
    
    
    
    
    
    

