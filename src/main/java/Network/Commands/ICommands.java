/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Commands;

import Network.Matchmaking.Matchmaking;
import Network.Client.RegistredClients;
import Network.Client.RunningClient;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Tato třída slouží jako interface pro jednotlivé příkazy.
 *  
 * @author Miloslav Fico
 */
public interface ICommands {
      /**
     *
     * @param rClient Thread komunikace s clientem
     * @param regClients Třída RegisteredClients
     * @param writer Writer clienta
     * @param reader Reader clienta
     * @param matchmaking Třída matchmaking
     * @param values Hodnoty ze zprávy
     * @return String který se má odeslat zpátky
     */

    public String doCommand(RunningClient rClient,RegistredClients regClients,PrintWriter writer,BufferedReader reader,Matchmaking matchmaking,String[] values);
    
    /**
     *
     * @return název příkazu
     */
    public String getName();
}
