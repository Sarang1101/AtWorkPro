package com.sag.atwork;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class book_worker extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rel_view,rel_edit,rel_pause,rel_cancel;
    ImageView imageView;
    TextView view,pause,edit,cancel,name,type,time,close,name_rel,typre_rel,date_rel;
    String line,m,message;
    FrameLayout frame;
    AlertDialog dialog;
    String id;
    Button cancelbtn;
    View view_inflater,pause_inflater,edit_inflater,cancel_inflater;
    LayoutInflater inflater;


    public void alertdialog(View v) {
        AlertDialog.Builder builder1= new AlertDialog.Builder(book_worker.this);
        builder1.setView(v);
        builder1.setCancelable(false);
         dialog= builder1.create();
        dialog.show();
        close=v.findViewById(R.id.close_alert);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private  void asyncPOST(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask= new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                frame.setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<NameValuePair> list =new ArrayList<>();
                list.add(new BasicNameValuePair("id",id));
                try{
                    HttpClient client=new DefaultHttpClient();
                    HttpPost post= new HttpPost(Urls.view_book_worker);
                    post.setEntity(new UrlEncodedFormEntity(list));
                    HttpResponse response=client.execute(post);
                    HttpEntity entity=response.getEntity();
                    InputStream is=entity.getContent();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);
                    StringBuilder builder=new StringBuilder();
                    while ((line=reader.readLine())!=null)
                    {
                        builder.append(line);
                    }
                    m=builder.toString();
                    JSONObject object= new JSONObject(m);
                    message=object.getString("message");

                    //TODO Replace

                    if (object.getString("message").equals("successful")){

                        Log.d("message","successful");

                    }

                }catch (Exception e){
                    Log.d("error",e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(book_worker.this, message, Toast.LENGTH_SHORT).show();
                frame.setVisibility(View.INVISIBLE);
               /* if (message.equalsIgnoreCase("Successful")){
                    startActivity(new Intent(book_worker.this,Home.class));
                    finish();
                }*/

            }
        };
        asyncTask.execute();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_worker);
        imageView=findViewById(R.id.image_rel);
        view=findViewById(R.id.view);
        pause=findViewById(R.id.pause);
        edit=findViewById(R.id.edit);
        rel_view=findViewById(R.id.rel1);
        rel_edit=findViewById(R.id.rel2);
        rel_pause=findViewById(R.id.rel3);
        rel_cancel=findViewById(R.id.rel4);
        cancel=findViewById(R.id.cancel);
        name=findViewById(R.id.name_rel);
        type=findViewById(R.id.type_rel);
        time=findViewById(R.id.time);
        close=findViewById(R.id.close_alert);
        rel_view.setOnClickListener(this);
        rel_edit.setOnClickListener(this);
        rel_pause.setOnClickListener(this);
        rel_cancel.setOnClickListener(this);

         inflater= getLayoutInflater();




/*
        Intent intent = getIntent();
        String name1 = intent.getStringExtra("name");
        String type1 = intent.getStringExtra("type");
        String date1= intent.getStringExtra("date");
        name.setText("name1");
        type.setText("type1");
        time.setText("date1");*/


        //id=i.getStringExtra("id");

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.rel1){
            view_inflater=inflater.inflate(R.layout.alert_view,null);
            alertdialog(view_inflater);

        }
        else if(v.getId()==R.id.rel2){
            edit_inflater=inflater.inflate(R.layout.alert_edit,null);

            alertdialog(edit_inflater);

        }
        else if(v.getId()==R.id.rel3){
            pause_inflater=inflater.inflate(R.layout.alert_pause,null);
            cancelbtn=pause_inflater.findViewById(R.id.cancel_btn);
            alertdialog(pause_inflater);

        }
        else if(v.getId()==R.id.rel4){
            cancel_inflater=inflater.inflate(R.layout.alert_cancelbooking,null);
            cancelbtn=cancel_inflater.findViewById(R.id.cancel_btn);
            alertdialog(cancel_inflater);

        }
    }
}
