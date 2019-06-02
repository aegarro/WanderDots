package wanderdots.server.post;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import wanderdots.Dot;
import wanderdots.Observer;
import wanderdots.server.ClientRequestQueue;

/* Provides the method "postDot" that create a given dot on the server.
 * The subscriber is called as soon as a response from the server is received,
 * whether error or success.
 */
public class DotPoster implements Response.Listener<JSONObject>,
                                Response.ErrorListener{

    private Observer observer ;
    private ClientRequestQueue queue ;
    private String dotID ;
    private String error ;

    public DotPoster(Observer observer) {
        this.observer = observer ;
        this.queue = ClientRequestQueue.getInstance() ;
        this.dotID = null ;
    }

    public String getDotID(){
        return this.dotID ;
    }

    public String getError(){
        return this.error ;
    }

    @Override
    public void onResponse(JSONObject response) {
        String errorMessage = "error";
        try {
            if(response.has(errorMessage)){
               this.error = response.getString(errorMessage) ;
               this.observer.subscriberHasChanged(errorMessage);
            } else {
                this.dotID = response.getString("id") ;
                this.observer.subscriberHasChanged("Dot has been received");
            }
        } catch(JSONException e){
            this.error = e.toString() ;
            this.observer.subscriberHasChanged(errorMessage);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("arodr error:", error.toString());
    }

    public void postDot(Dot dot){
        this.queue.addToRequestQueue(new DotPosterRequest(dot,
                this, this)) ;
    }
}
