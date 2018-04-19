package config;

import model.mail.Person;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import model.mail.Message;
import model.mail.Person;

/**
 * @author Marie Lemdjo
 * @author Francine Youndzo
 * 
 * Un fichier de configuration ont été fait pour définir les caractéristiques du
 * serveur et des informations sur les victimes, l'émetteur et les messages
 * Cette classe permet d'utiliser les données de ce fichier de configuration
 * dans le projet
 * 
*/
public class ConfigurationManager implements IConfiguration {
    private String smtpServerAddress;
    private int serverport;
    private final List<Person> victimes;
    private final List<Message> messages;
    private int numberOfGroup;
    private String subject = null;

    public ConfigurationManager() throws IOException {
        victimes = loadAddressFromFile("victims.utf8");
        messages = loadDataFromFile("message.utf8");
        loadPropertie("config.properties");

    }

    public void loadPropertie(String fileName) throws IOException {
        FileInputStream f = new FileInputStream(fileName);
        Properties properpties = new Properties();
        properpties.load(f);

        this.smtpServerAddress = properpties.getProperty("smtpserverAddress");
        this.serverport = Integer.parseInt(properpties.getProperty("smtpServerport"));
        this.numberOfGroup = Integer.parseInt(properpties.getProperty("numberOfGroup"));
        this.subject = properpties.getProperty("Subject");
    }

    public List<Message> loadDataFromFile(String fileName) throws IOException {
        List<Message> resultat;
        try (FileInputStream f = new FileInputStream(fileName)) {
            InputStreamReader fls = new InputStreamReader(f, "UTF-8");
            try (BufferedReader reader = new BufferedReader(fls)) {
                resultat = new LinkedList<>();
                String line = reader.readLine();
                while (line != null) {
                    Message m = new Message();
                    StringBuilder body = new StringBuilder();
                    while ((line != null) && (!line.equals("=="))) {

                        if (line.indexOf("subject") != -1) {
                            String str[] = line.split(":");
                            m.setSubject(str[1]);
                        } else {
                            body.append(line);
                            body.append("\r\n");
                        }
                        m.setbody(body.toString());
                        line = reader.readLine();
                    }

                    resultat.add(m);

                    line = reader.readLine();
                }
                return resultat;
            }
        }
    }

    public List<Person> loadAddressFromFile(String fileName) throws IOException {
        List<Person> result;
        String tmp = null;

        try (FileInputStream f = new FileInputStream(fileName)) {
            InputStreamReader fls = new InputStreamReader(f, "UTF-8");

            try (BufferedReader reader = new BufferedReader(fls)) {
                result = new LinkedList<Person>();
                String address = reader.readLine();
                while (address != null) {
                    String str[] = address.split("@");
                    str = str[0].split("\\.");


                    if (str.length == 2) {
                        tmp = str[1];
                    }
                    result.add(new Person(address, str[0], tmp));
                    address = reader.readLine();
                }
            }
        }
        return result;
    }
    public String getServerAddress() {
        return smtpServerAddress;
    }
    public int getServerport() {
        return serverport;
    }
    public List<Person> getVictim() {
        return victimes;
    }
    public int getNumberOfGroup() {
        return numberOfGroup;
    }
    public List<Message> getMessage() {
        return messages;
    }
    public String getSubject() {
        return subject;
    }

}
