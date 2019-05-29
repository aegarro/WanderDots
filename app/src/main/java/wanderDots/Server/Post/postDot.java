package wanderDots.Server.Post;

import android.content.Context;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import wanderDots.dot;
import wanderDots.Server.requestQueue;

public class postDot implements Response.Listener<JSONObject>,
                                Response.ErrorListener{

    private wanderDots.observer observer ;
    private requestQueue queue ;
    private String dotID ;
    private String error ;

    public postDot(Context context, wanderDots.observer observer) {
        this.queue = requestQueue.getInstance() ;
        this.observer = observer ;
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
                this.observer.subscriberHasChanged("dot has been received");
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

    public void postDot(dot dot){
        this.queue.addToRequestQueue(new createDotRequest(dot,
                this, this)) ;
    }
}
