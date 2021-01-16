package com.jsm.roomdatabaseretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jsm.roomdatabaseretrofit.Adapter.ActorAdapter;
import com.jsm.roomdatabaseretrofit.Modal.Actor;
import com.jsm.roomdatabaseretrofit.Network.Api;
import com.jsm.roomdatabaseretrofit.Repository.ActorRepository;
import com.jsm.roomdatabaseretrofit.ViewModal.ActorViewModal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActorViewModal actorViewModal;
    private ActorAdapter actorAdapter;
    private List<Actor> actorList;
    private static final String URL_DATA = "http://www.codingwithjks.tech/data.php/";
    private RecyclerView recyclerView;
    private ActorRepository actorRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        actorRepository = new ActorRepository(getApplication());
        actorList = new ArrayList<>();
        actorAdapter = new ActorAdapter(this, actorList);


        actorViewModal = new ViewModelProvider(this).get(ActorViewModal.class);
        networkRequest();
        actorViewModal.getAllActor().observe(this, actorList -> {
            recyclerView.setAdapter(actorAdapter);
            actorAdapter.getAllActors(actorList);
            Log.d("main", "onResponse: " +actorList);
        });
    }

    private void networkRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Actor>> call = api.getAllActors();
        call.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.isSuccessful()) {
                    actorRepository.insert(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}