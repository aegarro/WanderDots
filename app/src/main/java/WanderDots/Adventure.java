package WanderDots;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

public class Adventure extends Experience {

    private ArrayList<String> dotsVisited ;
    private String[] requiredFields = {"dotsVisited"} ;

    public Adventure(){
        super() ;
        this.dotsVisited = new ArrayList<String>() ;
    }

    public Adventure(JSONObject adventure) throws org.json.JSONException{
        super(adventure) ;
        if(!containsRequiredFields(adventure, requiredFields))
            throw new RuntimeException("Adventure Validation Error: Missing Field: " + getMissingField(adventure, requiredFields)) ;
        instantiateFromJSON(adventure);
    }

    @Override
    public void instantiateFromJSON(JSONObject adventure) throws org.json.JSONException {
        super.instantiateFromJSON(adventure);
        this.dotsVisited = createStringList(adventure.getJSONArray("dotsVisited")) ;
    }

    public void addDotVisited(String dotID){
        this.dotsVisited.add(dotID) ;
    }

    public void removeDotVisited(String dotID){
        this.dotsVisited.remove(dotID) ;
    }

    public ArrayList<String> getDotsVisited(){
        return new ArrayList<String>(this.dotsVisited) ;
    }

    //TODO: toString() (see Dot for guide)
    //TODO: toJSON() (same as above)
}
