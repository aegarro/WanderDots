package wanderDots.Server.Post;

import android.content.Context;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;
import wanderDots.Observer ;
import wanderDots.Dot ;
import wanderDots.Server.MyRequestQueue;

public class PostDot implements Response.Listener<JSONObject>,
                                Response.ErrorListener{

    private Observer observer ;
    private MyRequestQueue queue ;
    private String dotID ;
    private String error ;

    public PostDot(Context context, Observer observer) {
        this.queue = MyRequestQueue.getInstance(context) ;
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
        // response
        try {
            if(response.has("error")){
               this.error = response.getString("error") ;
               this.observer.subscriberHasChanged("error");
            } else {
                String dotID = response.getString("id") ;
                this.dotID = dotID ;
                this.observer.subscriberHasChanged("Dot has been received");
            }
        } catch(JSONException e){
            this.error = e.toString() ;
            this.observer.subscriberHasChanged("error");
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
