package wanderdots.server.post;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import wanderdots.Dot;

public class DotPosterRequest extends JsonObjectRequest {

    private static final String URL = "http://10.0.2.2:5000/api/post/Dot" ;

    public DotPosterRequest(Dot dot, Response.Listener responseListener,
                            Response.ErrorListener errorListener){
        super(Request.Method.POST, DotPosterRequest.URL, dot.toJSON(), responseListener, errorListener) ;
        Log.d("arodr", "created request for: " + dot.toJSON()) ;
    }
}
