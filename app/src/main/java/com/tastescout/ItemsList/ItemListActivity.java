package com.tastescout.ItemsList;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tastescout.R;
import com.tastescout.Retrofit.Info;
import com.tastescout.Retrofit.Result;
import com.tastescout.RoomDatabase.ItemViewModel;
import com.tastescout.RoomDatabase.ViewModelFactory;
import com.tastescout.Widget.ItemWidgetProvider;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.item_list)RecyclerView recyclerView;
    @BindView(R.id.searched_item)ImageView searchedItem;
    @BindView(R.id.searched_item_title)TextView searchedItemTitle;
    @BindView(R.id.searched_item_type)TextView searchedItemType;
    private ArrayList<Result> mItems;
    private ArrayList<Info> mInfos;
    private ItemListAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        mItems=new ArrayList();
        setupRecyclerView();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(getIntent().getParcelableArrayListExtra("resultList")!=null){
            mItems=getIntent().getParcelableArrayListExtra("resultList");
            mInfos=getIntent().getParcelableArrayListExtra("infoList");
            searchedItemTitle.setText(mInfos.get(0).getName());
            searchedItemType.setText(mInfos.get(0).getType());
            itemAdapter.changeSorting(mItems);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ItemWidgetProvider.class));
            ItemWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds,mItems);
        }else {
            ItemViewModel itemViewModel= ViewModelProviders.of(this, new ViewModelFactory(this.getApplication(),"","", currentUser.getUid())).get(ItemViewModel.class);
            itemViewModel.getmAllItems().observe(this, new Observer<List<Result>>() {
                @Override
                public void onChanged(@Nullable List<Result> mMovieItems) {
                    mItems= (ArrayList<Result>) mMovieItems;
                   itemAdapter.changeSorting(mItems);
                    searchedItemTitle.setText(R.string.saved);
                    searchedItemType.setText(String.valueOf(mItems.size()));
                }
            });
        }

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if(mInfos!=null){
                        collapsingToolbar.setTitle(mInfos.get(0).getName());
                    }else {
                        collapsingToolbar.setTitle(getString(R.string.saved));
                    }
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new ItemListAdapter(this, mItems, mTwoPane);
        recyclerView.setAdapter(itemAdapter);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

}
