package WanderDots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Dot {

    private String id;
    private String name ;
    private String description;
    private String creator ;

    private String[] categories ;
    private String[] adventureIDs ;
    private double latitude;
    private double longitude;
    private String[] pictureIds;

    private String[] requiredFields = {"_id", "name", "description", "location", "categories", "adventures", "creator", "pictureIds"} ;

    public Dot(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Dot(JSONObject dot){
        if(!isValidDot(dot))
            throw new RuntimeException("Dot Validation Error: Given dot missing " + getMissingField(dot)) ;
        try {
            JSONArray categories = dot.getJSONArray("categories") ;
            this.categories = initializeStringArray(categories);

            JSONArray pictureIds = dot.getJSONArray("pictureIds") ;
            this.pictureIds = initializeStringArray(pictureIds);

            JSONArray adventures = dot.getJSONArray("adventures") ;
            this.adventureIDs = initializeStringArray(adventures);

            JSONObject location = dot.getJSONObject("location") ;
            initializeLocation(location) ;

            this.id = location.getString("_id") ;
            this.description = dot.getString("description") ;
            this.name = dot.getString("name") ;
            this.creator = dot.getString("creator") ;
        } catch(org.json.JSONException e){
            Log.e("Dot Validation Error", "Dot: " + e.toString());
        }
    }

    private String[] initializeStringArray(JSONArray categories)  throws org.json.JSONException {
        String[] categoriesList = new String[categories.length()] ;
        for(int i=0; i<categories.length(); i++)
            categoriesList[i] = categories.getString(i) ;
        return categoriesList ;
    }

    private void initializeLocation(JSONObject location) throws org.json.JSONException {
        this.longitude = Double.parseDouble(location.getString("longitude")) ;
        this.latitude = Double.parseDouble(location.getString("latitude")) ;
    }

    private boolean isValidDot(JSONObject dot){
        boolean result = true ;
        for(String requiredField : requiredFields)
            result = result && dot.has(requiredField) ;
        return result ;
    }

    private String getMissingField(JSONObject dot){
        for(String requiredField : requiredFields)
            if(!dot.has(requiredField))
                return requiredField ;
        return "All fields valid" ;
    }

    public String getId() {
        return id ;
    }

    public String getCreator(){
        return this.creator ;
    }


    public String getName() {
        return this.name ;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String[] getPhotosIds() {
        return pictureIds;
    }
}
