package wanderDots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wanderDots.Server.Get.get;

public final class adventureLoader implements observer, loader<adventure> {

    private ArrayList<adventure> adventures;
    private String error ;
    private get<adventure> adventureGetter;

    public adventureLoader(){
        this.adventures = new ArrayList<>() ;
        this.error = null ;
        this.adventureGetter = new get(this, false) ;
        this.adventureGetter.loadData();
    }

    public void subscriberHasChanged(String message){
        try {
            JSONObject response = adventureGetter.getResponse() ;
            if(this.adventureGetter.hasError())
                this.error = this.adventureGetter.getError() ;
            else if(response == null)
                this.error = "adventure loader received null response";
            else if (response.has("error"))
                this.error = response.getString("error") ;
            else{
                JSONArray adventures = response.getJSONArray("adventures");
                Log.d("arodr", "length: " + adventures.length()) ;
                for (int i = 0; i < adventures.length(); i++) {
                    JSONObject adventure = adventures.getJSONObject(i);
                    wanderDots.adventure newAdventure = new adventure(adventure) ;
                    this.adventures.add(newAdventure) ;
                }
            }
        }catch(JSONException e){
            Log.d("arodr", "Error: " + e.toString());
            this.error = e.toString() ;
        }
        adventure.dataFinishedLoading();
    }

    public void reload(){
        this.adventureGetter.loadData() ;
    }

    public ArrayList<adventure> getData(){
        return this.adventures;
    }

    public String getError(){
        return this.error ;
    }

    public boolean hasError(){
        return this.error != null ;
    }
}
