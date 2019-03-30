/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Commands;

import Network.Client.GameClient;
import Network.Matchmaking.Matchmaking;
import Network.Client.RegistredClients;
import Network.Client.RunningClient;
import Network.Messange.MessageClient;
import java.io.BufferedReader;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tato třída reprezentuje co se stane když příjde příkaz EGAME. Přidelí ELO
 * podle výsledku hry a ukončí thred na kterém se klienti mezi soubou
 * domlouvaly.
 *
 * @author Miloslav Fico
 */
public class CommandEndGame implements ICommands {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String NAME = StringCommands.EGAME.toString().toUpperCase();

    @Override
    public String doCommand(RunningClient rClient, RegistredClients regClients, PrintWriter writer, BufferedReader reader, Matchmaking matchmaking, String[] values) {

        regClients.getClientFromRegClientsByName(values[1]).setElo(regClients.getClientFromRegClientsByName(values[1]).getElo() + 1);
        regClients.getClientFromRegClientsByName(values[2]).setElo(regClients.getClientFromRegClientsByName(values[2]).getElo() - 1);

        GameClient[] players = new GameClient[2];

        for (int i = 0; i < players.length; i++) {
            players[i] = matchmaking.getPlayingClientByName(values[i+1]);
            
            
            
            if (players[i]!=null) {
            players[i].getWriter().println("UPDATE/" + regClients.getClientFromRegClientsByName(values[i+1]).getElo() + ";\n\r");
            players[i].getWriter().flush();
            regClients.getMesClients().addToMessageClients(matchmaking.getPlayingClientByName(values[i+1]).getClient().getName(), new MessageClient(matchmaking.getPlayingClientByName(values[i+1]).getWriter()));
            matchmaking.removePlayingClient(values[i+1]);
            regClients.getMesClients().addToMessageClients(values[i+1], new MessageClient(players[i].getWriter()));
            logger.debug("Úspěšně ukončena hra pro :"+players[i].getClient().getName());
            }else{
            logger.debug("Neco se stalo");
            }
           
        }

        regClients.writeRegisteredClientsToText(regClients.getRegClients());

        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }

}
