package wanderDots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import wanderDots.Server.Get.get;

public final class dotLoader implements observer, loader<dot> {

    private ArrayList<dot> dots ;
    private String error ;
    private get<dot> dotGetter ;

    public dotLoader(){
        this.dots = new ArrayList<>() ;
        this.error = null ;
        this.dotGetter = new get(this, true) ;
        this.dotGetter.loadData();
    }

    //Initializes Dots from dotGetter's data retrieval
    public void subscriberHasChanged(String message){
        try {
            JSONObject response ;
            if(this.dotGetter.hasError())
                this.error = dotGetter.getError() ;
            else if((dotGetter.getResponse()).has("error"))
            {
                response = dotGetter.getResponse();
                this.error = response.getString("error") ;
            }
            else {
                response = dotGetter.getResponse();
                JSONArray jdots = response.getJSONArray("dots");
                for (int i = 0; i < jdots.length(); i++) {
                    JSONObject dot = jdots.getJSONObject(i);
                    this.dots.add(new dot(dot)) ;
                }
            }
        }catch(JSONException e){
            this.error = e.toString() ;
        }

        dot.dataFinishedLoading();
    }

    public void reload(){
        this.dotGetter.loadData() ;
    }

    public ArrayList<dot> getData(){
        return this.dots ;
    }

    public String getError(){
        return this.error ;
    }

    public boolean hasError(){
        return this.error != null ;
    }
}
