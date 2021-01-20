/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pexesoserver;

import Network.Network;
import UI.UI;

/**
 * Main Třída.
 *
 * @author Miloslav Fico
 */
public class PexesoServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Network network = new Network();
        Thread NetworkThread = new Thread(network);
        NetworkThread.start();

        UI ui = new UI(network);
    }


}
