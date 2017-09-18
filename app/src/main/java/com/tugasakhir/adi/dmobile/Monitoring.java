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

import java.util.Timer;
import java.util.TimerTask;

public class Monitoring extends AppCompatActivity {
    String Ketinggian;
    String Status;
    boolean loaddata=false;
    TextView tvKetinggian, tvStatus;

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
        timer.schedule(doAsynchronousTask, 0, 3000);
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
            String url = MainActivity.server.concat(MainActivity.directory);
            String JSON_data = sh.makeServiceCall(url, ServiceHandler.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject dataset = jsonObj.getJSONObject(LaurensiusSystemFramework.JSON_OBJ_DATASET);
                    JSONArray last_data = dataset.getJSONArray(LaurensiusSystemFramework.JSON_ARR_LAST_DATA);
                    JSONObject last_data_object = last_data.getJSONObject(0);
                    Ketinggian = last_data_object.getString("ketinggian").substring(0,5).concat(" CM dari dasar bendungan");
                    Status = last_data_object.getString("status");
                } catch (final JSONException e) {
                    Log.e(LaurensiusSystemFramework.TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(LaurensiusSystemFramework.TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loaddata) {
                setContentView(R.layout.activity_monitoring);
                tvKetinggian = (TextView)findViewById(R.id.tvValueKetinggianAir);
                tvStatus = (TextView)findViewById(R.id.tvValueStatusAir);
                tvKetinggian.setText(Ketinggian);
                tvStatus.setText(Status);
            }
            else{
                setContentView(R.layout.activity_badkoneksi);

            }

        }
    }
}
