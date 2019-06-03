package wanderdots.server.post;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wanderdots.server.ClientRequestQueue;
import wanderdots.Observer ;


public class ImagePoster implements Response.Listener<NetworkResponse>, Response.ErrorListener {

    private static final String url = "http://10.0.2.2:5000/api/upload" ;
    private static final String twoHyphens = "--";
    private static final String imagePoster = "ImagePoster";
    private static final String lineEnd = "\r\n";
    private static final String boundary = "apiclient-" + System.currentTimeMillis();
    private static final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;
    private Observer observer ;
    private JSONObject response ;
    private String error ;

    public ImagePoster(Observer observer){
        this.observer = observer ;
    }

    public void postImage(Bitmap image){
        byte[] fileData1 = convertBitmapToByteArray(image) ;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            buildPart(dos, fileData1, "testImageUpload.png");
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            multipartBody = bos.toByteArray();
        } catch (IOException e) {
            Log.d("arodr: ", "(error):" + e.toString()) ;
        }

        ImagePostRequest imagePostRequest = new ImagePostRequest(url, null, mimeType, multipartBody, this, this) ;
        ClientRequestQueue.getInstance().addToRequestQueue(imagePostRequest);
    }

    @Override
    public void onResponse(NetworkResponse networkResponse) {
        try {
            String responseInText = parseNetworkResponse(networkResponse);
            if(responseInText == null){
                this.observer.subscriberHasChanged(imagePoster);
                return ;
            }
            JSONObject jResponse = new JSONObject(responseInText) ;
            this.response = jResponse ;
        }catch(JSONException e){
           Log.d("arodr", e.toString()) ;
           this.error = e.toString() ;
        }
        this.observer.subscriberHasChanged(imagePoster);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        this.error = error.toString() ;
        this.observer.subscriberHasChanged(imagePoster);
    }

    public boolean hasError(){
        return this.error != null ;
    }

    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"document\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private String parseNetworkResponse(NetworkResponse response){
        String json ;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return json ;
        }catch(java.io.UnsupportedEncodingException e){
            this.error = e.toString() ;
        }
        return null ;
    }

    public String getError(){
        return this.error ;
    }

    public JSONObject getResponse(){
        return this.response ;
    }
}
