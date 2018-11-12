package com.tastescout.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tastescout.FirebaseAuthActivity;
import com.tastescout.ItemsList.ItemListActivity;
import com.tastescout.R;
import com.tastescout.Retrofit.Info;
import com.tastescout.Retrofit.Item;
import com.tastescout.Retrofit.Result;
import com.tastescout.Retrofit.RetrofitConnection;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String searchItemPreferences ="SEARCH ITEM PREFERENCES";
    private SharedPreferences pref;
    private ArrayList items;
    private ArrayList info;
    @BindView(R.id.search_et)EditText searchEditText;
    @BindView(R.id.adView)AdView adView;
    @BindView(R.id.progress)ProgressBar progressBar;
    @BindView(R.id.search_button)Button searchButton;
    TextView userName;
    TextView userMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
         pref = getApplicationContext().getSharedPreferences(searchItemPreferences, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        items=new ArrayList();
        info =new ArrayList();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userName = (TextView) header.findViewById(R.id.user_name);
        userMail = (TextView) header.findViewById(R.id.user_mail);

        userName.setText(currentUser != null ? currentUser.getDisplayName() : "");
        userMail.setText(currentUser != null ? currentUser.getEmail() : "");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent=new Intent(this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.saved) {
            Intent intent=new Intent(getApplication(),ItemListActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(getApplication(),FirebaseAuthActivity.class);
                            startActivity(intent);
                        }
                    });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onCheckboxClicked(View view) {
        SharedPreferences.Editor editor=pref.edit();
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.movies:
                if (checked)
                    editor.putString("Movies","1");
            else
                    editor.putString("Movies","0");
            case R.id.songs:
                if (checked)
                    editor.putString("Songs","1");
            else
                    editor.putString("Movies","0");
            case R.id.tv_shows:
                if(checked)
                    editor.putString("Tv Shows","1");
            else
                    editor.putString("Tv Shows","0");
            case R.id.books:
                if(checked)
                    editor.putString("Books","1");
                else
                    editor.putString("Books","0");
            case R.id.authors:
                if(checked)
                    editor.putString("Authors","1");
                else
                    editor.putString("Authors","0");
            case R.id.games:
                if(checked)
                    editor.putString("Games","1");
                else
                    editor.putString("Games","0");
        }
        editor.apply();
    }

    public void search(View view) {
        String searchWord=searchEditText.getText().toString();
        if (isOnline()){
            if(!searchWord.equals("")){
                searchButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                RetrofitConnection.getInstance().getJSONApi().getSimilar(searchWord,"221996-mustafas-74YXC3DJ","1").enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                        if (response.body() != null) {
                            searchButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            items= (ArrayList<Result>) response.body().getSimilar().getResults();
                            info = (ArrayList<Info>) response.body().getSimilar().getInfo();
                            if(items.size()>0){

                                Intent intent=new Intent(getApplication(),ItemListActivity.class);
                                intent.putParcelableArrayListExtra("resultList",items);
                                intent.putParcelableArrayListExtra("infoList", info);
                                startActivity(intent);
                            }else {
                                searchButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(SearchActivity.this);
                                builder1.setMessage(R.string.unknown);
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
                        Log.e("connectionFailed",t+"");
                        searchButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplication(),R.string.check_connection,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else {
            Toast.makeText(getApplication(),R.string.check_connection,Toast.LENGTH_LONG).show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
