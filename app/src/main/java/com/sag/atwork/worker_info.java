package com.sag.atwork;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
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


public class worker_info extends AppCompatActivity {

    ProgressDialog pd;
    ImageView photo;
    Button back,book,review;
    TextView name,rating,address,phone,email;
    String m,line,name1,address1,phone1,rating1,message,photo1,email1;
    String wid;

    public void asyncPOST(){
        @SuppressLint("staticFieldLeak") AsyncTask<Void,Void,Void>
                asyncTask=new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("id",wid));

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Urls.worker_info);
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
                    name1 = object.getString("name");
                    address1 = object.getString("address");
                    phone1= object.getString("Phone");
                    rating1 = object.getString("rating");
                    message=object.getString("message");
                    photo1=object.getString("Photo");
                    email1=object.getString("email");
                } catch (Exception e) {
                    Log.d("error", e.getMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                   name.setText(name1);
                    address.setText(address1);
                    phone.setText(phone1);
                    email.setText(email1);
                    rating.setText(rating1+"/5.0");
                Picasso.get().load(Urls.ip+photo1).into(photo);
                Toast.makeText(getApplicationContext() ,m, Toast.LENGTH_SHORT).show();

            }
        };
        asyncTask.execute();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_info);
        back=findViewById(R.id.back);
        book=findViewById(R.id.book);
        review=findViewById(R.id.review);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        photo=findViewById(R.id.image);
        rating=findViewById(R.id.rating);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wid = getIntent().getStringExtra("id");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(worker_info.this,Home.class);
                startActivity(in);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(worker_info.this,book_info.class);
                i.putExtra("name",name.getText().toString());
                i.putExtra("address",address.getText().toString());
                i.putExtra("rating",rating.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",phone.getText().toString());
                i.putExtra("photo",photo1);
                startActivity(i);
            }
        });

         if (!wid.equals("")) {

             asyncPOST();

         }

    }
}
