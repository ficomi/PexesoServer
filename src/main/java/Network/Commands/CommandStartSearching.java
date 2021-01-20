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
 * Tato třída reprezentuje co se stane když příjde příkaz SSEARCH.
 *  Přidá klienta do fronty klirntů čekající na hru.
 * @author Miloslav Fico
 */
public class CommandStartSearching implements ICommands {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String NAME = StringCommands.SSEARCH.toString().toUpperCase();

    @Override
    public String doCommand(RunningClient rClient,RegistredClients regClients,PrintWriter writer,BufferedReader reader,Matchmaking matchmaking,String[] values) 
    {
   
    if(Boolean.valueOf(values[3]) && regClients.isRegisteredClient(values[1],values[2])){
        logger.debug("Přidání clienta do front na hru: "+values[1]);
     matchmaking.addClientToSearch(regClients.getClientFromRegClientsById(regClients.getIdByName(values[1])),writer,reader);
    
        return "SSEARCH/add";
    }else{
        logger.debug("Odebrání clienta z fronty na hru: "+values[1]);
           matchmaking.removeClientFromSearch(matchmaking.getClientFromSearch(values[1]));
     
        return "SSEARCH/remove";
        }
    }
        
          
        
      

    @Override
    public String getName() {
        return NAME;
    }
}


    

