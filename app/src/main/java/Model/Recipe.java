package Model;

import java.util.UUID;

public class Recipe {
    public Recipe(){

    }

    public Recipe(UUID id, String header, String course, String diet, String uri, String description, String userId, Boolean publicated){
        this.id = id;
        this.header = header;
        this.course = course;
        this.diet = diet;
        this.uri = uri;
        this.description = description;
        this.userId = userId;
        this.publicated = publicated;
    }

    UUID id;
    String header;
    String course;
    String diet;
    String uri;
    String description;
    String userId;
    Boolean publicated;

    public void SetId(UUID id){this.id = id;}
    public UUID GetId(){return this.id;}

    public void SetHeader(String header){this.header = header;}
    public String GetHeader(){return this.header;}

    public void SetCourse(String course){this.course = course;}
    public String GetCourse(){return this.course;}

    public void SetDiet(String diet){this.diet = diet;}
    public String GetDiet(){return this.diet;}

    public void SetUri(String uri){this.uri = uri;}
    public String GetUri(){return this.uri;}

    public void SetDescription(String description){this.description = description;}
    public String GetDescription(){return this.description;}

    public void SetUserId(String userId){this.userId = userId;}
    public String GetUserId(){return this.userId;}

    public void SetPublicated(Boolean publicated){this.publicated = publicated;}
    public Boolean GetPublicated(){return this.publicated;}
}
