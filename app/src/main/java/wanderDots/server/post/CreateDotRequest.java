package wanderDots.server.post;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Map;

import wanderDots.Dot;

public class CreateDotRequest extends JsonObjectRequest {

    private Dot dot ;
    public static String url = "http://10.0.2.2:5000/api/post/Dot" ;

    public CreateDotRequest(Dot dot, Response.Listener responseListener,
                            Response.ErrorListener errorListener){
        super(Request.Method.POST, CreateDotRequest.url, dot.toJSON(), responseListener, errorListener) ;
        this.dot = null ;
    }

    public void setDot(Dot dot){
        this.dot = dot ;
    }

    @Override
    protected Map<String, String> getParams() {
        return dot.toHashMap() ;
    }
}
