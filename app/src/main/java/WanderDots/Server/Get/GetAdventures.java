package WanderDots.Server.Get;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import WanderDots.Adventure;
import WanderDots.Dot;
import WanderDots.Server.MyRequestQueue;
import WanderDots.Observer ;

/* Returns All the Dots contained in the database
 * This methods expects the user to implements "Listener" methods throw the Volley.Response.Listener
 * This class implements a version of Listener, one for strings, but users will need one for ArrayList<Dot> (using generics).
 */
public class GetAdventures implements ErrorListener, Listener<String> {

    private Observer observer ;
    private ArrayList<Adventure> adventures ;
    private String error ;

    private MyRequestQueue queue ;

    public GetAdventures(Context context, Observer observer){
        this.queue = MyRequestQueue.getInstance(context);
        this.observer = observer ;
        this.adventures = null ;
        this.error = null ;
    }

    public void loadAdventures(){
        String url ="http://10.0.2.2:5000/api/get/adventures";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        queue.addToRequestQueue(stringRequest);
    }

    private void updateAdventures(ArrayList<Adventure> newAdventures){
        this.adventures = newAdventures ;
        observer.dataHasChanged();
    }

    public void onResponse(String response) {
        try {
            JSONArray jsonAdventures = new JSONObject(response).getJSONArray("adventures") ;
            ArrayList<Adventure> adventures = new ArrayList<Adventure>() ;
            for(int i=0; i<jsonAdventures.length(); i++)
                adventures.add(new Adventure(jsonAdventures.getJSONObject(i))) ;
            updateAdventures(adventures);
        } catch(org.json.JSONException e){
            Log.e("GetAdventures Error:", e.toString());
        }
    }

    public void onErrorResponse(VolleyError error) {
        this.error = error.toString() ;
    }

    public String getError(){
        return this.error ;
    }

    public ArrayList<Adventure> getAdventures(){
        return this.adventures;
    }
}
