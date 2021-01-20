package Database.Entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entita Client
 */
@Entity
@Table(name = "clients")
public class Client implements Serializable {

    /**
     *  ID klienta
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Název klienta
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Heslo ukládané jako pole 64 bajtů
     */
    @Column(name = "passwd", nullable = false, columnDefinition = "BINARY(64)")
    private byte[] passwd;


    /**
     * Sůl k heslu uložená jako 16 bajtů
     */
    @Column(name = "salt", nullable = false, columnDefinition = "BINARY(16)")
    private byte[] salt;

    /**
     * Elo klienta
     */
    @Column(name = "elo", nullable = false, columnDefinition = "SMALLINT")
    private int elo;

    public Client(byte[] passwd, byte[] salt, String name) {
        this.name = name;
        this.salt = salt;
        this.passwd = passwd;
    }

    public Client() {
    }
    
    

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getPasswd() {
        return passwd;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getElo() {
        return elo;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setName(String name) {
        this.name = name;
    }


    
    

}
