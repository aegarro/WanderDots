package WanderDots.Server.Post;

import android.content.Context;

import WanderDots.Observer ;
import WanderDots.Dot ;
import WanderDots.Server.MyRequestQueue;

public class PostDot {

    private Observer observer ;
    private Dot dotCreated ;
    private MyRequestQueue queue ;


    public PostDot(Context context, Observer observer) {
        this.queue = MyRequestQueue.getInstance(context) ;
        this.observer = observer ;
    }

    public void createDot(Dot dot){
        String dotJSON = dot.toString() ;
        String url = "http://localhost:5000/api/post/dot" ;

    }
}
