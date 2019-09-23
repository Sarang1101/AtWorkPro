package com.sag.atwork;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    Button back,edit,aupdate;
    TextView name,email,address,dob,phone,headname;
    EditText aname,aemail,aaddress,aphone,apass;
    ImageView photo;
    View view;
    ProgressDialog pd;
    AlertDialog.Builder alertdialog;
    String line,m,name1,address1,phone1,dob1,email1,message,photo1;
    SharedPreferences preferences;
    AlertDialog dialog;

    public void asyncPOST(){
        @SuppressLint("staticFieldLeak")AsyncTask<Void,Void,Void>
                asyncTask=new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("Id", preferences.getString("id", null)));

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Urls.getuserdata);
                    post.setEntity(new UrlEncodedFormEntity(list));
                    HttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    m = builder.toString();
                    JSONObject object = new JSONObject(m);
                    name1 = object.getString("Name");
                    email1 = object.getString("Email");
                    address1 = object.getString("Address");
                    dob1 = object.getString("DOB");
                    phone1 = object.getString("Phone");
                    photo1= object.getString("Photo");
                    message=object.getString("message");
                } catch (Exception e) {
                    Log.d("error", e.getMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if (message.equals("successful")) {
                    name.setText(name1);
                    email.setText(email1);
                    address.setText(address1);
                    dob.setText(dob1);
                    phone.setText(phone1);
                    Picasso.get().load(Urls.ip+photo1).into(photo);
                    headname.setText(name1);
                }
            }
        };
        asyncTask.execute();
    }

    private  void asyncUPDATE(){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask= new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                pd=new ProgressDialog(Profile.this);
                pd.setMessage("please wait..");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();

            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<NameValuePair> list =new ArrayList<>();
                list.add(new BasicNameValuePair("Email",aemail.getText().toString()));
                list.add(new BasicNameValuePair("Username",aname.getText().toString()));
                list.add(new BasicNameValuePair("Address",aaddress.getText().toString()));
                list.add(new BasicNameValuePair("Phone",aphone.getText().toString()));
               list.add(new BasicNameValuePair("Password",apass.getText().toString()));
                list.add(new BasicNameValuePair("Id", preferences.getString("id", null)));


                try {
                    HttpClient client=new DefaultHttpClient();
                    HttpPost post= new HttpPost(Urls.update);
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
                }catch (Exception e){
                    Toast.makeText(Profile.this, "exception", Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(pd.isShowing()) {
                    pd.dismiss();

                }
                Toast.makeText(Profile.this,m , Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                asyncPOST();
            }
        };
        asyncTask.execute();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        preferences=getSharedPreferences("login",MODE_PRIVATE);
        setSupportActionBar(toolbar);
        back=findViewById(R.id.back);
        name=findViewById(R.id.name);
        aname=findViewById(R.id.aname);
        aemail=findViewById(R.id.aemail);
        aaddress=findViewById(R.id.aaddress);
        aphone=findViewById(R.id.aphone);

        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        photo=findViewById(R.id.image);
        phone=findViewById(R.id.phone);
        edit=findViewById(R.id.edit);
        dob=findViewById(R.id.dob);
        headname=findViewById(R.id.headname);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                alertdialog= new AlertDialog.Builder(Profile.this);
                alertdialog.setTitle("\t Edit Profile");
                view=inflater.inflate(R.layout.alertdialog,null);
                alertdialog.setView(view);
                aupdate=view.findViewById(R.id.aupdate);
                aname=view.findViewById(R.id.aname);
                aname.setText(name1);
                apass=view.findViewById(R.id.apass);
                aemail=view.findViewById(R.id.aemail);
                aemail.setText(email1);
                aaddress=view.findViewById(R.id.aaddress);
                aaddress.setText(address1);
                aphone=view.findViewById(R.id.aphone);
                aphone.setText(phone1);
                dialog= alertdialog.create();
                dialog.show();
                aupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncUPDATE();

                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Home.class));
            }
        });

        asyncPOST();

    }
}
