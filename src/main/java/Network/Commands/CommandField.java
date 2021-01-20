/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Commands;

import Network.Client.RegistredClients;
import Network.Client.RunningClient;
import Network.Matchmaking.Matchmaking;
import Security.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 *
 * @author pix
 */
public class CommandField implements ICommands {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String NAME = StringCommands.FIELD.toString().toUpperCase();

    @Override
    public String doCommand(RunningClient rClient, RegistredClients regClients, PrintWriter writer, BufferedReader reader, Matchmaking matchmaking, String[] values) {

       

        try {
            matchmaking.getPlayingClientByName(values[values.length-1]).getWriter().println(Communication.stringEncrypt("FIELD/"+values[1]+"/"+values[2]+"/"+values[3]+"/;"));
            matchmaking.getPlayingClientByName(values[values.length-1]).getWriter().flush();
            logger.debug("Přijato a odesláno pro: "+values[values.length-1]);
            
        } catch (Exception e) {

            logger.debug("Nenalezen hrající hráč s tímto jménem: "+values[values.length-1]);
            
            return "Server nemohl najít protihráče;";
        }
        
       
        return "Field recived";
    }

    @Override
    public String getName() {
        return NAME;
    }

}