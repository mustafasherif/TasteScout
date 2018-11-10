package com.tastescout.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tastescout.R;
import com.tastescout.Retrofit.Result;
import com.tastescout.RoomDatabase.ItemViewModel;
import com.tastescout.RoomDatabase.ViewModelFactory;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_name)TextView userName;
    @BindView(R.id.email)TextView userMail;
    @BindView(R.id.saved_items)TextView savedItems;
    private ArrayList<Result> mItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peofile);
        ButterKnife.bind(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userName.setText(currentUser.getDisplayName());
        }
        if (currentUser != null) {
            userMail.setText(currentUser.getEmail());
        }
        ItemViewModel itemViewModel= ViewModelProviders.of(this, new ViewModelFactory(this.getApplication(),"","", currentUser.getUid())).get(ItemViewModel.class);
        itemViewModel.getmAllItems().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> mMovieItems) {
                mItems= (ArrayList<Result>) mMovieItems;
                if (mItems != null) {
                    savedItems.setText(mItems.size());
                }
            }
        });
    }
}
