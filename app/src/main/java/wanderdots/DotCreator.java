package wanderdots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import wanderdots.server.get.Get;

/* Converts the pure server response (given by Get object)
 * into instances of Dot.
 *
 */
public final class DotCreator implements Observer, Loader<Dot> {

    private ArrayList<Dot> dots ;
    private String error ;
    private Get dotGetter ;

    public DotCreator(){
        this.dots = new ArrayList<>() ;
        this.error = null ;
        this.dotGetter = new Get(this, true) ;
    }

    /* Gets called whenever Get (dotGetter) has finished loading response from server
     * Creates list of Dots from response.
     * @param {String} message - The type of update that occurred in Get.
     */
    public void subscriberHasChanged(String message){
        Log.d("arodr", "DotCreator received new data") ;
        try {
            JSONObject response ;
            if(this.dotGetter.hasError())
                this.error = dotGetter.getError() ;
            else if((dotGetter.getResponse()).has("error")) {
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
        Log.d("arodr", "DotCreator (error)" + this.error) ;
        Log.d("arodr", "DotCreator (data size)" + this.dots.size()) ;
        Dot.dataFinishedLoading();
    }

    public void reload(){
        this.error = null ;
        this.dots.clear() ;
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
