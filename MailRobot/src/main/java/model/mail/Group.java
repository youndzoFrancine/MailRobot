package model.mail;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Marie Lemdjo
 * @author Francine Youndzo
 * 
 * Un groupe est constitué d'un ensemble de personnes
 */
public class Group {
    private  List<Person> members = new LinkedList<Person>();

    public void addMember(Person p){
        members.add(p);
    }

    public List<Person> getmembers(){
        return members;
    }
}
