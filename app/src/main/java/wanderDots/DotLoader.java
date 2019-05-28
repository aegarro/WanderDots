package wanderDots;

import com.example.wanderdots.MainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import wanderDots.Server.Get.Get;

public final class DotLoader implements Observer, Loader<Dot> {

    private ArrayList<Dot> dots ;
    private String error ;
    private Get<Dot> dotGetter ;

    public DotLoader(){
        this.dots = new ArrayList<>() ;
        this.error = null ;
        this.dotGetter = new Get(MainActivity.DEFAULT_CONTEXT,this, true) ;
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
                    this.dots.add(new Dot(dot)) ;
                }
            }
        }catch(JSONException e){
            this.error = e.toString() ;
        }

        Dot.dataFinishedLoading();
    }

    public void reload(){
        this.dotGetter.loadData() ;
    }

    public ArrayList<Dot> getData(){
        return this.dots ;
    }

    public String getError(){
        return this.error ;
    }

    public boolean hasError(){
        return this.error != null ;
    }
}
