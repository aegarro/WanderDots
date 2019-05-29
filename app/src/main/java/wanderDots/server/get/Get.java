package wanderDots.server.get;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import wanderDots.Experience;
import wanderDots.Observer;
import wanderDots.server.RequestQueue;

/* Returns All the Dots contained in the database
 * This methods expects the user to implements "Listener" methods throw the Volley.Response.Listener
 * This class implements a version of Listener, one for strings, but users will need one for ArrayList<Dot> (using generics).
 */
public class Get implements ErrorListener, Listener<String> {

    private Observer observer ;
    private String url ;
    private String error ;
    private JSONObject response ;
    private String getDot = "http://10.0.2.2:5000/api/Get/dots" ;
    private String getAdventures = "http://10.0.2.2:5000/api/Get/adventures";

    private RequestQueue queue ;

    public Get(Observer observer, boolean isDot){
        this.queue = RequestQueue.getInstance();
        this.observer = observer ;
        this.url = isDot ? getDot : getAdventures ;
        this.error = null ;
    }

    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url, this, this) ;
        queue.addToRequestQueue(stringRequest);
    }

    public void onResponse(String response) {
        try {
            if(response == null){
                this.error = "Null response from StringRequest" ;
                this.observer.subscriberHasChanged("error");
            }
            this.response = new JSONObject(response);
            observer.subscriberHasChanged("update");

        }catch(JSONException e){
            Log.d("arodr:Get","JSON Error" + e.toString()) ;
            this.error = e.toString() ;
            this.observer.subscriberHasChanged("error");
        }
    }

    public void onErrorResponse(VolleyError error) {
        Log.d("arodr:Get", "an error has occurred creating a request") ;
        this.error = error.toString() ;
        this.observer.subscriberHasChanged(this.url);
    }

    public boolean hasError(){
        return this.error != null ;
    }

    public String getError(){
        return this.error ;
    }

    public JSONObject getResponse(){
        return this.response ;
    }
}
