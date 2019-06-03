package wanderdots;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Dot extends Experience  {

    private static ArrayList<Observer> Observers = new ArrayList<>();
    private static ArrayList<Dot> data = null ;
    private static DotCreator loader = new DotCreator() ;
    private static String adventure_string = "adventures";

    private ArrayList<String> adventureIds;
    private String[] requiredFields = {adventure_string} ;

    public Dot(){
        super() ;
        this.adventureIds = new ArrayList<>() ;
    }

    public Dot(JSONObject dot) throws org.json.JSONException{
        super(dot) ;
        if(!containsRequiredFields(dot, requiredFields))
            throw new RuntimeException("Dot Validation Error: Given Dot missing " + getMissingField(dot, requiredFields)) ;
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
    //Used for sending data to server to create Adventure
    public HashMap<String, String> toHashMap(){
        HashMap<String, String> dot = super.toHashMap() ;
        dot.put(adventure_string, jsonifyArray(this.adventureIds)) ;
        return dot ;
    }

    public static void addObserver(Observer observer){
        Observers.add(observer) ;
    }

    public static ArrayList<Dot> getData(){
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
        for(Observer observer : Observers)
            observer.subscriberHasChanged("update");
    }

    public static void reload(){
        loader.reload();
        setError(null) ;
    }
}
