package smtp;

import config.ConfigurationManager;
import config.IConfiguration;
import model.mail.Message;
import model.mail.Person;
import model.prank.Prank;
import model.prank.PrankGenerator;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marie Lemdjo
 * @author Francine Youndzo
 * 
 * 
 * Classe impl�mentant le protocole SMTP
 */
public class SmtpClient implements ISmtpClient{
    /**
     * adresse du serveur smtp
     */
    private InetAddress serverAddress;
    /**
     * port utilisé par le client smtp
     */
    private int serverport;

    /**
     * adresse source pour l'envoi des messages
     */
    private Person sender;
    /**
     * liste des mails recus;
     */

    private Message mail = new Message();
    private Prank prank ;
    private String message;
    /**
     * objet du mail
     */
    private String subject = "";

    /**
     * cette String va nous permettre de stocker la dernière valeur de la reponse
     * du serveur.
     */
    String lastcommande = "";

    /*
    * l'envoi se fait via la sequence suivante connect to server
    * EHLO
    * MAIL FROM
    * RCPT TO
    * DATA .....data...... QUIT
    */
    public void sendMessage(String serverName, int port, Prank p) throws SmtpException, IOException {

        serverAddress = InetAddress.getByName(serverName);
        this.prank = p;
        serverport = port;
        mail = prank.getMessage();
        message = mail.getBody();
        sender = prank.getVictimSender();
        this.subject = mail.getSubject();

        send();
    }

    protected void send() throws SmtpException, IOException {
        Socket sock = null;
        try {
            sock = new Socket(serverAddress, serverport);
            //Avec gestion de l'encodage
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));

            int returnCode = readServerResponse(in);

            if (returnCode != 250) {
                throw new SmtpException("CONNECT", returnCode, lastcommande);
            }

            sendcommand(out, "EHLO " + InetAddress.getLocalHost().getHostName(), in, 250);
            sendcommand(out, "MAIL from: " + sender.getAddr(), in, 250);

            for (int i = 0; i < prank.getVictimRecipient().size(); ++i) {
                sendcommand(out, "RCPT TO: " + prank.getVictimRecipient().get(i).getAddr(), in, 250);
            }
            sendcommand(out, "DATA ", in, 3);

            sendata(out);
            returnCode = readServerResponse(in);
            if (returnCode != 250) {
                throw new IOException("An error occured : sending data");
            }
            sendcommand(out, "Quit", in, 250);

        } catch (IOException e1) {
            if (sock != null) {
                sock.close();
            }
            throw e1;
        } catch (SmtpException e2) {
            if (sock != null) {
                sock.close();
            }
            throw e2;
        }
    }

    protected void sendcommand(PrintWriter out, String cmd, BufferedReader in, int okCode) throws SmtpException, IOException {

        out.write(cmd);
        out.write("\r\n"); //Les retours à la ligne dans le serveur
        out.flush();
        int returnCode = readServerResponse(in);

        if (returnCode != okCode) {
            throw new SmtpException(cmd, okCode, lastcommande);
        }

    }

    protected void sendata(PrintWriter out) {
            // entête du mail
            out.write("From: " + sender.getAddr() + "\r\n"); //Fim d'une commande
            out.write("TO: " + prank.getVictimRecipient().get(0).getAddr());

            for (int i = 0; i < prank.getVictimRecipient().size(); ++i) {
                out.write("," + prank.getVictimRecipient().get(i).getAddr());
            }
            out.write("\r\n");
            out.write("Cc: " + prank.getVictimRecipient().get(0).getAddr());

            for (int i = 0; i < prank.getVictimRecipient().size(); ++i) {
                out.write("," + prank.getVictimRecipient().get(i).getAddr());
            }
            out.write("\r\n"); //Les retours à la ligne dans le serveur.
            out.write("Subject: " + mail.getSubject() + "\r\n");
            out.write("\r\n");
            //-------------- fin de l'entête

            //message à envoyer
            String data = msg2data(message);

            out.write(data);

            //fin du message marquée par <CR/LF>.<CR/LF>
            out.write("\r\n.\r\n");
            out.flush();
    }
    /**
     * convertir le String au format compris par le serveur. Le format de message
     * attendu est le suivant: les lignes séparées par saut de ligne Le format
     * de données SMTP est : lignes séparées par <lt> <LF> <LF>;
     */
    protected String msg2data(String message) {
        StringBuffer buffer = new StringBuffer();
        String line;
        int start = 0;
        int end = 0;
        if (message != null) {
            do {
                end = message.indexOf('\n', start);
                if (end == -1) {
                    line = message.substring(start);
                } else {
                    line = message.substring(start, end);
                    end++;
                }

                if (line.length() > 0 && line.charAt(0) == '.') {
                    buffer.append('.');
                }

                buffer.append(line);
                if (end != -1) {
                    buffer.append("\r\n");
                }

                start = end;
            } while (end != -1);
        }
        return buffer.toString();
    }

    /**
     * Chaque ligne de la commande smtp lors de la connexion à un code. C'est avec
     * ce code qu'on vérifie si le serveur est d'accord ou pas d'initier une
     * conversation avec le client. Le but de cette methode est de retourner
     * ce code et le dernier message qui sera stocker dans String est la dernier
     * message envoyé par le serveur .
     */
    protected int readServerResponse(BufferedReader in) throws IOException {
        int code;
        boolean plusDeligne;
        String line;
        StringBuffer text = new StringBuffer();

        do {
            line = in.readLine();
           // trace("response: " + line);

            plusDeligne = (line.charAt(3) == '-');
            text.append((line.substring(4, line.length())));
        } while (plusDeligne);

        code = Integer.parseInt(line.substring(0, 3));

        lastcommande = text.toString();

        return code;

    }


    //Faire en sorte de demander le nombre de groupe de personne
    public static void main(String[] argv) throws IOException {
        //recuperation des données nécessaires pour générer un prank
        IConfiguration conf = new ConfigurationManager();

        //liste contenant les pranks
        List<Prank> pranks = new LinkedList<Prank>();
        //génération liste de panks
        PrankGenerator gPranks = new PrankGenerator(conf);
        pranks = gPranks.generatePranks();

        //nom du serveur
        String serverName = conf.getServerAddress();

        //numèro du port
        int serverPort = conf.getServerport();

        SmtpClient smtp = new SmtpClient();
        try {
            for (Prank p : pranks) {
                smtp.sendMessage(serverName, serverPort, p);
            }
        } catch (Exception e) {
            System.err.println("Error while sending: " + e.toString());
            e.printStackTrace();
        }
        System.exit(0);
    }
}

class SmtpException extends Exception {

    /**
     *
     * cree une nouvelle exception
     *
     * @param lastCmd la dernière commande envoyer avent l'erreur.
     * @param errorCode le code d'erreur retourne par le serveur.
     * @param errorMsg le message d'erreur.
     */
    public SmtpException(String lastCmd, int errorCode, String errorMsg) {
        this.lastCmd = lastCmd;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * convertir l'exception en String
     * @return String representation of Exception
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();

        buff.append("Error while executing cmd " + lastCmd + ":"
                + errorCode + "-" + errorMsg);

        return buff.toString();
    }
    String lastCmd = null;
    int errorCode = -1;
    String errorMsg = null;
}
