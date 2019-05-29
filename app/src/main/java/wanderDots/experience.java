package wanderDots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class experience {

    private static final String CATEGORIES = "categories";
    private static final String CREATOR = "creator";
    private static final String PICTUREIDS = "pictureIds";
    private static final String LOCATION = "location";
    private static final String DESCRIPTION = "description";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";

    private static String error ;

    static {
        error = null ;
    }

    public String id ;
    public String name ;
    public String creator ;
    public String description ;
    public ArrayList<String> categories;
    public ArrayList<String> pictureIds ;
    public Double latitude ;
    public Double longitude ;
    private String[] requiredFields = {"_id", "name", DESCRIPTION, LOCATION, CATEGORIES, CREATOR, PICTUREIDS} ;

    public experience(){
        this.categories = new ArrayList<String>() ;
        this.pictureIds = new ArrayList<String>() ;
    }

    public experience(JSONObject experience) throws org.json.JSONException{
        Log.d("arodr", "creating experience ... ")  ;
        instantiateFromJSON(experience);
    }

    public void instantiateFromJSON(JSONObject experience) throws org.json.JSONException{
        if(!containsRequiredFields(experience, requiredFields))
            throw new RuntimeException("experience missing field: " + getMissingField(experience, requiredFields)) ;

        JSONArray categories = experience.getJSONArray(CATEGORIES) ;
        this.categories = createStringList(categories) ;

        JSONArray pictureIds = experience.getJSONArray(PICTUREIDS) ;
        this.pictureIds = createStringList(pictureIds);

        JSONObject location = experience.getJSONObject(LOCATION) ;
        initializeLocation(location) ;

        this.id = location.getString("_id") ;

        this.description = experience.getString(DESCRIPTION) ;

        this.name = experience.getString("name") ;

        this.creator = experience.getString(CREATOR) ;
    }

    public ArrayList<String> createStringList(JSONArray categories)  throws org.json.JSONException {
        ArrayList<String> categoriesList = new ArrayList<String>() ;
        for(int i=0; i<categories.length(); i++)
            categoriesList.add(categories.getString(i)) ;
        return categoriesList ;
    }

    public void initializeLocation(JSONObject location) throws org.json.JSONException {
        this.longitude = Double.parseDouble(location.getString(LONGITUDE)) ;
        this.latitude = Double.parseDouble(location.getString(LATITUDE)) ;
    }

    public boolean containsRequiredFields(JSONObject object, String[] requiredFields){
        boolean result = true ;
        for(String requiredField : requiredFields)
            result = result && object.has(requiredField) ;
        return result ;
    }

    public String getMissingField(JSONObject object, String[] requiredFields){
        for(String requiredField : requiredFields)
            if(!object.has(requiredField))
                return requiredField ;
        return "All fields valid" ;
    }

    public void addCategory(String category){
        this.categories.add(category) ;
    }

    public void addPictureId(String pictureId){
        this.pictureIds.add(pictureId) ;
    }


    public JSONObject toJSON(){
        try {
            JSONObject data = new JSONObject();
            data.put("name", name) ;
            data.put(DESCRIPTION, description) ;
            data.put(CREATOR, creator) ;
            data.put(CATEGORIES, (Object) categories) ;
            data.put(PICTUREIDS, (Object) pictureIds) ;

            JSONObject location = new JSONObject() ;
            location.put(LATITUDE, latitude) ;
            location.put(LONGITUDE, longitude) ;
            data.put(LOCATION, (Object) location) ;

            return data ;
        }catch(JSONException e) {
            Log.d("arodr: ", "(error):" + e.toString()) ;
            return null ;
        }
    }

    public String toString() {
        return this.toJSON().toString() ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static String jsonifyArray(ArrayList<String> arr){
        JSONArray stringArray = new JSONArray(arr) ;
        return stringArray.toString() ;
    }

    public static String jsonifyArray(String[] arr){
        try{
            JSONArray stringArray = new JSONArray(arr) ;
            return stringArray.toString() ;
        }catch(JSONException e){
            Log.d("ERROR", e.toString()) ;
            return null ;
        }
    }

    public String getLocationJSON(){
        try {
            JSONObject location = new JSONObject() ;
            location.put(LATITUDE, this.latitude) ;
            location.put(LONGITUDE, this.longitude) ;
            return location.toString() ;
        }catch(JSONException e) {
            Log.d("Error getLocationJSON", e.toString()) ;
            return null ;
        }
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> experience = new HashMap<String, String>() ;
        experience.put("name", this.name) ;
        experience.put(CREATOR, this.creator) ;
        experience.put(DESCRIPTION, this.description);
        experience.put(PICTUREIDS, jsonifyArray(this.pictureIds)) ;
        experience.put(LOCATION, getLocationJSON() ) ;
        return experience ;
    }

    protected static void setError(String string){
        error = string ;
    }

    public static String getError(){
        return error;
    }

    public static boolean hasError(){
        return error != null ;
    }

}
