package wanderDots.Server.Post;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Map;

public class createDotRequest extends JsonObjectRequest {

    private wanderDots.dot dot ;
    public static String url = "http://10.0.2.2:5000/api/post/dot" ;

    public createDotRequest(wanderDots.dot dot, Response.Listener responseListener,
                            Response.ErrorListener errorListener){
        super(Request.Method.POST, createDotRequest.url, dot.toJSON(), responseListener, errorListener) ;
        this.dot = null ;
    }

    public void setDot(wanderDots.dot dot){
        this.dot = dot ;
    }

    @Override
    protected Map<String, String> getParams() {
        return dot.toHashMap() ;
    }
}
