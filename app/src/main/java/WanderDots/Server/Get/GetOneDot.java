package WanderDots.Server.Get;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import WanderDots.Dot;
import WanderDots.Server.MyRequestQueue;

/* Returns Some Dot from the Server given ID
 * This methods expects the user to implements "Listener" methods throw the Volley.Response.Listener
 * This class implements a version of Listener, one for strings, but users will need one for Dots (using generics).
 */
public class GetOneDot implements ErrorListener, Listener<String> {

    private ErrorListener errorListener ;
    private Listener<Dot> dotListener ;
    private Dot dot ;

    private MyRequestQueue queue ;

    public GetOneDot(Context context, Listener<Dot> dotListener){
        this.queue = MyRequestQueue.getInstance(context);
        this.dotListener = dotListener ;
        this.errorListener = null ;
        this.dot = null ;
    }

    public GetOneDot(Context context, Listener<Dot> dotListListener, ErrorListener errorListener){
        this(context, dotListListener) ;
        this.errorListener = errorListener ;
    }

    /* Sends request for dot with given ID to server
     * @param {String} dotID - The id of the dot to get
     * @return VOID
     */
    public void loadDot(String dotID){
        String url ="http://10.0.2.2:5000/api/get/dots/" + dotID ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        queue.addToRequestQueue(stringRequest);
    }

    /* Returns the last dot queried from the server (idk if useful)
     * @return {Dot} The last dot that was returned from the server
     */
    public Dot getCachedDot(){
        return this.dot ;
    }

    //FOR USE BY VOLLEY LIBRARY - DO NOT USE
    public void onResponse(String response) {
        try {
            JSONObject dot = new JSONObject(response).getJSONObject("dot") ;
            this.dot = new Dot(dot) ;
            dotListener.onResponse(this.dot);
        } catch(org.json.JSONException e){
            Log.e("Server Error:", e.toString());
        }
    }

    //FOR USE BY VOLLEY LIBRARY - DO NOT USE
    public void onErrorResponse(VolleyError error) {
        if(errorListener == null)
            Log.e("HTTPS Request Error", error.toString()) ;
        else
            this.errorListener.onErrorResponse(error);
    }
}
