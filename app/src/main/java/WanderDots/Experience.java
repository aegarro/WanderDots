package WanderDots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Experience<T> {

    private static String error ;
    protected static ArrayList<Observer> observers ;

    static {
        error = null ;
        observers = new ArrayList<>() ;
    }

    public String id ;
    public String name ;
    public String creator ;
    public String description ;
    public ArrayList<String> categories;
    public ArrayList<String> pictureIds ;
    public Double latitude ;
    public Double longitude ;
    private String[] requiredFields = {"_id", "name", "description", "location", "categories", "creator", "pictureIds"} ;

    public Experience(){
        this.categories = new ArrayList<String>() ;
        this.pictureIds = new ArrayList<String>() ;
    }

    public Experience(JSONObject experience) throws org.json.JSONException{
        if(!containsRequiredFields(experience, requiredFields))
            throw new RuntimeException("Experience missing field: " + getMissingField(experience, requiredFields)) ;
        instantiateFromJSON(experience);
    }

    public void instantiateFromJSON(JSONObject experience) throws org.json.JSONException{
        JSONArray categories = experience.getJSONArray("categories") ;
        this.categories = createStringList(categories) ;

        JSONArray pictureIds = experience.getJSONArray("pictureIds") ;
        this.pictureIds = createStringList(pictureIds);

        JSONObject location = experience.getJSONObject("location") ;
        initializeLocation(location) ;

        this.id = location.getString("_id") ;
        this.description = experience.getString("description") ;
        this.name = experience.getString("name") ;
        this.creator = experience.getString("creator") ;
    }

    public ArrayList<String> createStringList(JSONArray categories)  throws org.json.JSONException {
        ArrayList<String> categoriesList = new ArrayList<String>() ;
        for(int i=0; i<categories.length(); i++)
            categoriesList.add(categories.getString(i)) ;
        return categoriesList ;
    }

    public void initializeLocation(JSONObject location) throws org.json.JSONException {
        this.longitude = Double.parseDouble(location.getString("longitude")) ;
        this.latitude = Double.parseDouble(location.getString("latitude")) ;
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
            data.put("description", description) ;
            data.put("creator", creator) ;
            data.put("categories", (Object) categories) ;
            data.put("pictureIds", (Object) pictureIds) ;

            JSONObject location = new JSONObject() ;
            location.put("latitude", latitude) ;
            location.put("longitude", longitude) ;
            data.put("location", (Object) location) ;

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
            location.put("latitude", this.latitude) ;
            location.put("longitude", this.longitude) ;
            return location.toString() ;
        }catch(JSONException e) {
            Log.d("Error getLocationJSON", e.toString()) ;
            return null ;
        }
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> experience = new HashMap<String, String>() ;
        experience.put("name", this.name) ;
        experience.put("creator", this.creator) ;
        experience.put("description", this.description);
        experience.put("pictureIds", jsonifyArray(this.pictureIds)) ;
        experience.put("location", getLocationJSON() ) ;
        return experience ;
    }

    /*
            STATIC METHODS FOR LOADING DATA
     */
    protected static void setError(String string){
        error = string ;
    }

    public static String getError(){
        return error;
    }

    public static boolean hasError(){
        return error != null ;
    }

    public static void notifyObservers(){
        for(Observer observer : observers)
            observer.subscriberHasChanged("update");
    }
}
