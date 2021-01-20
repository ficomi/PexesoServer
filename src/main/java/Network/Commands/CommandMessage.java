/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Commands;

import Network.Matchmaking.Matchmaking;
import Network.Client.RegistredClients;
import Network.Client.RunningClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.logging.Level;


/**
 * Tato třída reprezentuje co se stane když příjde příkaz SENDMSG.
 *  Příjme zprávu od klienta a pošle ji klientům kteří jsou připojeny k chatu.
 * @author Miloslav Fico
 */
public class CommandMessage implements ICommands{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String NAME = StringCommands.SENDMSG.toString().toUpperCase();
    
    @Override
    public String doCommand(RunningClient rClient,RegistredClients regClients,PrintWriter writer,BufferedReader reader,Matchmaking matchmaking,String[] values) {
        logger.debug("Prijata zprava z chatu a rezeslána všem");
        regClients.getMesClients().sentToAllMessageClients(values[1]+"/"+values[2]);
       return""; // zde by se mohlo odesilat potvrzeni ze se vsem odeslala zprava
    }

    @Override
    public String getName() {
       return NAME;
    } 
    
}
