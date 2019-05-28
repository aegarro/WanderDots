package wanderDots;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Dot extends Experience {

    private static ArrayList<Observer> observers = new ArrayList<>();
    private static ArrayList<Dot> data = null ;
    private static DotLoader loader = new DotLoader() ;

    private ArrayList<String> adventureIds;
    private String[] requiredFields = {"adventures"} ;

    public Dot(){
        super() ;
        this.adventureIds = new ArrayList<>() ;
    }

    public Dot(JSONObject dot) throws org.json.JSONException{
        super(dot) ;
        if(!containsRequiredFields(dot, requiredFields))
            throw new RuntimeException("Dot Validation Error: Given dot missing " + getMissingField(dot, requiredFields)) ;
        instantiateFromJSON(dot) ;
    }

    @Override
    public void instantiateFromJSON(JSONObject dot) throws org.json.JSONException{
        super.instantiateFromJSON(dot) ;
        createStringList(dot.getJSONArray("adventures")) ;
    }

    public JSONObject toJSON(){
        try {
            JSONObject data = super.toJSON() ;
            data.put("adventures", (Object) adventureIds);
            return data ;
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
        dot.put("adventures", jsonifyArray(this.adventureIds)) ;
        return dot ;
    }

    //DO NOT REMOVE: This method triggers the static initializer of this class
    //Beginning the loading process, without this Experience parent class recieves a null
    //Loader object
    public static void addObserver(Observer observer){
        observers.add(observer) ;
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
        for(Observer observer : observers)
            observer.subscriberHasChanged("update");
    }

    public static void reload(){
        loader.reload();
    }
}
