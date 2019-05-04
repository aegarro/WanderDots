package WanderDots.Server.Get;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener ;
import com.android.volley.Response.Listener ;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import WanderDots.Dot;
import WanderDots.Server.MyRequestQueue;

/* Returns All the Dots contained in the database
 * This methods expects the user to implements "Listener" methods throw the Volley.Response.Listener
 * This class implements a version of Listener, one for strings, but users will need one for ArrayList<Dot> (using generics).
 */
public class GetDots implements ErrorListener, Listener<String> {

    private ErrorListener errorListener ;
    private Listener<ArrayList<Dot>> dotListListener ;
    private ArrayList<Dot> dots ;

    private MyRequestQueue queue ;

    public GetDots(Context context, Listener<ArrayList<Dot>> dotListListener){
        this.queue = MyRequestQueue.getInstance(context);
        this.dotListListener = dotListListener ;
        this.errorListener = null ;
        this.dots = new ArrayList<Dot>() ;
    }

    public GetDots(Context context, Listener<ArrayList<Dot>> dotListListener, ErrorListener errorListener){
        this(context, dotListListener) ;
        this.errorListener = errorListener ;
    }

    public void loadDots(){
        String url ="http://10.0.2.2:5000/api/get/dots";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        queue.addToRequestQueue(stringRequest);
    }

    public void onResponse(String response) {
        try {
            JSONArray dots = new JSONObject(response).getJSONArray("dots") ;
            for(int i=0; i<dots.length(); i++)
                this.dots.add(new Dot(dots.getJSONObject(i))) ;
            dotListListener.onResponse(this.dots);
        } catch(org.json.JSONException e){
            Log.e("Server Error:", e.toString());
        }
    }

    public void onErrorResponse(VolleyError error) {
        if(errorListener == null)
            Log.e("HTTPS Request Error", error.toString()) ;
        else
            this.errorListener.onErrorResponse(error);
    }
}
