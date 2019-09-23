package com.sag.atwork;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class worker_select extends AppCompatActivity {


    ListView listView;
    com.sag.atwork.Adapter adapter;
    String work,line,m,username,location,photo,wid,starRating;
    ProgressDialog pd;
    EditText search1;
    FrameLayout frameLayout;
    ArrayList<worker> list2;
    String search;
    public void search(){

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> asyncTask= new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                pd=new ProgressDialog(worker_select.this);
                pd.setMessage("please wait..");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();

            }

            @Override
            protected Void doInBackground(Void... voids) {
                List<NameValuePair> list =new ArrayList<>();
                list.add(new BasicNameValuePair("workertype",search));

                try {
                    HttpClient client=new DefaultHttpClient();
                    HttpPost post = new HttpPost(Urls.search_worker);
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
                    JSONArray array=new JSONArray(m);

                    for(int i=0;i<array.length();i++){
                        JSONObject object=array.getJSONObject(i);
                        username=object.getString("name");
                        location=object.getString("address");
                        work=object.getString("workertype");
                        photo=object.getString("photo");
                        wid=object.getString("id");
                        starRating=object.getString("rating");
                        list2.add(new worker(username,location,work,photo,wid,starRating));

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(pd.isShowing())
                    pd.dismiss();


                if(m.contains("error")){
                    frameLayout.setVisibility(View.VISIBLE);

                }
                else {
                    frameLayout.setVisibility(View.INVISIBLE);
                    adapter = new com.sag.atwork.Adapter(worker_select.this,list2);
                    listView.setAdapter(adapter);
                }

            }
        };
        asyncTask.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        frameLayout=findViewById(R.id.frameempty);
        search1=findViewById(R.id.search);
        listView= findViewById(R.id.ListView);
        list2 = new ArrayList<>();
        Intent i = getIntent();
        search =i.getStringExtra("search");


        search();
        search1.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }
}

