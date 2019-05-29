package wanderdots.server.post;

import android.content.Context;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import wanderdots.Dot;
import wanderdots.Observer;
import wanderdots.server.RequestQueue;

public class PostDot implements Response.Listener<JSONObject>,
                                Response.ErrorListener{

    private Observer observer ;
    private RequestQueue queue ;
    private String dotID ;
    private String error ;

    public PostDot(Context context, Observer observer) {
        this.queue = RequestQueue.getInstance() ;
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
        this.queue.addToRequestQueue(new CreateDotRequest(dot,
                this, this)) ;
    }
}
