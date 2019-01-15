package com.example.meowmeow.youtubekids.Model;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Surface;
import android.widget.Toast;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meowmeow.youtubekids.Adapter.PlayVideoAdapter;
import com.example.meowmeow.youtubekids.Interface.PlayVideo;
import com.example.meowmeow.youtubekids.ScreenToFaceDistance.CameraSurfaceView;
import com.example.meowmeow.youtubekids.ScreenToFaceDistance.message.MeasurementStepMessage;
import com.example.meowmeow.youtubekids.ScreenToFaceDistance.message.MessageHUB;
import com.example.meowmeow.youtubekids.ScreenToFaceDistance.message.MessageListener;


import com.example.meowmeow.youtubekids.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayVideoYTB extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {

    //youtube
    String API_KEYVIDEO = "AIzaSyA2e3uG6u3_fWeh_KNIS4UN5bZPD2FiDxM";
    int REQUEST_VIDEO = 123;
    YouTubePlayerView youTubePlayerView;
    //list video
    public RecyclerView recyclerView;
    ArrayList<PlayVideo> playVideoArrayList = new ArrayList<>();
    PlayVideoAdapter playVideoAdapter;

    //Khai báo keyplaylist
    private String API_KEYPLAYLIST ="AIzaSyAI6YiDW8IaP6bVYSLTPyih2uNX0PWNyn0";
    // khai báo keyid
    private String ID_PLAYLIST = "PLx9-XYVFVnB0GG3MYSueXcdAoJa5i5OM0";
    // link lấy danh sách video từ playlist id
    public String urlYTB = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+ID_PLAYLIST+"&key="+API_KEYPLAYLIST;

    //    //camera
//    public static final String CAM_SIZE_WIDTH = "intent_cam_size_width";
//    public static final String CAM_SIZE_HEIGHT = "intent_cam_size_height";
//    public static final String AVG_NUM = "intent_avg_num";
//    public static final String PROBANT_NAME = "intent_probant_name";
//    Double checkDis,checkHandler = 0.0;
//
//    private CameraSurfaceView _mySurfaceView;
//    Camera _cam = Camera.open(1);
//    private final static DecimalFormat _decimalFormater = new DecimalFormat(
//            "0.0");
//
//    private float _currentDevicePosition;
//
//    private int _cameraHeight;
//    private int _cameraWidth;
//    private int _avgNum;
//    int MY_REQUEST_CODE = 123;
//    Handler handler = new Handler();
//    Runnable runnable;
//
//    TextView _currentDistanceView;
//    Button _calibrateButton;
//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_ytb);
        //youtube
        youTubePlayerView = findViewById(R.id.myYTBView);
        youTubePlayerView.initialize(API_KEYVIDEO, this);

        recyclerView = findViewById(R.id.recycleview_playYTB);
        GetYTBJson(urlYTB);
//        //camerasurface
//        _mySurfaceView = (CameraSurfaceView) findViewById(R.id.surface_camera);
//
//        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
//                (int) (0.95 * this.getResources().getDisplayMetrics().widthPixels),
//                (int) (0.6 * this.getResources().getDisplayMetrics().heightPixels));
//
//        layout.setMargins(0, (int) (0.05 * this.getResources()
//                .getDisplayMetrics().heightPixels), 0, 0);
//
//        _mySurfaceView.setLayoutParams(layout);
//        _currentDistanceView = (TextView) findViewById(R.id.currentDistance);
//        _calibrateButton = (Button) findViewById(R.id.calibrateButton);
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if(checkDis == checkHandler)
//                {
////                    Toast.makeText(MainActivity.this, "Qua gan", Toast.LENGTH_SHORT).show();
//                    Log.d("BBB","Qua gan");
//                }
//                else{
//                    checkDis = checkHandler;
//                }
//                handler.postDelayed(runnable,5000);
//            }
//        };
//        handler.postDelayed(runnable,20000);
//    }
    }
    //youtube
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        String results = getIntent().getExtras().getString("videoId");
//        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        youTubePlayer.cueVideo(results);
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(PlayVideoYTB.this, REQUEST_VIDEO );
        }
        else {
            Toast.makeText(this, "Error!!!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_VIDEO){
            youTubePlayerView.initialize(API_KEYVIDEO, PlayVideoYTB.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

                                playVideoArrayList.add(new PlayVideo(title,urlvideo,idvideo));
                            }
                            playVideoAdapter = new PlayVideoAdapter(getApplicationContext(),R.layout.item_custom_video,playVideoArrayList);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(playVideoAdapter);
                            playVideoAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(MusicMovie.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlayVideoYTB.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

//    //camera
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//        MessageHUB.get().registerListener(this);
//        // _audioManager.registerMediaButtonEventReceiver(_headSetButtonReceiver);
//
//        // 1 for front cam. No front cam ? Not my fault!
//        try{
//
//
//
//
//        }catch (Exception ex)
//        {
//
//        }
//        Camera.Parameters param = _cam.getParameters();
//
//        // Find the best suitable camera picture size for your device. Competent
//        // research has shown that a smaller size gets better results up to a
//        // certain point.
//        // http://ieeexplore.ieee.org/xpl/login.jsp?tp=&arnumber=6825217&url=http%3A%2F%2Fieeexplore.ieee.org%2Fiel7%2F6816619%2F6825201%2F06825217.pdf%3Farnumber%3D6825217
//        List<Camera.Size> pSize = param.getSupportedPreviewSizes();
//        double deviceRatio = (double) this.getResources().getDisplayMetrics().widthPixels
//                / (double) this.getResources().getDisplayMetrics().heightPixels;
//
//        Camera.Size bestSize = pSize.get(0);
//        double bestRation = (double) bestSize.width / (double) bestSize.height;
//
//        for (Camera.Size size : pSize) {
//            double sizeRatio = (double) size.width / (double) size.height;
//
//            if (Math.abs(deviceRatio - bestRation) > Math.abs(deviceRatio
//                    - sizeRatio)) {
//                bestSize = size;
//                bestRation = sizeRatio;
//            }
//        }
//       _cameraHeight = 0;
//       _cameraWidth = 0;
//
////        Log.d("PInfo", _cameraWidth + " x " + _cameraHeight);
//
//        param.setPreviewSize(_cameraWidth, _cameraHeight);
////        _cam.setParameters(param);
//        _cam.startPreview();
//        _mySurfaceView.setCamera(_cam);
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        MessageHUB.get().unregisterListener(this);
//
//        // _audioManager
//        // .unregisterMediaButtonEventReceiver(_headSetButtonReceiver);
//        resetCam();
//    }
//    /**
//     * Sets the current eye distance to the calibration point.
//     *
//     * @paramv
//     */
//    public void pressedCalibrate(final View v) {
//
//        if (!_mySurfaceView.isCalibrated()) {
//            _mySurfaceView.calibrate();
//        }
//    }
//
//    public void pressedReset(final View v) {
//
//        if (_mySurfaceView.isCalibrated()) {
//            _mySurfaceView.reset();
//        }
//    }
//
//    public void onShowMiddlePoint(final View view) {
//        // Is the toggle on?
//        boolean on = ((Switch) view).isChecked();
//        _mySurfaceView.showMiddleEye(on);
//    }
//
//    public void onShowEyePoints(final View view) {
//        // Is the toggle on?
//        boolean on = ((Switch) view).isChecked();
//        _mySurfaceView.showEyePoints(on);
//    }
//
//    public void updateUI(final MeasurementStepMessage message) {
//
//        float results = message.getDistToFace();
//        if(results >0 && results <= 25)
//        {
//            finish();
//            //Toast.makeText(this, "ABC", Toast.LENGTH_SHORT).show();
//        }else {
//            _currentDistanceView.setText(_decimalFormater.format(message
//                    .getDistToFace()) + " cm");
//            float fontRatio = message.getDistToFace() / 29.7f;
//            _currentDistanceView.setTextSize(fontRatio * 20);
//        }
//
//        try{
//            checkDis = Double.parseDouble(_decimalFormater.format(message.getDistToFace()));}
//        catch (Exception ex){
//
//        }
//
////        if(Double.parseDouble(_decimalFormater.format(message
////                .getDistToFace())) < 20)
////        {
////            Toast.makeText(this, "Quá gần màn hình", Toast.LENGTH_SHORT).show();
////        }
//
//    }
//
//    private void resetCam() {
//        _mySurfaceView.reset();
//
//        _cam.stopPreview();
//        _cam.setPreviewCallback(null);
//        _cam.release();
//    }
//
//    @Override
//    public void onMessage(int messageID, Object message) {
//        switch (messageID) {
//
//            case MessageHUB.MEASUREMENT_STEP:
//                updateUI((MeasurementStepMessage) message);
//                break;
//
//            case MessageHUB.DONE_CALIBRATION:
//                break;
//            default:
//                break;
//        }
//    }

}
