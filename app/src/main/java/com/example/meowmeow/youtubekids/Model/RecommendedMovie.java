package com.example.meowmeow.youtubekids.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meowmeow.youtubekids.Adapter.LearningVideoAdapter;
import com.example.meowmeow.youtubekids.Adapter.MusicVideoApdater;
import com.example.meowmeow.youtubekids.Adapter.RecommendedVideoAdapter;
import com.example.meowmeow.youtubekids.Interface.LearningVideo;
import com.example.meowmeow.youtubekids.Interface.MusicVideo;
import com.example.meowmeow.youtubekids.Interface.RecommendedVideo;
import com.example.meowmeow.youtubekids.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class RecommendedMovie extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    ImageButton img_explore,img_learning, img_shows, img_music, img_search, img_recommended;
    CircularImageView img_user;
    Context context;
    public RecyclerView recyclerView;
    ArrayList<RecommendedVideo> recommendedVideoArrayList = new ArrayList<>();
    RecommendedVideoAdapter recommendedVideoAdapter;

    //Khai báo keyplaylist
    private String API_KEYPLAYLIST ="AIzaSyAI6YiDW8IaP6bVYSLTPyih2uNX0PWNyn0";
    // khai báo keyid
    private String ID_PLAYLIST = "PLx9-XYVFVnB0GG3MYSueXcdAoJa5i5OM0";
    //private String Base_URL = "https://www.googleapis.com/youtube/v3/";
    // link lấy danh sách video từ playlist id
    public String urlYTB = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+ID_PLAYLIST+"&key="+API_KEYPLAYLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_movie);
        //ánh xạ đến view để hiển thị
        AnhXa();
        //lấy dữ liệu từ youtube
        GetYTBJson(urlYTB);
        //sự kiện click của button
        ControlButton();
        //lấy dữ liệu từ sharepreferences
        GetPreferences();
        //Cảm biến để đo khoảng cách trong android
        SensorKidsTV();
    }

    private void AnhXa() {
        img_user = findViewById(R.id.img_avatar);
        img_explore = findViewById(R.id.img_explore);
        img_learning = findViewById(R.id.img_learning);
        img_shows = findViewById(R.id.img_shows);
        img_music = findViewById(R.id.img_music);
        img_search = findViewById(R.id.img_search);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_recommended);
    }

    private void ControlButton() {
        img_user.setOnClickListener(this);
        img_explore.setOnClickListener(this);
        img_music.setOnClickListener(this);
        img_shows.setOnClickListener(this);
        img_learning.setOnClickListener(this);
        img_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_avatar:
                Intent intent = new Intent(RecommendedMovie.this, UserActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.img_explore:
                Intent intent2 = new Intent(RecommendedMovie.this, ExplorerMovie.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.img_music:
                Intent intent3 = new Intent(RecommendedMovie.this,MusicMovie.class);
                startActivity(intent3);
                break;
            case R.id.img_shows:
                Intent intent4 = new Intent(RecommendedMovie.this,ShowsMovie.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.img_learning:
                Intent intent5 = new Intent(RecommendedMovie.this,LearningMovie.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.img_search:
                Intent intent6 = new Intent(RecommendedMovie.this,SearchMovie.class);
                startActivity(intent6);
                finish();
                break;
        }
    }

    //lấy dữ liệu bằng sharepreferences
    public void GetPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_name",MODE_PRIVATE);
        String AvataValue = sharedPreferences.getString("avatar","");
        if(!AvataValue.equals("")){
            img_user.setImageBitmap(decodeBase64(AvataValue));
        }
    }

    //hàm lấy hình ảnh thành bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void SensorKidsTV(){
        SensorManager sensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor proximitySensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            finish(); // Close app
        }
        // Create listener
        SensorEventListener proximitySensorListener = new SensorEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    params.screenBrightness = 0;
                    params.getColorMode();
                    getWindow().setAttributes(params);

                } else {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    params.screenBrightness = 0.9f;
                    getWindow().setAttributes(params);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        // Register it, specifying the polling interval in
        // microseconds
        sensorManager.registerListener(proximitySensorListener,
                proximitySensor, 2 * 1000 * 1000);
    }

    private void GetYTBJson(final String url) {
        Log.d("BBB",url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            String title ="";
                            String urlvideo ="";
                            String idvideo = "";
                            for(int i = 0; i < jsonItems.length();i++)
                            {
                                JSONObject jsonObject = jsonItems.getJSONObject(i);
                                JSONObject jsonSnippet = jsonObject.getJSONObject("snippet");
                                title = jsonSnippet.getString("title");
                                JSONObject jsonThumbnails = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnails.getJSONObject("medium");
                                urlvideo = jsonMedium.getString("url");
                                JSONObject jsonResource = jsonSnippet.getJSONObject("resourceId");
                                idvideo = jsonResource.getString("videoId");

                                recommendedVideoArrayList.add(new RecommendedVideo(title,urlvideo,idvideo));
                            }
                            recommendedVideoAdapter = new RecommendedVideoAdapter(getApplicationContext(),R.layout.item_custom_video,recommendedVideoArrayList);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(recommendedVideoAdapter);
                            recommendedVideoAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(MusicMovie.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecommendedMovie.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
