package wanderDots;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class adventure extends experience {

    private final static String[] requiredFields = {"dotsVisited"} ;
    private final static String dotsVString = "dotsVisited";

    private static ArrayList<observer> observers = new ArrayList<>() ;
    private static ArrayList<adventure> data = null ;
    private static adventureLoader loader = new adventureLoader() ;

    private ArrayList<String> dotsVisited ;

    public adventure(JSONObject adventure) throws org.json.JSONException{
        super(adventure) ;
        if(!containsRequiredFields(adventure, requiredFields))
            throw new RuntimeException("adventure Validation Error: Missing Field: " + getMissingField(adventure, requiredFields)) ;
        instantiateFromJSON(adventure);
    }

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
            Log.d("arodr:adventure:toJSON", e.toString()) ;
            return null ;
        }
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> adventure = super.toHashMap() ;
        adventure.put(dotsVString, jsonifyArray(dotsVisited)) ;
        return adventure ;
    }

    public static void addObserver(observer observer){
        observers.add(observer) ;
    }

    public static ArrayList<adventure> getData(){
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
}
