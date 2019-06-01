package wanderdots.server.get;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import wanderdots.Observer;
import wanderdots.server.RequestQueue;

/* Communicates with the server to get either Dots or Adventures. Notifies observer when finished.
 *
 * The observer is attached during object construction and loading begins immediately after.
 * Observer will be called once response has been received, regardless of error or successful
 * response.
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
        loadData() ;
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
