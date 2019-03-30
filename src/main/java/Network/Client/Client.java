/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;



/**
 *  Třída clienta.
 * @author Miloslav Fico
 */
public class Client{
    
    private String name;
    private String passwd;
    private int elo;
   

    public Client(String name,String passwd) {
       this.name=name;
       this.passwd=passwd;
       elo = 100;
    }
      public Client(String name,String passwd,int elo) {
       this.name=name;
       this.passwd=passwd;
       this.elo = elo;
    }
    
    
   

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getName() {
        return name;
    }
    
    
  
}
