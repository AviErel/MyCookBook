package Model;

import java.io.Serializable;

public class User implements Serializable {
    public User(){ }

    public User(String id, String languagePreference){
        this.id = id;
        this.languagePreference = languagePreference;
    }

    String id;
    String languagePreference;

    public String GetId(){return this.id;}
    public String GetLanguagePreference(){return this.languagePreference;}
    public void SetLanguagePreference(String languagePreference){this.languagePreference = languagePreference;}
}
