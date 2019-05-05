package WanderDots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Experience {

    protected String id ;
    protected String name ;
    protected String creator ;
    protected String description ;
    protected String[] categories ;
    protected String[] pictureIds ;
    protected Double latitude ;
    protected Double longitude ;
    private String[] requiredFields = {"_id", "name", "description", "location", "categories", "creator", "pictureIds"} ;


    public Experience(String name, double latitude, double longitude){
        this.name = name ;
        this.latitude = latitude ;
        this.longitude = longitude ;
    }

    public Experience(JSONObject experience){
        if(!containsRequiredFields(experience, requiredFields))
            throw new RuntimeException("Experience missing field: " + getMissingField(experience, requiredFields)) ;

        try {
            JSONArray categories = experience.getJSONArray("categories") ;
            this.categories = initializeStringArray(categories);

            JSONArray pictureIds = experience.getJSONArray("pictureIds") ;
            this.pictureIds = initializeStringArray(pictureIds);

            JSONObject location = experience.getJSONObject("location") ;
            initializeLocation(location) ;

            this.id = location.getString("_id") ;
            this.description = experience.getString("description") ;
            this.name = experience.getString("name") ;
            this.creator = experience.getString("creator") ;
        } catch(org.json.JSONException e){
            Log.e("Dot Validation Error", "Dot: " + e.toString());
        }
    }

    protected String[] initializeStringArray(JSONArray categories)  throws org.json.JSONException {
        String[] categoriesList = new String[categories.length()] ;
        for(int i=0; i<categories.length(); i++)
            categoriesList[i] = categories.getString(i) ;
        return categoriesList ;
    }

    protected void initializeLocation(JSONObject location) throws org.json.JSONException {
        this.longitude = Double.parseDouble(location.getString("longitude")) ;
        this.latitude = Double.parseDouble(location.getString("latitude")) ;
    }

    protected boolean containsRequiredFields(JSONObject object, String[] requiredFields){
        boolean result = true ;
        for(String requiredField : requiredFields)
            result = result && object.has(requiredField) ;
        return result ;
    }

    protected String getMissingField(JSONObject object, String[] requiredFields){
        for(String requiredField : requiredFields)
            if(!object.has(requiredField))
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
