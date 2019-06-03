package wanderdots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Experience {

    private static final String CATEGORIES_FIELD = "categories";
    private static final String CREATOR_FIELD = "creator";
    private static final String PICTUREIDS_FIELD = "pictureIds";
    private static final String LOCATION_FIELD = "location";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String LONGITUDE_FIELD = "longitude";
    private static final String LATITUDE_FIELD = "latitude";

    private static String error ;

    static {
        error = null ;
    }

    private String id ;
    private String name ;
    private String creator ;
    private String description ;
    private List<String> categories;
    private ArrayList<String> pictureIds ;
    private Double latitude ;
    private Double longitude ;
    private String[] requiredFields = {"_id", "name", DESCRIPTION_FIELD, LOCATION_FIELD, CATEGORIES_FIELD, CREATOR_FIELD, PICTUREIDS_FIELD} ;

    public Experience(){
        this.categories = new ArrayList<>() ;
        this.pictureIds = new ArrayList<>() ;
    }

    public Experience(JSONObject experience) throws org.json.JSONException{
        instantiateFromJSON(experience);
    }

    public void instantiateFromJSON(JSONObject experience) throws org.json.JSONException{
        if(!containsRequiredFields(experience, requiredFields))
            throw new RuntimeException("Experience missing field: " + getMissingField(experience, requiredFields)) ;

        JSONArray jCategories = experience.getJSONArray(CATEGORIES_FIELD) ;
        this.categories = createStringList(jCategories) ;

        JSONArray jPictureIds = experience.getJSONArray(PICTUREIDS_FIELD) ;
        this.pictureIds = createStringList(jPictureIds);

        JSONObject location = experience.getJSONObject(LOCATION_FIELD) ;
        initializeLocation(location) ;

        this.id = location.getString("_id") ;
        this.description = experience.getString(DESCRIPTION_FIELD) ;
        this.name = experience.getString("name") ;
        this.creator = experience.getString(CREATOR_FIELD) ;
    }

    public ArrayList<String> createStringList(JSONArray categories)  throws org.json.JSONException {
        ArrayList<String> categoriesList = new ArrayList<String>() ;
        for(int i=0; i<categories.length(); i++){
            String category = categories.getString(i) ;
            categoriesList.add(category) ;
        }
        return categoriesList ;
    }

    public void initializeLocation(JSONObject location) throws org.json.JSONException {
        this.longitude = Double.parseDouble(location.getString(LONGITUDE_FIELD)) ;
        this.latitude = Double.parseDouble(location.getString(LATITUDE_FIELD)) ;
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
        Log.d("arodr", "adding picture id" + pictureId) ;
        this.pictureIds.add(pictureId) ;
    }


    public JSONObject toJSON(){
        try {
            JSONObject data = new JSONObject();
            data.put("name", name) ;
            data.put(DESCRIPTION_FIELD, description) ;
            data.put(CREATOR_FIELD, creator) ;
            data.put(CATEGORIES_FIELD, (Object) new JSONArray(categories)) ;
            data.put(PICTUREIDS_FIELD, (Object) new JSONArray(pictureIds)) ;

            JSONObject location = new JSONObject() ;
            location.put(LATITUDE_FIELD, latitude) ;
            location.put(LONGITUDE_FIELD, longitude) ;
            data.put(LOCATION_FIELD, (Object) location) ;

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

    public ArrayList<String> getPictureIds(){
        return this.pictureIds ;
    }

    protected static String jsonifyArray(ArrayList<String> arr){
        JSONArray stringArray = new JSONArray(arr) ;
        return stringArray.toString() ;
    }

    protected static JSONArray jsonifyArray(String[] arr){
        try{
            return new JSONArray(arr) ;
        }catch(JSONException e){
            Log.d("ERROR", e.toString()) ;
            return null ;
        }
    }

    public String getLocationJSON(){
        try {
            JSONObject location = new JSONObject() ;
            location.put(LATITUDE_FIELD, this.latitude) ;
            location.put(LONGITUDE_FIELD, this.longitude) ;
            return location.toString() ;
        }catch(JSONException e) {
            Log.d("Error getLocationJSON", e.toString()) ;
            return null ;
        }
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> experience = new HashMap<String, String>() ;
        experience.put("name", this.name) ;
        experience.put(CREATOR_FIELD, this.creator) ;
        experience.put(DESCRIPTION_FIELD, this.description);
        experience.put(PICTUREIDS_FIELD, jsonifyArray(this.pictureIds)) ;
        experience.put(LOCATION_FIELD, getLocationJSON() ) ;
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
