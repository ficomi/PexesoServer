package Network.Client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testy které se vztahují na třídu RegisteredClients
 */
public class RegistredClientsTest {

    /**
     * Test přidání klienta
     */
    @Test
    public void addClinetToRegClients() {
        RegistredClients regClients = new RegistredClients();
        regClients.addClinetToRegClients("pepa","pepa");
        assertTrue(regClients.isRegisteredClient("pepa"));
    }

    /**
     * Test na získání klienta
     */
    @Test
    public void getClientFromRegClientsById() {
        RegistredClients regClients = new RegistredClients();
        regClients.addClinetToRegClients("pepa","pepa");
        assertEquals("pepa",regClients.getClientFromRegClientsById(regClients.getIdByName("pepa")).getName());
    }

    /**
     * Test na nastavení ela u klienta
     */
    @Test
    public void setRegisteredClientElo() {
        RegistredClients regClients = new RegistredClients();
        regClients.addClinetToRegClients("pepa","pepa");
        regClients.getClientFromRegClientsById(regClients.getIdByName("pepa")).setElo(10);
        assertEquals(10,regClients.getClientFromRegClientsById(regClients.getIdByName("pepa")).getElo());
    }

}