package wanderdots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wanderdots.server.get.Get;

public final class AdventureLoader implements Observer, Loader<Adventure> {

    private ArrayList<Adventure> Adventures;
    private String error ;
    private Get adventureGetter;

    public AdventureLoader(){
        this.Adventures = new ArrayList<>() ;
        this.error = null ;
        this.adventureGetter = new Get(this, false) ;
        this.adventureGetter.loadData();
    }

    public void subscriberHasChanged(String message){
        try {
            JSONObject response = adventureGetter.getResponse() ;
            if(this.adventureGetter.hasError())
                this.error = this.adventureGetter.getError() ;
            else if(response == null)
                this.error = "Adventure Loader received null response";
            else if (response.has("error"))
                this.error = response.getString("error") ;
            else{
                JSONArray adventures = response.getJSONArray("Adventures");
                Log.d("arodr", "length: " + adventures.length()) ;
                for (int i = 0; i < adventures.length(); i++) {
                    JSONObject adventure = adventures.getJSONObject(i);
                    Adventure newAdventure = new Adventure(adventure) ;
                    this.Adventures.add(newAdventure) ;
                }
            }
        }catch(JSONException e){
            Log.d("arodr", "Error: " + e.toString());
            this.error = e.toString() ;
        }
        Adventure.dataFinishedLoading();
    }

    public void reload(){
        this.adventureGetter.loadData() ;
    }

    public ArrayList<Adventure> getData(){
        return this.Adventures;
    }

    public String getError(){
        return this.error ;
    }

    public boolean hasError(){
        return this.error != null ;
    }
}
