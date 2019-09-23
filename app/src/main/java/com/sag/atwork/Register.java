package com.sag.atwork;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    EditText dob,username,email,address,phone,password;
    Bitmap bitmap;
    TextView login;
    FrameLayout frame;
    CircleImageView image;
    Button register;
    public static boolean hasPermissions(Context context,String...permissions) {
    if (context != null && permissions != null) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
    }
    return true;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        frame=findViewById(R.id.frame);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        dob=findViewById(R.id.dob);
        image=findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,100);
            }
        });
        String[] PERMISSIONS = new String[0];
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            PERMISSIONS=new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE};
            }
        if (!hasPermissions(this,PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
        dob.setOnClickListener(new View.OnClickListener() {
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
                        dob.setText(stringBuffer.toString());
                    }
                };
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month=now.get(Calendar.MONTH);
                int day=now.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(Register.this,R.style.datepicker,onDateSetListener,year,month,day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().equals("")){
                    username.setError("You need a username to register");
                }
                else if (email.getText().toString().equals("")){
                    email.setError("Enter an email");
                }
                else if (address.getText().toString().equals("")){
                    address.setError("Enter your residential address");
                }
                else if (dob.getText().toString().equals("")){
                    dob.setError("Enter your DATE OF BIRTH");
                }
                else if (phone.getText().toString().equals("")){
                    phone.setError("Enter your phone number here");
                }
                else if (password.getText().toString().equals("")){
                    password.setError("Enter a password with atleast 8 characters");
                }
                else {
                    uploadBitmap(bitmap);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    frame.setVisibility(View.VISIBLE);
                }


                    }
        });
                login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Register.this,Login.class));
    }
});

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {


            Uri imageUri = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);


                image.setImageBitmap(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {


        final String tag = username.getText().toString().trim();
        final String tag1 = email.getText().toString().trim().toLowerCase();
        final String tag2 = address.getText().toString().trim();
        final String tag3 = dob.getText().toString().trim();
        final String tag4 = phone.getText().toString().trim().toLowerCase();
        final String tag5 = password.getText().toString().trim();
        final String tag6 = "user";


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,Urls.register,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            JSONObject obj = new JSONObject(new String( response.data));
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            frame.setVisibility(View.INVISIBLE);
                            if (obj.getString("message").equalsIgnoreCase("Successful")){
                                Toast.makeText(Register.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this,Login.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Register.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Username", tag);
                params.put("Email", tag1);
                params.put("Address", tag2);
                params.put("DOB", tag3);
                params.put("Phone", tag4);
                params.put("Password", tag5);
                params.put("user_type", tag6);
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

}



