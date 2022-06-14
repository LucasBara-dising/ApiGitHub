package com.example.apigithub;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        MyThread thread = new MyThread(context, appWidgetManager, appWidgetId);
        thread.start();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    static class MyThread extends Thread{
        Context context;
        AppWidgetManager manager;
        int widgetId;

        public MyThread(Context context, AppWidgetManager manager, int widgetId){
            this.context = context;
            this.manager = manager;
            this.widgetId = widgetId;
        }

        @Override
        public void run(){
            String data = "";
            try {
                //usa o api pra fazer uma consulta no git hub pelo user
                String urlBase="https://api.github.com/users/";
                String user="lucasbara-dising";

                URL url = new URL(urlBase+user);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader r = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(),
                                    "utf-8"));
                    data = r.readLine();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //mostra o nome do user
                JSONObject jsonObject = new JSONObject(data);
                @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

                String str = jsonObject.getString("name");
                remoteViews.setTextViewText(R.id.appwidget_text, str);
                manager.updateAppWidget(widgetId, remoteViews);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
