package wanderdots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Plays two different roles (unfortunately):
 * 1. Is the data structure of an Adventure
 * 2. Statically loads Adventures on the server whenever
 * an observer attaches itself to it.
 */
public class Adventure extends Experience {

    private static final String DOTS_V_STRING = "dotsVisited";
    private static final String[] requiredFields = {DOTS_V_STRING} ;

    private static ArrayList<Observer> observers = new ArrayList<>() ;
    private static ArrayList<Adventure> data = null ;
    private static AdventureCreator loader = new AdventureCreator() ;

    private List<String> dotsVisited ;

    public Adventure(JSONObject adventure) throws org.json.JSONException{
        super(adventure) ;
        if(!containsRequiredFields(adventure, requiredFields)){
            Log.d("arodr", "Adventure Validation Error: Missing Field: " + getMissingField(adventure, requiredFields)) ;
            return ;
        }
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
            jdata.put(DOTS_V_STRING, (Object) new JSONArray(dotsVisited)) ;
            return jdata ;
        } catch(JSONException e){
            Log.d("arodr:Adventure:toJSON", e.toString()) ;
            return null ;
        }
    }

    public Map<String, String> toHashMap(){
        Map<String, String> adventure = super.toHashMap() ;
        adventure.put(DOTS_V_STRING, jsonifyArray(dotsVisited)) ;
        return adventure ;
    }

    public static void addObserver(Observer observer){
        observers.add(observer) ;
    }

    public static List<Adventure> getData(){
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

    public static void reload(){
        setError(null) ;
        data.clear() ;
        loader.reload();
    }
}
