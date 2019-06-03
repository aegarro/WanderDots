package wanderdots.server;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.Tests.MainActivity;

import android.util.LruCache ;

/* Provides a singleton for queueing server request and
 * running them.
 */

public class ClientRequestQueue {

    private static ClientRequestQueue instance;
    private com.android.volley.RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx = MainActivity.getDefaultContext() ;

    private ClientRequestQueue() {
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

    public static synchronized ClientRequestQueue getInstance() {
        if (instance == null) {
            instance = new ClientRequestQueue();
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