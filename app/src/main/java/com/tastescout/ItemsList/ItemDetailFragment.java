package com.tastescout.ItemsList;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tastescout.R;
import com.tastescout.Retrofit.Result;
import com.tastescout.RoomDatabase.ItemViewModel;
import com.tastescout.RoomDatabase.ViewModelFactory;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailFragment extends Fragment {

    @BindView(R.id.item_image)
    ImageView itemImage;
    @BindView(R.id.article)
    TextView article;
    @BindView(R.id.like)
    Button likeButton;
    @BindView(R.id.share)
    Button shareButton;
    public Result mItem;
    private Application application;
    private FirebaseUser currentUser;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = Objects.requireNonNull(getActivity()).getApplication();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        ButterKnife.bind(this, rootView);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
         currentUser = mAuth.getCurrentUser();
        return rootView;
    }

    @Override
    public void onStart() {
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liked();
            }
        });
        super.onStart();
        if (getArguments() != null && getArguments().containsKey("item")) {
            if (getArguments() != null && getArguments().containsKey("item")) {
                mItem = getArguments().getParcelable("item");
                if (mItem != null) {
                    if (mItem.getYID() != null && !mItem.getYID().isEmpty()) {
                        Glide.with(itemImage).load("http://img.youtube.com/vi/" + mItem.getYID() + "/0.jpg").into(itemImage);
                    } else {
                        checkType(mItem.getType());
                    }
                    article.setText(mItem.getWTeaser());
                }
            }
        }
        getMovieItem();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_body)+" "+mItem.getName()+" "+mItem.getType()+".\n"+getString(R.string.think));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    void checkType(String type) {
        switch (type) {
            case "movie":
                itemImage.setImageResource(R.drawable.movie);
                break;
            case "show":
                itemImage.setImageResource(R.drawable.show);
                break;
            case "music":
                itemImage.setImageResource(R.drawable.music);
                break;
            case "author":
                itemImage.setImageResource(R.drawable.author);
                break;
            case "game":
                itemImage.setImageResource(R.drawable.game);
                break;
            case "book":
                itemImage.setImageResource(R.drawable.book);
                break;
        }
    }

    public void liked() {
        if (!mItem.getLiked()) {
            ItemViewModel itemViewModel = ViewModelProviders.of(this, new ViewModelFactory(application, "", "","")).get(ItemViewModel.class);
            if (currentUser != null) {
                mItem.setUser(currentUser.getUid());
            }
            itemViewModel.insert(mItem);
            mItem.setLiked(true);
            likeButton.setBackgroundResource(R.drawable.ic_like_red);
        } else {
            ItemViewModel itemViewModel = ViewModelProviders.of(this, new ViewModelFactory(application, "", "","")).get(ItemViewModel.class);
            itemViewModel.delete(mItem);
            mItem.setLiked(false);
            likeButton.setBackgroundResource(R.drawable.ic_like_white);
        }

    }

    public void getMovieItem() {

        ItemViewModel itemViewModel = ViewModelProviders.of(this, new ViewModelFactory(application, mItem.getName(), mItem.getType(),currentUser.getUid())).get(ItemViewModel.class);
        itemViewModel.getmItem().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(@Nullable Result result) {
                if (result != null) {
                    access(result);
                }

            }
        });

    }

    private void access(Result result) {
        mItem.setLiked(true);
        if (result.getLiked()) {
            likeButton.setBackgroundResource(R.drawable.ic_like_red);
        }
    }



}
