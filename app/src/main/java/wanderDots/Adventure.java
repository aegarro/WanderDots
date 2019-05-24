package wanderDots;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Adventure extends Experience {

    private static ArrayList<Adventure> data ;
    private static AdventureLoader loader ;

    private ArrayList<String> dotsVisited ;
    private String[] requiredFields = {"dotsVisited"} ;
    public String dotsVString = "dotsVisited";

    static {
        data = new ArrayList<>() ;
        loader = new AdventureLoader() ;
    }

    public Adventure(JSONObject adventure) throws org.json.JSONException{
        super(adventure) ;
        if(!containsRequiredFields(adventure, requiredFields))
            throw new RuntimeException("Adventure Validation Error: Missing Field: " + getMissingField(adventure, requiredFields)) ;
        instantiateFromJSON(adventure);
    }

    /*
        JSON and String Representations
     */
    @Override
    public void instantiateFromJSON(JSONObject adventure) throws org.json.JSONException {
        super.instantiateFromJSON(adventure);
        this.dotsVisited = createStringList(adventure.getJSONArray(dotsVString)) ;
    }

    public String toString(){
        return this.toJSON().toString() ;
    }

    public JSONObject toJSON(){
        try {
            JSONObject data = super.toJSON() ;
            data.put(dotsVString, (Object) dotsVisited) ;
            return data ;
        } catch(JSONException e){
            Log.d("arodr:Adventure:toJSON", e.toString()) ;
            return null ;
        }
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> adventure = super.toHashMap() ;
        adventure.put(dotsVString, jsonifyArray(dotsVisited)) ;
        return adventure ;
    }

    //DO NOT REMOVE: This method triggers the static initializer of this class
    //Beginning the loading process, without this Experience parent class recieves a null
    //Loader object
    public static void addObserver(Observer observer){
        observers.add(observer) ;
    }

    public static ArrayList<Adventure> getData(){
        return data ;
    }

    public static void dataFinishedLoading(){
        if(loader.hasError())
            setError(loader.getError()) ;
        else
            data = loader.getData() ;
        notifyObservers();
    }

    public static void reload(){
        loader.reload();
    }
}
