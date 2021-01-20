/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Commands;

import Network.Matchmaking.Matchmaking;
import Network.Client.RegistredClients;
import Network.Client.RunningClient;
import Network.Messange.MessageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 * Tato třída reprezentuje co se stane když příjde příkaz STARTCOM Oveřuje
 * jestli data které server přijal odpovídají registrovanému klientovi.
 *
 * @author Miloslav Fico
 */
public class CommandLogin implements ICommands {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String NAME = StringCommands.LOG.toString().toUpperCase();

    @Override
    public String doCommand(RunningClient rClient, RegistredClients regClients, PrintWriter writer, BufferedReader reader, Matchmaking matchmaking, String[] values) {

        if (regClients.isRegisteredClient(values[1]) && !matchmaking.isActivePlayer(values[1])) {

            if (regClients.isRegisteredClient(values[1], values[2])) {
                logger.debug("Úspěšně přihlášen client: " + values[1]);
                rClient.setClientName(values[1]);
                matchmaking.addToActiveClients(values[1]);
                regClients.getMesClients().addToMessageClients(values[1], new MessageClient(writer));
                return "LOG/" + values[1] + "/" + values[2] + "/" + regClients.getClientFromRegClientsById(regClients.getIdByName(values[1])).getElo() + ";";
            }
        }

        logger.debug("Přihlášení clienta nebylo neúspěšné: " + values[1]);
        return "LOG/0;";
    }

    @Override
    public String getName() {
        return NAME;
    }

}
