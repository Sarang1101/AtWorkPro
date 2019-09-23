package com.sag.atwork.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.sag.atwork.R;

public class worker_fragment extends Fragment implements View.OnClickListener{
    CardView carpenter,plumber,painter,mechanic,electrician,builder,farmer,gardener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_worker,container,false);
       carpenter=view.findViewById(R.id.carpenter);
       plumber=view.findViewById(R.id.plumber);
       painter=view.findViewById(R.id.painter);
       mechanic=view.findViewById(R.id.mechanic);
       electrician=view.findViewById(R.id.electrician);
       builder=view.findViewById(R.id.builder);
       farmer=view.findViewById(R.id.farmer);
       gardener=view.findViewById(R.id.gardener);

       carpenter.setOnClickListener(this);
       plumber.setOnClickListener(this);
       painter.setOnClickListener(this);
       mechanic.setOnClickListener(this);
       gardener.setOnClickListener(this);
       electrician.setOnClickListener(this);
       builder.setOnClickListener(this);
       farmer.setOnClickListener(this);
       return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(),com.sag.atwork.worker_select.class);
        int id = v.getId();

        if (id == R.id.carpenter) {
            i.putExtra("search","carpenter");
        }
        else if (id == R.id.plumber) {
            i.putExtra("search","plumber");
        }else if (id == R.id.painter) {

            i.putExtra("search","painter");
        }else if (id == R.id.mechanic) {
            i.putExtra("search","mechanic");
        }else if (id == R.id.gardener) {
            i.putExtra("search","gardener");
        }else if (id == R.id.electrician) {
            i.putExtra("search","electrician");
        }else if (id == R.id.builder) {
            i.putExtra("search","builder");
        }else if (id == R.id.farmer) {
            i.putExtra("search","farmer");
        }
        startActivity(i);

    }
}
