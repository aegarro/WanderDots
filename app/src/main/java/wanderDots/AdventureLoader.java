package wanderDots;

import com.example.wanderdots.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wanderDots.Server.Get.Get;

public final class AdventureLoader implements Observer, Loader<Adventure> {

    private ArrayList<Adventure> adventures;
    private String error ;
    private Get<Adventure> adventureGetter;

    public AdventureLoader(){
        this.adventures = new ArrayList<>() ;
        this.error = null ;
        this.adventureGetter = new Get(MainActivity.DEFAULT_CONTEXT,this, false) ;
        this.adventureGetter.loadData();
    }

    public void subscriberHasChanged(String message){
        try {
            JSONObject response ;
            if(this.adventureGetter.hasError())
                this.error = this.adventureGetter.getError() ;
            else if ((response = adventureGetter.getResponse()).has("error"))
                this.error = response.getString("error") ;
            else{
                JSONArray adventures = response.getJSONArray("adventures");
                for (int i = 0; i < adventures.length(); i++) {
                    JSONObject adventure = adventures.getJSONObject(i);
                    this.adventures.add(new Adventure(adventure)) ;
                }
            }
        }catch(JSONException e){
           this.error = e.toString() ;
        }
        Adventure.dataFinishedLoading();
    }

    public void reload(){
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
