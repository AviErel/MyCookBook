package Model;

import java.util.UUID;

public class Recipe {
    public Recipe(){

    }

    public Recipe(UUID id, String header, String course, String diet, String uri, String description, String userId, Boolean publicated){
        this._id = id;
        this._header = header;
        this._course = course;
        this._diet = diet;
        this._uri = uri;
        this._description = description;
        this._userId = userId;
        this._publicated = publicated;
    }

    private UUID _id;
    private String _header;
    private String _course;
    private String _diet;
    private String _uri;
    private String _description;
    private String _userId;
    private Boolean _publicated;

    public void SetId(UUID id){this._id = id;}
    public UUID GetId(){return this._id;}

    public void SetHeader(String header){this._header = header;}
    public String GetHeader(){return this._header;}

    public void SetCourse(String course){this._course = course;}
    public String GetCourse(){return this._course;}

    public void SetDiet(String diet){this._diet = diet;}
    public String GetDiet(){return this._diet;}

    public void SetUri(String uri){this._uri = uri;}
    public String GetUri(){return this._uri;}

    public void SetDescription(String description){this._description = description;}
    public String GetDescription(){return this._description;}

    public void SetUserId(String userId){this._userId = userId;}
    public String GetUserId(){return this._userId;}

    public void SetPublicated(Boolean publicated){this._publicated = publicated;}
    public Boolean GetPublicated(){return this._publicated;}
}
