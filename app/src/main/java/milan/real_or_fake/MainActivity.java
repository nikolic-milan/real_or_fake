package milan.real_or_fake;

import android.annotation.SuppressLint;

import android.content.ClipData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Random;



public class MainActivity extends AppCompatActivity {

    private ImageView image, img, left_100, right_100, left_80, right_80, left_60, right_60,
            left_40, right_40, left_20, right_20, left_0, right_0;
    public TextView text;
    public double confidenceLevel;
    int rand, truerand;
    public boolean real;
    String getImageURL = "http://www.studenti.famnit.upr.si/~89161009/getImage.php";
    String img_url;
    String real_fake, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        img = (ImageView) findViewById(R.id.movable);
        img.setOnLongClickListener(longClickListener);
        text = (TextView) findViewById(R.id.textView);
        getImage();

        left_100 = (ImageView) findViewById(R.id.left100);
        right_100 = (ImageView) findViewById(R.id.right100);
        left_80 = (ImageView) findViewById(R.id.left80);
        right_80 = (ImageView) findViewById(R.id.right80);
        left_60 = (ImageView) findViewById(R.id.left60);
        right_60 = (ImageView) findViewById(R.id.right60);
        left_40 = (ImageView) findViewById(R.id.left40);
        right_40 = (ImageView) findViewById(R.id.right40);
        left_20 = (ImageView) findViewById(R.id.left20);
        right_20 = (ImageView) findViewById(R.id.right20);
        left_0 = (ImageView) findViewById(R.id.left0);
        right_0 = (ImageView) findViewById(R.id.right0);

        left_100.setOnDragListener(dragListener);
        right_100.setOnDragListener(dragListener);
        left_80.setOnDragListener(dragListener);
        right_80.setOnDragListener(dragListener);
        left_60.setOnDragListener(dragListener);
        right_60.setOnDragListener(dragListener);
        left_40.setOnDragListener(dragListener);
        right_40.setOnDragListener(dragListener);
        left_20.setOnDragListener(dragListener);
        right_20.setOnDragListener(dragListener);
        left_0.setOnDragListener(dragListener);
        right_0.setOnDragListener(dragListener);





    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                return true;
            }
        };

    View.OnDragListener dragListener = new View.OnDragListener() {

        @SuppressLint("ResourceType")
        @Override
        public boolean onDrag (View v, DragEvent event) {
            int dragEvent = event.getAction();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:

                    if (v.getId() == R.id.left100) {
                        confidenceLevel = 100;
                        text.setText(confidenceLevel + "% Fake");
                        real = false;
                    }else if (v.getId() == R.id.right100){
                        confidenceLevel = 100;
                        text.setText(confidenceLevel + "% Real");
                        real = true;
                    }else if(v.getId() == R.id.left80) {
                        confidenceLevel = random(80, 99);
                        text.setText(confidenceLevel + "% Fake");
                        real = false;
                    }else if(v.getId() == R.id.right80){
                        confidenceLevel = random(80, 99);
                        text.setText(confidenceLevel + "% Real");
                        real = true;
                    }else if(v.getId() == R.id.left60) {
                        confidenceLevel = random(60, 79);
                        text.setText(confidenceLevel + "% Fake");
                        real = false;
                    }else if(v.getId() == R.id.right60){
                        confidenceLevel = random(60, 79);
                        text.setText(confidenceLevel + "% Real");
                        real = true;
                    }else if(v.getId() == R.id.left40) {
                        confidenceLevel = random(40, 59);
                        text.setText(confidenceLevel + "% Fake");
                        real = false;
                    }else if(v.getId() == R.id.right40){
                        confidenceLevel = random(40, 59);
                        text.setText(confidenceLevel + "% Real");
                        real = true;
                    }else if(v.getId() == R.id.left20) {
                        confidenceLevel = random(20, 39);
                        text.setText(confidenceLevel + "% Fake");
                        real = false;
                    }else if(v.getId() == R.id.right20){
                        confidenceLevel = random(20, 39);
                        text.setText(confidenceLevel + "% Real");
                        real = true;
                    }else if(v.getId() == R.id.left0) {
                        confidenceLevel = random(0, 19);
                        text.setText(confidenceLevel + "% Fake");
                        real = false;
                    }else if(v.getId() == R.id.right0){
                        confidenceLevel = random(0, 19);
                        text.setText(confidenceLevel + "% Real");
                        real = true;
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    text.setText("");
                    break;
                case DragEvent.ACTION_DROP:
                    Intent i = new Intent(MainActivity.this, PopupActivity.class);
                    i.putExtra("opinions", real);
                    i.putExtra("conf", confidenceLevel);
                    i.putExtra("img", img_url);
                    i.putExtra("rf", real_fake);
                    i.putExtra("id", id);
                    MainActivity.this.startActivity(i);
                    MainActivity.this.finish();
                    break;
            }

            return true;
        }
    };

    public void  getImage (){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getImageURL,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("sk2_images");

                            rand = (int) random(0, data.length()-1);

                            JSONObject dat = data.getJSONObject(rand);
                            id = dat.getString("id");
                            img_url = dat.getString("img_url");
                            real_fake = dat.getString("real_fake");
                            Glide.with(getApplicationContext()).load(img_url)
                                    .override(400, 320).fitCenter().into(image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public double random(int min, int max){
        Random r = new Random();
        double x = r.nextDouble() * (max-min) + min;
        return(Math.round(x * 10) /10.0);

    }


}

