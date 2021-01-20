/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Messange;


import Security.Communication;

import java.io.PrintWriter;

/**
 * Tato třída reprezentuje chatujícího klienta.
 * @author Miloslav Fico
 */
public class MessageClient {
   
    PrintWriter writer;

    public MessageClient(PrintWriter writer) {
 
        this.writer = writer;
    }

  

    public PrintWriter getWriter() {
        return writer;
    } 
    
    public void sentMessage(String message) throws Exception{
     writer.println(Communication.stringEncrypt("RECMSG/"+message+";"));
     writer.flush();
    }
}
