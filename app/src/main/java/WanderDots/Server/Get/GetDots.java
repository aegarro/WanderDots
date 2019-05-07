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

import WanderDots.Observer ;
import WanderDots.Dot;
import WanderDots.Server.MyRequestQueue;

/* Returns All the Dots contained in the database
 * This methods expects the user to implements "Listener" methods throw the Volley.Response.Listener
 * This class implements a version of Listener, one for strings, but users will need one for ArrayList<Dot> (using generics).
 */
public class GetDots implements ErrorListener, Listener<String> {

    private Observer observer ;
    private ArrayList<Dot> dots ;
    private String error ;

    private String url = "http://10.0.2.2:5000/api/get/dots" ;

    private MyRequestQueue queue ;

    public GetDots(Context context, Observer observer){
        this.queue = MyRequestQueue.getInstance(context);
        this.observer = observer ;
        this.dots = null ;
        this.error = null ;
    }

    public void loadDots(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        queue.addToRequestQueue(stringRequest);
    }

    public void updateDots(ArrayList<Dot> newDots){
        this.dots = newDots ;
        this.observer.dataHasChanged("dots");
        System.out.println("Added " + newDots.size() + " new dots!") ;
    }

    public void onResponse(String response) {
        try {
            JSONArray jsonDots = new JSONObject(response).getJSONArray("dots") ;
            ArrayList<Dot> dots = new ArrayList<Dot>() ;
            for(int i=0; i<jsonDots.length(); i++)
                dots.add(new Dot(jsonDots.getJSONObject(i))) ;
            updateDots(dots) ;
        } catch(org.json.JSONException e){
            Log.e("GetDots Error:", e.toString());
        }
    }

    public boolean hasValue(){
        return this.dots != null ;
    }

    public void onErrorResponse(VolleyError error) {
        this.error = error.toString() ;
    }

    public ArrayList<Dot> getDots(){
        return this.dots ;
    }

    public String getError(){
        return this.error ;
    }

    public void clear(){
        this.dots = null ;
        this.error = null ;
    }
}
