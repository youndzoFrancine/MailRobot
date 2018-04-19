package model.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marie Lemdjo
 * @author Francine Youndzo
 * 
 * Cette classe comprend la logique métier d'un message SMTP
 * Un message est constitué d'une entête et d'un corps
 * L'entête comprend l'émetteur, le destinataire et un champ CC
*/

public class Message {

    private String from;
    private String address;
    private final List<String> to = new ArrayList<String>();
    private final List<String> cc = new ArrayList<String>();
    String Subject;
    String body ;
    String realName;

    public String getFrom(){
        return this.from;
    }

    public String getBody(){
        return this.body;
    }

    public List<String> getTo(){
        return to;
    }

    public  String getSubject(){
        return Subject;
    }

    public void setTo(List<String> s){
        this.to.addAll(s);
    }

    public void SetFrom(String from){
        this.from = from;
    }

    public void setCc(List<String> s){
        this.cc.addAll(s);
    }

    public void setbody(String body){
        this.body = body;
    }

    public void setSubject(String subject){
        this.Subject = subject;
    }

    public String getAddress(){
        return address;
    }

    public void setAdress(int index){
        this.address = to.get(index);
    }

    public void setRealName(String realName){
        this.realName = realName;
    }

    public String getRealName(){
        return realName;
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer();

        if(realName != null){
            buffer.append("\"" + realName + "\"" );
        }
        if(address != null){
            buffer.append("<" + address + ">");
        }
        else{
            return null;
        }
        return buffer.toString();
    }
}
