package com.sag.atwork.fragments;

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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sag.atwork.R;
import com.sag.atwork.Urls;
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

public class search_results extends Fragment {
    Adapter adapter;
    ProgressDialog pd;
    ImageView photo;
    SharedPreferences preferences;
    TextView name,rating,address,phone;
    String m,search,line,name1,address1,phone1,rating1,message,photo1;
    ListView list;

    public void asyncPOST(){
        @SuppressLint("staticFieldLeak") AsyncTask<Void,Void,Void>
                asyncTask=new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("workertype", search));

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(Urls.search_worker);
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
                    phone1= object.getString("phone");
                    rating1 = object.getString("rating");
                    message=object.getString("message");
                    phone1=object.getString("photo");
                } catch (Exception e) {
                    Log.d("error", e.getMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {


                    /*name.setText(name1);
                    address.setText(address1);
                    phone.setText(phone1);
                    rating.setText(rating1);*/
                   // Picasso.get().load(Urls.ip+photo1).into(photo);
                    Toast.makeText(getContext() ,m, Toast.LENGTH_SHORT).show();

            }
        };
        asyncTask.execute();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_result,container,false);
        list=v.findViewById(R.id.ListView);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            search =bundle.getString("search",null);

        }
        asyncPOST();
        return v;
    }

}

