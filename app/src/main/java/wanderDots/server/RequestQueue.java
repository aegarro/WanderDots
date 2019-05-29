package wanderDots.server;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.wanderdots.MainActivity;

import android.util.LruCache ;

public class RequestQueue {

    private static RequestQueue instance;
    private com.android.volley.RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx = MainActivity.DEFAULT_CONTEXT ;

    private RequestQueue() {
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized RequestQueue getInstance() {
        if (instance == null) {
            instance = new RequestQueue();
        }
        return instance;
    }

    public com.android.volley.RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}