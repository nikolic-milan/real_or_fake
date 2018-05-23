package milan.real_or_fake;

import android.accessibilityservice.FingerprintGestureController;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.Map;

import static milan.real_or_fake.R.drawable.fakeimage;

public class PopupActivity extends AppCompatActivity {
    TextView op, confidances, thinkFake, fakeConfidance, thinkReal, realConfidance, text;
    Button next;
    ImageView imageView;
    boolean real;
    double confidancelvl;
    int real_Pnumber, fake_Pnumber, id_number, random;
    double avg, same, up;
    String getDataURL = "http://www.studenti.famnit.upr.si/~89161009/getData.php";
    String sendDataURL = "http://www.studenti.famnit.upr.si/~89161009/sendData.php";
    String img_url, real_fake, id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        confidances = (TextView) findViewById(R.id.confidance);
        op = (TextView) findViewById(R.id.opinion);
        thinkFake = (TextView) findViewById(R.id.thinkFake);
        fakeConfidance = (TextView) findViewById(R.id.fakeConfidance);
        thinkReal = (TextView) findViewById(R.id.thinkReal);
        realConfidance = (TextView) findViewById(R.id.realConfidance);
        next = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        text = (TextView) findViewById(R.id.textImage);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PopupActivity.this, MainActivity.class);
                PopupActivity.this.startActivity(i);
                PopupActivity.this.finish();
            }
        });

        getData();


    }

    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            real = extras.getBoolean("opinions");
            confidancelvl = extras.getDouble("conf");

            img_url = extras.getString("img");
            real_fake = extras.getString("rf");
            id = extras.getString("id");


        }

        if (real == false){
            op.setText("You think this is fake!");
        }
        Glide.with(getApplicationContext()).load(img_url)
                .override(400, 320).fitCenter().into(imageView);
        if (real_fake.equals("fake")) {
            text.setBackgroundResource(R.drawable.fakeimage);
        } else {
            text.setBackgroundResource(R.drawable.realimage);
        }

        confidances.setText("With confidance: " + confidancelvl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getDataURL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data =  response.getJSONArray("sk2_votes");
                            int i = Integer.parseInt(id);
                            JSONObject dat = data.getJSONObject(i);

                            String img_id = dat.getString("img_id");
                            String real_p = dat.getString("real_p");
                            String fake_p = dat.getString("fake_p");
                            String real_people = dat.getString("real_people");
                            String fake_people = dat.getString("fake_people");




                            if (real==false){
                                fake_Pnumber = Integer.parseInt(fake_people) + 1;
                                real_Pnumber = Integer.parseInt(real_people);
                                avg = Math.round((Double.parseDouble(fake_p) + confidancelvl)
                                        / fake_Pnumber * 10) / 10.0;
                                same = Double.parseDouble(real_p);
                                up = Double.parseDouble(fake_p) + confidancelvl;
                                double real = Math.round((same / real_Pnumber) * 10) / 10.0;
                                fakeConfidance.setText("with " + avg + "% confidance");
                                realConfidance.setText("with " + real + "% confidance");

                            }else{
                                real_Pnumber = Integer.parseInt(real_people) + 1;
                                fake_Pnumber = Integer.parseInt(fake_people);
                                avg = Math.round((Double.parseDouble(real_p) + confidancelvl) /
                                        real_Pnumber *10) /10.0;
                                same = Double.parseDouble(fake_p);
                                up = Double.parseDouble(real_p) + confidancelvl;
                                double fake = Math.round((same / fake_Pnumber) * 10) / 10.0;
                                realConfidance.setText("with " + avg + "% confidance");
                                fakeConfidance.setText("with " + fake + "% confidance");

                            }


                            thinkFake.setText(fake_Pnumber + " people think this is fake");
                            thinkReal.setText(real_Pnumber + " people think this is real");
                            sendData();

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

    public void sendData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, sendDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametar = new HashMap<String, String>();
                parametar.put("img_id", String.valueOf(id));
                if(real==false){
                    parametar.put("fake_p", String.valueOf(up));
                    parametar.put("real_p", String.valueOf(same));

                }else {
                    parametar.put("real_p", String.valueOf(up));
                    parametar.put("fake_p", String.valueOf(same));
                }
                parametar.put("real_people", String.valueOf(real_Pnumber));
                parametar.put("fake_people", String.valueOf(fake_Pnumber));

                return parametar;
            }
        };
        requestQueue.add(request);
    }

}

