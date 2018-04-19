package config;

import model.mail.Message;
import model.mail.Person;

import java.io.IOException;
import java.util.List;

/**
 * @author Marie Lemdjo
 * @author Francine Youndzo
 * 
 * Interface de la classe implémentant la configuration du protocole SMTP
*/
public interface IConfiguration {


    public List<Message> loadDataFromFile(String fileName) throws IOException;

    public List<Person> loadAddressFromFile(String fileName) throws IOException;

    public void loadPropertie(String fileName) throws IOException;

    public String getServerAddress();

    public int getServerport();

    public List<Person> getVictim();

    public List<Message> getMessage();

    public int getNumberOfGroup();

    public String getSubject();


}
