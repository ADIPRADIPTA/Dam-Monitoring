package com.tugasakhir.adi.dmobile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceNotifikasi extends Service {

    boolean init = true;
    boolean load_data;
    String Ketinggian, Status, Notif, Volume;
    String Status_terakhir = null;
    public ServiceNotifikasi() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Notifikasi ON", Toast.LENGTH_LONG).show();
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
                            Log.d("On Runable :", "OK");
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Notifikasi OFF", Toast.LENGTH_LONG).show();
    }

    private class GetJSONData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String svr = getApplicationContext().getResources().getString(R.string.default_server);
            String dir = getApplicationContext().getResources().getString(R.string.default_directory);
            String url = svr.concat(dir);
            String JSON_data = sh.makeServiceCall(url, ServiceHandler.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject dataset = jsonObj.getJSONObject(LaurensiusSystemFramework.JSON_OBJ_DATASET);
                    JSONArray last_data = dataset.getJSONArray(LaurensiusSystemFramework.JSON_ARR_LAST_DATA);
                    JSONObject last_data_object = last_data.getJSONObject(0);
                    Ketinggian = last_data_object.getString("ketinggian");
                    Status = last_data_object.getString("status");
                    Volume = last_data_object.getString("volume");
                    Notif = last_data_object.getString("notif");
                    Log.d("Dari dalam Service :",JSON_data);
                } catch (final JSONException e) {
                    Log.e(LaurensiusSystemFramework.TAG, e.getMessage());
                }
                load_data = true;
            }else{
                load_data = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(load_data){
                if(init==true){
                    Status_terakhir = Notif;
                    init = false;
                }else
                if(init==false){
                    Log.d("Async  :",Status);
                    if(!Status_terakhir.equals(Notif) && !Status.equals("2")){
                        Notification.Builder builder = new Notification.Builder(getApplication().getBaseContext());
                        Intent notificationIntent = new Intent(getApplication().getBaseContext(),Monitoring.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
                        builder.setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Status Ketinggian : " + Status + "!")
                                .setContentText("Ketinggian : " + Ketinggian +" cm || Volume" + Volume + " cm³")
                                .setContentIntent(pendingIntent);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(alarmSound);
                        NotificationManager notificationManager = (NotificationManager) getApplication().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = builder.getNotification();
                        notificationManager.notify(R.drawable.notification_template_icon_bg, notification);
                    }
                    Status_terakhir = Notif;
                }
            }
        }
    }
}
