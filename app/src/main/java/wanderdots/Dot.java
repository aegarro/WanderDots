package wanderdots;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

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
        if(!containsRequiredFields(dot, requiredFields)){
            Log.d("arodr", "Dot Validation Error: Given Dot missing " + getMissingField(dot, requiredFields)) ;
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
            jdata.put(adventureString, (Object) adventureIds);
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
    //Used for sending data to server to create Adventure
    public HashMap<String, String> toHashMap(){
        HashMap<String, String> dot = super.toHashMap() ;
        dot.put(adventureString, jsonifyArray(this.adventureIds)) ;
        return dot ;
    }

    public static void addObserver(Observer observer){
        observers.add(observer) ;
    }

    public static ArrayList<Dot> getData(){
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
