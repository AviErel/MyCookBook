package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recipe implements Serializable {
    public Recipe(){
        this.imagesNames = new ArrayList<>();
    }

    public Recipe(String id, String header, String course, String diet, String uri, String description,
                  String userId,String[] tags,String ingredients,String preparations, Boolean publicated){
        this.id = id;
        this.header = header;
        this.course = course;
        this.diet = diet;
        this.uri = uri;
        this.description = description;
        this.userId = userId;
        this.tags=tags;
        this.ingredients=ingredients;
        this.preparations=preparations;
        this.publicated = publicated;
        this.imagesNames = new ArrayList<>();
    }

    public Recipe(String id, String header, String course, String diet, String uri, String description, String userId,String[] tags,String ingredients,String preparations, Boolean publicated, List<String>names){
        this.id = id;
        this.header = header;
        this.course = course;
        this.diet = diet;
        this.uri = uri;
        this.description = description;
        this.userId = userId;
        this.tags=tags;
        this.publicated = publicated;
        this.imagesNames = new ArrayList<>();

        if(names != null)
            this.imagesNames.addAll(names);
    }

    String id;
    String header;
    String course;
    String diet;
    String uri;
    String description;
    String userId;
    Boolean publicated;
    String[] tags;
    String ingredients;
    String preparations;
    List<String> imagesNames;

    public void SetId(String id){this.id = id;}
    public String GetId(){return this.id;}

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

    public void SetTags(String[] tags){this.tags=tags;}
    public String[] GetTags(){return tags;}

    public void SetIngredients(String ingredients){this.ingredients=ingredients;}
    public String GetIngredients(){return ingredients;}

    public void SetPreparetions(String preps){this.preparations=preps;}
    public String GetPreparetions(){return preparations;}

    public void SetPublicated(Boolean publicated){this.publicated = publicated;}
    public Boolean GetPublicated(){return this.publicated;}

    public void SetImagesNames(List<String> names){
        this.imagesNames.clear();
        this.imagesNames.addAll(names);
    }
    public List<String> GetImagesNames(){return this.imagesNames;}
}
