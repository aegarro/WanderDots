package WanderDots.Server.Get;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.ArrayList;
import WanderDots.Experience;
import WanderDots.Observer;
import WanderDots.Server.MyRequestQueue;

/* Returns All the Dots contained in the database
 * This methods expects the user to implements "Listener" methods throw the Volley.Response.Listener
 * This class implements a version of Listener, one for strings, but users will need one for ArrayList<Dot> (using generics).
 */
public class Get<T extends Experience> implements ErrorListener, Listener<String> {

    private Observer observer ;
    private String url ;
    private ArrayList<T> data ;
    private String error ;
    private DataCreator<T> creator ;
    private String getDot = "http://10.0.2.2:5000/api/get/dots" ;
    private String getAdventures = "http://10.0.2.2:5000/api/get/adventures";

    private MyRequestQueue queue ;

    public Get(Context context, Observer observer, boolean isDot, DataCreator<T> creator){
        this.queue = MyRequestQueue.getInstance(context);
        this.observer = observer ;
        this.url = isDot ? getDot : getAdventures ;
        this.data = null ;
        this.error = null ;
        this.creator = creator ;
    }

    public void load(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url, this, this) ;
        queue.addToRequestQueue(stringRequest);
    }

    public void update(ArrayList<T> newData){
        this.data = newData ;
        observer.dataHasChanged(this.url);
    }

    public void onResponse(String response) {
        ArrayList<T> dataFromServer = this.creator.createMany(response) ;
        update(dataFromServer) ;
    }

    public void onErrorResponse(VolleyError error) {
        this.error = error.toString() ;
        this.observer.dataHasChanged(this.url);
        Log.d("arodr", "an error has occurred creating a request") ;
    }

    public boolean hasData(){
        return this.data != null ;
    }

    public boolean hasError(){
        return this.error != null ;
    }

    public String getError(){
        return this.error ;
    }

    public ArrayList<T> getData(){
        return this.data ;
    }
}
