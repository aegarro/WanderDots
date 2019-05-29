package wanderDots;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class dot extends experience {

    private static ArrayList<observer> observers = new ArrayList<>();
    private static ArrayList<dot> data = null ;
    private static dotLoader loader = new dotLoader() ;
    private static String adventure_string = "adventures";

    private ArrayList<String> adventureIds;
    private String[] requiredFields = {adventure_string} ;

    public dot(){
        super() ;
        this.adventureIds = new ArrayList<>() ;
    }

    public dot(JSONObject dot) throws org.json.JSONException{
        super(dot) ;
        if(!containsRequiredFields(dot, requiredFields))
            throw new RuntimeException("dot Validation Error: Given dot missing " + getMissingField(dot, requiredFields)) ;
        instantiateFromJSON(dot) ;
    }

    @Override
    public void instantiateFromJSON(JSONObject dot) throws org.json.JSONException{
        super.instantiateFromJSON(dot) ;
        createStringList(dot.getJSONArray(adventure_string)) ;
    }

    public JSONObject toJSON(){
        try {
            JSONObject jdata = super.toJSON() ;
            jdata.put(adventure_string, (Object) adventureIds);
            return jdata ;
        }catch(JSONException e) {
            Log.d("arodr: (error) toJSON", e.toString()) ;
            return null ;
        }
    }

    public String toString(){
                                   return this.toJSON().toString() ;
                                                                    }

    @Override
    //Used for sending data to server to create adventure
    public HashMap<String, String> toHashMap(){
        HashMap<String, String> dot = super.toHashMap() ;
        dot.put(adventure_string, jsonifyArray(this.adventureIds)) ;
        return dot ;
    }

    //DO NOT REMOVE: This method triggers the static initializer of this class
    //Beginning the loading process, without this experience parent class recieves a null
    //loader object
    public static void addObserver(observer observer){
        observers.add(observer) ;
    }

    public static ArrayList<dot> getData(){
        return data ;
    }

    public static void dataFinishedLoading(){
        if(loader.hasError())
            setError(loader.getError()) ;
        else
            data = loader.getData() ;
        notifyObservers();
    }

    public static void notifyObservers(){
        for(wanderDots.observer observer : observers)
            observer.subscriberHasChanged("update");
    }

    public static void reload(){
        loader.reload();
    }
}
