package com.example.friendslocation.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.friendslocation.JSONParser;
import com.example.friendslocation.Models.MyLocation;
import com.example.friendslocation.databinding.FragmentGalleryBinding;
import com.example.friendslocation.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    String nom;
    String num;
    String longitude;
    String latitude;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.button.setOnClickListener(view -> {

            new GalleryFragment.Upload(getActivity()).execute();


        });


        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


class Upload extends AsyncTask {
    Context cont;
    AlertDialog alert;

    public Upload(Context cont) {
        this.cont = cont;
    }

    @Override
    protected void onPreExecute() {
        // UI thread

        AlertDialog.Builder builder=new AlertDialog.Builder(cont);
        builder.setTitle("uploading");
        builder.setMessage("uploading data......");
        alert=builder.create();
        alert.show();
    }

    @Override
    protected void onPostExecute(Object o) {
//            UI thread

        alert.dismiss();



    }


    @Override
    protected Object doInBackground(Object[] objects) {
        nom=binding.editName.getText().toString();
        num=binding.editTextPhone.getText().toString();
        longitude=binding.editlong.getText().toString();
        latitude=binding.editlat.getText().toString();
        JSONParser parser=new JSONParser();
        String ip="10.0.2.2";
        //benessba le emulateur tnajem te5dem 3al avd:10.0.2.2 //LAN: ip:192.... //internet: site
        String url="http://"+ip+"/tp3Mobile/servicephp/add.php";
        HashMap<String,String> params= new HashMap<String,String>();
        params.put("nom",nom);
        params.put("numero",num);
        params.put("longitude",longitude);
        params.put("latitude",latitude);

        JSONObject response=parser.makeHttpRequest(url,"POST",params);

//            recupiration des result
        try {
            int success=response.getInt("success");
            if(success==0){
                String msg=response.getString("message");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
}
