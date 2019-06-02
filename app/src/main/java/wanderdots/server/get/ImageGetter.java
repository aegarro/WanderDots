package wanderdots.server.get;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.wanderdots.R;

import wanderdots.server.ClientRequestQueue;
import wanderdots.Observer ;

public class ImageGetter implements Response.Listener<Bitmap>, Response.ErrorListener{

    private static final String baseURL = "http://10.0.2.2:5000/api/images" ;
    private Observer observer ;
    private Bitmap image ;
    private String error ;

    public ImageGetter(Observer observer){
       this.observer = observer ;
    }

    public void loadImage(String id){
        String url = baseURL + "/" + id ;
        ImageRequest request = new ImageRequest(url, this, 0, 0, null, this) ;
        ClientRequestQueue.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onResponse(Bitmap bitmap) {
        this.image = bitmap ;
        this.observer.subscriberHasChanged("update");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        this.error = error.toString() ;
        this.observer.subscriberHasChanged("error");
    }

    public boolean hasError(){
        return this.error != null ;
    }

    public String getError(){
        return this.error ;
    }

    public Bitmap getImage(){
        return this.image ;
    }
}
