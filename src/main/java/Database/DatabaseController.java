package Database;

import Database.Entities.Client;
import Security.Passwords;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * Třída která se stará o komunikaci s databází
 */
public class DatabaseController {

    private Session session;
    private List<Client> clients;
    private SessionFactory sFactory;
    private Configuration cfg;               // cfg v resources

    /**
     *  SQL Query na vytvoření tabulky která slouži k ukládání klientů
     */
    private final String QUERY_CRATE_TABLE = "CREATE TABLE IF NOT EXISTS clients ("
            + "id INT unsigned NOT NULL AUTO_INCREMENT,"
            + "name VARCHAR(25)  NOT NULL,"
            + "passwd BINARY(" + Passwords.getKEY_LENGTH()/8 + ") NOT NULL,"
            + "salt BINARY(" + Passwords.getSALT_LENGTH() + ") NOT NULL,"
            + "elo SMALLINT unsigned NOT NULL,"
            + "PRIMARY KEY (id)"
            + ")";

    public DatabaseController() {

        connectToDatabase();
        updateData();
    }

    /**
     * Metoda která vrací kolekci dat z tabulky
     * @param type
     * @param <T>
     * @return
     * @throws IllegalArgumentException
     */
    public <T> List<T> findAllDataFromTable(Class<T> type) throws IllegalArgumentException {
        session = sFactory.openSession();
        Criteria cr = session.createCriteria(type);
        List result = cr.list();
        session.close();
        return result;
    }

    /**
     * Updatování klientů v aplikaci
     */
    public void updateData() {
        clients = findAllDataFromTable(Client.class);
        for (Client c : clients) {
            System.out.println(c.getName()+" elo: "+c.getElo());
            
        }
    }

    /**
     * Metoda sloužící k připojeni k databázi
     * @throws ExceptionInInitializerError
     */
    private void connectToDatabase() throws ExceptionInInitializerError {
        cfg = new Configuration();
        sFactory = cfg.configure().buildSessionFactory();
        session = sFactory.openSession();
        session.beginTransaction();
        session.createSQLQuery(QUERY_CRATE_TABLE).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Přidání klienta do databáze
     * @param c
     */
    public void addClient(Client c) {

        session = sFactory.openSession();
        session.beginTransaction();
        session.persist(c);
        session.getTransaction().commit();
        session.close();
        updateData();
    }

    /**
     * Update klienta v databázi
     * @param c
     * @param elo
     */
    public void updateClientsElo(Client c,int elo){
        session = sFactory.openSession();
        session.beginTransaction();
        c.setElo(elo);
        session.update(c);
        session.getTransaction().commit();
        session.close();
        updateData();
    
    }

    public List<Client> getClients() {
        return clients;
    }

}
