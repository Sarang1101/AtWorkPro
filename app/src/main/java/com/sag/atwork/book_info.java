package com.sag.atwork;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.sag.atwork.Register.hasPermissions;

public class book_info extends AppCompatActivity implements LocationListener{
    ImageView imageView;
    TextView name, address,rating, email, phno;
    Button book2,manage;
    ProgressDialog pd;
    String work,line,m,username,location,photo,wid,starRating;
    LocationManager locationManager;
    FrameLayout frame;
    Double longitude, latitude;
    EditText location1, date;


    @RequiresApi(api = Build.VERSION_CODES.M)
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void asyncTask(){
    @SuppressLint("StaticFieldLeak")
    AsyncTask<Void,Void,Void> asyncTask= new AsyncTask<Void, Void, Void>() {

        @Override
        protected void onPreExecute() {
            frame.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<NameValuePair> list =new ArrayList<>();
            list.add(new BasicNameValuePair("date",date.getText().toString()));
            list.add(new BasicNameValuePair("location",location1.getText().toString()));

            try {
                HttpClient client=new DefaultHttpClient();
                HttpPost post = new HttpPost(Urls.book_worker);
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
                    work=object.getString("workertype");
                    photo=object.getString("photo");
                    wid=object.getString("id");



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

                frame.setVisibility(View.INVISIBLE);
        }
    };
        asyncTask.execute();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        frame=findViewById(R.id.frame);
        name=findViewById(R.id.name_bookinfo);
        address=findViewById(R.id.address_bookinfo);
        rating=findViewById(R.id.rating_bookinfo);
        email=findViewById(R.id.email_bookinfo);
        phno=findViewById(R.id.phno_bookinfo);
        manage=findViewById(R.id.manage);
        imageView=findViewById(R.id.image_bookinfo);
        book2=findViewById(R.id.book2);
        location1=findViewById(R.id.location_bookinfo);
        date=findViewById(R.id.date_bookinfo);
        Intent in= getIntent();
        String name1= in.getStringExtra("name");
        String address1= in.getStringExtra("address");
        String rating1= in.getStringExtra("rating");
        String email1 = in.getStringExtra("email");
        String phone1= in.getStringExtra("phone");
        String image = in.getStringExtra("photo");

        name.setText(name1);
        address.setText(address1);
        rating.setText(rating1);
        email.setText(email1);
        phno.setText(phone1);
        Picasso.get().load(Urls.ip+image).into(imageView);

        String[] PERMISSIONS =new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION};
        }

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
        location1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        StringBuffer stringBuffer= new StringBuffer();
                        stringBuffer.append(dayOfMonth);
                        stringBuffer.append(" /");
                        stringBuffer.append(month);
                        stringBuffer.append(" /");
                        stringBuffer.append(year);
                        date.setText(stringBuffer.toString());
                    }
                };
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month=now.get(Calendar.MONTH);
                int day=now.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(book_info.this,R.style.datepicker,onDateSetListener,year,month,day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }

        });
        book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asyncTask();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();







        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            location1.setText(addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)/*+", "+addresses.get(0).getAddressLine(2)*/);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                location1.setBackground(getDrawable(R.drawable.edittextgreen));
            }

            else {
                location1.setBackgroundColor(Color.parseColor("#00ff00"));
            }
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(book_info.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }

}
