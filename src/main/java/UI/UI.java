/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Propojení grafického rozhraní se serverem.
 *
 * @author Miloslav Fico
 */
public class UI {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Network NETWORK;
    private final ServerWindow serWindow;

    public UI(Network network) {
        NETWORK = network;

        serWindow = new ServerWindow(NETWORK);
        logger.debug("Start UI.");
        startUI();

    }

    private void startUI() {
        serWindow.setVisible(true);
        serWindow.displayInfo();
    }


}
