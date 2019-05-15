package WanderDots.Server.Post;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import WanderDots.Dot;
import java.util.Map;

public class CreateDotRequest extends JsonObjectRequest {

    private Dot dot ;
    public static String url = "http://10.0.2.2:5000/api/post/dot" ;

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
        return dot.getHashMap() ;
    }
}
