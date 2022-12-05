package com.example.friendslocation.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.friendslocation.JSONParser;
import com.example.friendslocation.Models.MyLocation;
import com.example.friendslocation.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<MyLocation> data = new ArrayList<MyLocation>();
    ArrayAdapter ad;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ad= new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,data);
        binding.lvData.setAdapter(ad);
        binding.downloadBtn.setOnClickListener(view -> {

            new Telechargement(getActivity()).execute();


        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class Telechargement extends AsyncTask {

        Context cont;
        AlertDialog alert;

        public Telechargement(Context cont) {
            this.cont = cont;
        }

        @Override
        protected void onPreExecute() {
            // UI thread
            data.clear();
            AlertDialog.Builder builder=new AlertDialog.Builder(cont);
            builder.setTitle("Telechargement");
            builder.setMessage("Veuillez patientez......");
            alert=builder.create();
            alert.show();
        }

        @Override
        protected void onPostExecute(Object o) {
//            UI thread
            ad.notifyDataSetChanged();
            alert.dismiss();



        }

        @Override
        protected Object doInBackground(Object[] objects) {

            JSONParser parser=new JSONParser();
            String ip="10.0.2.2";
            //benessba le emulateur tnajem te5dem 3al avd:10.0.2.2 //LAN: ip:192.... //internet: site
            String url="http://"+ip+"/tp3Mobile/servicephp/getAll.php";
            JSONObject response=parser.makeHttpRequest(url,"GET",null);

//            recupiration des result
            try {
                int success=response.getInt("success");
                if(success==0){
                    String msg=response.getString("message");
                }else {

                    JSONArray tab=response.getJSONArray("Ami");
                    for(int i=0;i<tab.length();i++){
                        JSONObject ligne=tab.getJSONObject(i);
                        String nom=ligne.getString("nom");
                        String numero=ligne.getString("numero");
                        String longitude=ligne.getString("longitude");
                        String latitude=ligne.getString("latitude");
                        data.add(new MyLocation(nom,numero,longitude,latitude));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
