package smtp;

import model.mail.Message;
import model.prank.Prank;

import java.io.IOException;

/**
 * @author Marie Lemdjo
 * @author Francine Youndzo
 * 
 * Interface pour la classe implémentant le protocole SMTP
 */
public interface ISmtpClient {
    public void sendMessage(String serverName, int port, Prank p) throws IOException, SmtpException;
}
