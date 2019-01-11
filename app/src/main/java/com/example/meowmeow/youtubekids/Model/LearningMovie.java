package com.example.meowmeow.youtubekids.Model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meowmeow.youtubekids.Adapter.ExploreVideoAdapter;
import com.example.meowmeow.youtubekids.Adapter.LearningVideoAdapter;
import com.example.meowmeow.youtubekids.Adapter.MusicVideoApdater;
import com.example.meowmeow.youtubekids.Interface.ExploreVideo;
import com.example.meowmeow.youtubekids.Interface.LearningVideo;
import com.example.meowmeow.youtubekids.Interface.MusicVideo;
import com.example.meowmeow.youtubekids.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LearningMovie extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    ImageButton img_recommend,img_explore, img_shows, img_music, img_search;
    CircularImageView img_user;

    private String API_KEYPLAYLIST = "AIzaSyAI6YiDW8IaP6bVYSLTPyih2uNX0PWNyn0";
    private String ID_PLAYLIST = "PLNI3rMEOpVxmWIeT7XEuA0xIQXAyECmuV";

    RecyclerView recyclerView;
    ArrayList<LearningVideo> learningMovieArrayList = new ArrayList<>();
    LearningVideoAdapter learningVideoAdapter;

    // link lấy danh sách video từ playlist id
    public String urlYTB = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+ID_PLAYLIST+"&key="+API_KEYPLAYLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_movie);
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
        img_recommend = findViewById(R.id.img_recommend);
        img_shows = findViewById(R.id.img_shows);
        img_explore = findViewById(R.id.img_explore);
        img_music = findViewById(R.id.img_music);
        img_search = findViewById(R.id.img_search);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_learning);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_explore:
                Intent intent = new Intent(LearningMovie.this, ExplorerMovie.class);
                startActivity(intent);
                finish();
                break;
            case R.id.img_shows:
                Intent intent2 = new Intent(LearningMovie.this,ShowsMovie.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.img_music:
                Intent intent3 = new Intent(LearningMovie.this, MusicMovie.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.img_recommend:
                Intent intent4 = new Intent(LearningMovie.this, RecommendedMovie.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.img_avatar:
                Intent intent5 = new Intent(LearningMovie.this, UserActivity.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.img_search:
                Intent intent6 = new Intent(LearningMovie.this, SearchMovie.class);
                startActivity(intent6);
                finish();
                break;
        }
    }

    private void ControlButton() {
        img_user.setOnClickListener(this);
        img_explore.setOnClickListener(this);
        img_recommend.setOnClickListener(this);
        img_shows.setOnClickListener(this);
        img_music.setOnClickListener(this);
        img_search.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_learning);
    }

    private void GetYTBJson(final String url) {
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

                                learningMovieArrayList.add(new LearningVideo(title,urlvideo,idvideo));
                            }
                            learningVideoAdapter = new LearningVideoAdapter(getApplicationContext(),R.layout.item_custom_video,learningMovieArrayList);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(learningVideoAdapter);
                            learningVideoAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(MusicMovie.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LearningMovie.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
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

}
