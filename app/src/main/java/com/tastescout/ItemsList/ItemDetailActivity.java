package com.tastescout.ItemsList;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import com.tastescout.R;
import com.tastescout.Retrofit.Result;
import butterknife.ButterKnife;


public class ItemDetailActivity extends AppCompatActivity {


    public Result mItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        mItem = getIntent().getParcelableExtra("item");
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mItem.getName());
        }


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("item", mItem);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
           getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }




    }


}
