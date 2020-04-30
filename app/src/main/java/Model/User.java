package Model;

import java.io.Serializable;

public class User implements Serializable {
    public User(){ }

    public User(String id, String languagePreference){
        this.id = id;
        this.languagePreference = languagePreference;
    }

    public User(String id,String name, String languagePreference){
        this.id = id;
        this.languagePreference = languagePreference;
        this.name=name;
    }

    String id;
    String languagePreference;
    String name;

    public String GetId(){return this.id;}
    public String GetLanguagePreference(){return this.languagePreference;}
    public void SetLanguagePreference(String languagePreference){this.languagePreference = languagePreference;}
    public String GetName(){return name;}
    public void SetName(String name){this.name=name;}
}
