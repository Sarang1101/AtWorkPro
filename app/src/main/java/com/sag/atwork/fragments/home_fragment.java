package com.sag.atwork.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sag.atwork.Profile;
import com.sag.atwork.R;

import java.util.Objects;

public class home_fragment extends Fragment implements View.OnClickListener {
    View view;
    CardView profilecard,workercard,notifycard,historycard,settingscard;
    SharedPreferences preferences;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_home,container,false);
         preferences= Objects.requireNonNull(getActivity()).getSharedPreferences("login", Context.MODE_PRIVATE);

         profilecard=view.findViewById(R.id.profilecard);
        workercard=view.findViewById(R.id.workercard);
        notifycard=view.findViewById(R.id.notifycard);
        historycard=view.findViewById(R.id.historycard);
        settingscard=view.findViewById(R.id.settingscard);

        profilecard.setOnClickListener(this);
        workercard.setOnClickListener(this);
        notifycard.setOnClickListener(this);
        historycard.setOnClickListener(this);
        settingscard.setOnClickListener(this);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        Fragment fragment=null;
        int id = v.getId();

        if (id == R.id.profilecard) {
            startActivity(new Intent(getActivity(), Profile.class));
        } else if (id == R.id.workercard) {
            fragment= new com.sag.atwork.fragments.worker_fragment();
        } else if (id == R.id.notifycard) {
            fragment= new com.sag.atwork.fragments.notify_fragment();
        } else if (id == R.id.settingscard) {
            fragment= new com.sag.atwork.fragments.settings_fragment();
        }else if (id == R.id.historycard){
            fragment= new com.sag.atwork.fragments.history_fragment();
        }
        if(fragment!=null){
            FragmentTransaction ft= Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_fragment,fragment);
            ft.commit();
        }
    }

}
