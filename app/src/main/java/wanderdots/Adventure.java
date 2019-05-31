package wanderdots;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Adventure extends Experience {

    private final static String DOTS_V_STRING = "dotsVisited";
    private final static String[] requiredFields = {DOTS_V_STRING} ;

    private static ArrayList<Observer> observers = new ArrayList<>() ;
    private static ArrayList<Adventure> data = null ;
    private static AdventureLoader loader = new AdventureLoader() ;

    private ArrayList<String> dotsVisited ;

    public Adventure(JSONObject adventure) throws org.json.JSONException{
        super(adventure) ;
        if(!containsRequiredFields(adventure, requiredFields))
            throw new RuntimeException("Adventure Validation Error: Missing Field: " + getMissingField(adventure, requiredFields)) ;
        instantiateFromJSON(adventure);
    }

    @Override
    public void instantiateFromJSON(JSONObject adventure) throws org.json.JSONException {
        super.instantiateFromJSON(adventure);
        this.dotsVisited = createStringList(adventure.getJSONArray(DOTS_V_STRING)) ;
    }

    public String toString(){
        return this.toJSON().toString() ;
    }

    public JSONObject toJSON(){
        try {
            JSONObject jdata = super.toJSON() ;
            jdata.put(DOTS_V_STRING, (Object) dotsVisited) ;
            return jdata ;
        } catch(JSONException e){
            Log.d("arodr:Adventure:toJSON", e.toString()) ;
            return null ;
        }
    }

    public HashMap<String, String> toHashMap(){
        HashMap<String, String> adventure = super.toHashMap() ;
        adventure.put(DOTS_V_STRING, jsonifyArray(dotsVisited)) ;
        return adventure ;
    }

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

    public static void notifyObservers(){
        for(Observer observer : observers)
            observer.subscriberHasChanged("update");
    }
}
