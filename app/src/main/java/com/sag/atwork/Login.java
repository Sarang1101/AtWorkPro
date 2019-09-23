package com.sag.atwork;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class Login extends AppCompatActivity {
    EditText email,pass;
    Button login;
    FrameLayout frame;
    String line,m,message;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView lgn;
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
                list.add(new BasicNameValuePair("Email",email.getText().toString()));
                list.add(new BasicNameValuePair("Password",pass.getText().toString()));

                try {
                    HttpClient client=new DefaultHttpClient();
                    HttpPost post= new HttpPost(Urls.login);
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

                    if (object.getString("message").equals("successful")){
                        editor.putString("email",email.getText().toString());
                        editor.putString("password",pass.getText().toString());
                        editor.putString("id",object.getString("id"));
                        editor.putString("photo",object.getString("Photo"));
                        editor.putString("user_type",object.getString("user_type"));
                        editor.apply();
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
                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
               frame.setVisibility(View.INVISIBLE);
               if (message.equalsIgnoreCase("Successful")){
                   startActivity(new Intent(Login.this,Home.class));
                   finish();
               }

            }
        };
        asyncTask.execute();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lgn=findViewById(R.id.lgn);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        preferences=getSharedPreferences("login",MODE_PRIVATE);

        editor=preferences.edit();
        frame=findViewById(R.id.frame);
        login=findViewById(R.id.login);
        if(preferences.getString("id",null)!=null){
            startActivity(new Intent(Login.this,Home.class));

        }
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (email.getText().toString().equals("")) {
                    email.setError("enter registered email");
                } else if (pass.getText().toString().equals("")) {
                    pass.setError("enter your password");
                }
                else{
                    asyncPOST();
                }
            }
        });
        Toast.makeText(this,Urls.login, Toast.LENGTH_SHORT).show();

    }
}
