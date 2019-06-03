package wanderdots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wanderdots.server.get.Get;

// Creates Adventures from the server response given by Get object.

public final class AdventureCreator implements Observer, Loader<Adventure> {

    private ArrayList<Adventure> adventures;
    private String error ;
    private Get adventureGetter;

    public AdventureCreator(){
        this.adventures = new ArrayList<>() ;
        this.error = null ;
        this.adventureGetter = new Get(this, false) ;
    }

    /* Is called whenever Get has finished loading adventures
     * creates list of adventures from response
     * @param {String} message - The type of update that occurred in Get
     */
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
                JSONArray adventures = response.getJSONArray("adventures");
                for (int i = 0; i < adventures.length(); i++) {
                    JSONObject adventure = adventures.getJSONObject(i);
                    Adventure newAdventure = new Adventure(adventure) ;
                    this.adventures.add(newAdventure) ;
                }
            }
        }catch(JSONException e){
            Log.d("arodr", "Error: " + e.toString());
            this.error = e.toString() ;
        }
        Adventure.dataFinishedLoading();
    }

    public void reload(){

        this.error = null ;
        this.adventures.clear() ;
        this.adventureGetter.loadData() ;
    }

    public ArrayList<Adventure> getData(){
        return this.adventures;
    }

    public String getError(){
        return this.error ;
    }

    public boolean hasError(){
        return this.error != null ;
    }
}
