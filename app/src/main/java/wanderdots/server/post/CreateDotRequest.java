package wanderdots.server.post;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Map;

import wanderdots.Dot;

public class CreateDotRequest extends JsonObjectRequest {

    private static final String url = "http://10.0.2.2:5000/api/post/Dot" ;

    private Dot dot ;

    public CreateDotRequest(Dot dot, Response.Listener responseListener,
                            Response.ErrorListener errorListener){
        super(Request.Method.POST, CreateDotRequest.url, dot.toJSON(), responseListener, errorListener) ;
        this.dot = dot ;
    }

    @Override
    protected Map<String, String> getParams() {
        return dot.toHashMap() ;
    }
}
