package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    public Group(){
        members=new ArrayList<>();
    }

    public Group(String name){
        this();
        this.name=name;
    }

    public Group(String id,String name){
        this(name);
        this.id=id;
    }

    public Group(String id,String name,List<String>members){
        this(id,name);
        this.members.addAll(members);
    }

    String id;
    String name;
    List<String> members;
}
