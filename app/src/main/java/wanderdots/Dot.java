package wanderdots;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Plays two different roles (unfortunately):
 * 1. Is the data structure of an Dot
 * 2. Statically loads Dots on the server whenever
 * an observer attaches itself to it.
 */
public class Dot extends Experience  {

    private static ArrayList<Observer> observers = new ArrayList<>();
    private static ArrayList<Dot> data = null ;
    private static final String LOGTAG = "arodr";
    private static DotCreator loader = new DotCreator() ;
    private static String adventureString = "adventures";

    private ArrayList<String> adventureIds;
    private String[] requiredFields = {adventureString} ;

    public Dot(){
        super() ;
        this.adventureIds = new ArrayList<>() ;
    }

    public Dot(JSONObject dot) throws org.json.JSONException{
        super(dot) ;
        if(!containsRequiredFields(dot, this.requiredFields)){
            Log.d(LOGTAG, "Dot Validation Error: Given Dot missing " + getMissingField(dot, this.requiredFields)) ;
            return ;
        }
        instantiateFromJSON(dot) ;
    }

    @Override
    public void instantiateFromJSON(JSONObject dot) throws org.json.JSONException{
        super.instantiateFromJSON(dot) ;
        createStringList(dot.getJSONArray(adventureString)) ;
    }

    public JSONObject toJSON(){
        try {
            JSONObject jdata = super.toJSON() ;
            jdata.put(adventureString, (Object) this.adventureIds);
            return jdata ;
        }catch(JSONException e) {
            Log.d(LOGTAG, e.toString()) ;
            return null ;
        }
    }

    public String toString(){
                                   return this.toJSON().toString() ;
                                                                    }

    @Override
    //Used for sending data to server to create Adventure
    public Map<String, String> toHashMap(){
        Map<String, String> dot = super.toHashMap() ;
        dot.put(adventureString, jsonifyArray(this.adventureIds)) ;
        return dot ;
    }

    public static void addObserver(Observer observer){
        observers.add(observer) ;
    }

    public static List<Dot> getData(){
        return data ;
    }

    public static void dataFinishedLoading(){
        if(loader.hasError()){
            Log.d(LOGTAG, "Dot received error while loading data") ;
            Log.d(LOGTAG, loader.getError()) ;
            setError(loader.getError()) ;
            return ;
        }

        data = loader.getData() ;
        notifyObservers();
    }

    public static void notifyObservers(){
        for(Observer observer : observers)
            observer.subscriberHasChanged("update");
    }

    public static void reload(){
        Log.d(LOGTAG, "Reloading Dot loader") ;
        setError(null) ;
        data.clear() ;
        loader.reload();
    }
}
