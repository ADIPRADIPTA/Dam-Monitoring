package com.tugasakhir.adi.dmobile;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Monitoring extends AppCompatActivity {
    String Ketinggian;
    String Status;
    String Volume;
    boolean loaddata=false;
    TextView tvKetinggian, tvStatus, tvVolume;
    String JSON_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            GetJSONData getjsondata = new GetJSONData();
                            getjsondata.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);

//        Runnable refresh = new Runnable() {
//            @Override
//            public void run() {
//                new GetJSONData().execute();
//                handler.postDelayed(this, 4000);
//            }
//        };
//        timer.schedule(doAsynchronousTask, 0, 5000);
//        handler.postDelayed(refresh, 1000);
    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(LaurensiusSystemFramework.TAG, "Do in background");
            ServiceHandler sh = new ServiceHandler();
            Random r = new Random();
            int angka_acak = (r.nextInt(100) + 9999999);
            String url = getResources().getString(R.string.default_server).concat(getResources().getString(R.string.default_directory)).concat(String.valueOf(angka_acak)).concat("/");
            Log.d("URL :",url);
            JSON_data = sh.makeServiceCall(url, ServiceHandler.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject dataset = jsonObj.getJSONObject(LaurensiusSystemFramework.JSON_OBJ_DATASET);
                    JSONArray last_data = dataset.getJSONArray(LaurensiusSystemFramework.JSON_ARR_LAST_DATA);
                    JSONObject last_data_object = last_data.getJSONObject(0);
                    Ketinggian = last_data_object.getString("ketinggian").concat(" cm");
                    Status = last_data_object.getString("status");
                    Volume = last_data_object.getString("volume").concat(" cm³");
                } catch (final JSONException e) {
                    Log.e(LaurensiusSystemFramework.TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(LaurensiusSystemFramework.TAG, "JSON data monitoring : " + JSON_data);
            JSON_data = null;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loaddata) {
                setContentView(R.layout.activity_monitoring);
                tvKetinggian = (TextView)findViewById(R.id.tvValueKetinggianAir);
                tvStatus = (TextView)findViewById(R.id.tvValueStatusAir);
                tvVolume = (TextView)findViewById(R.id.tvValueVolume);
                tvKetinggian.setText(Ketinggian);
                tvStatus.setText(Status);
                tvVolume.setText(Volume);
            }
            else{
                setContentView(R.layout.activity_badkoneksi);
            }
        }
    }
}
