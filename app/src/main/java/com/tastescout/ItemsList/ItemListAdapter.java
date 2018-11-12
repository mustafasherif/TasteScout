package com.tastescout.ItemsList;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tastescout.R;
import com.tastescout.Retrofit.Result;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private final ItemListActivity mParentActivity;
    private ArrayList<Result> mItems;
    private final boolean mTwoPane;

    public ItemListAdapter(ItemListActivity ParentActivity, ArrayList<Result> Items, boolean TwoPane) {
        this.mParentActivity = ParentActivity;
        this.mItems = Items;
        this.mTwoPane = TwoPane;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_content, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(mItems.get(i).getName());
        if (mItems.get(i).getYID()!=null&&!mItems.get(i).getYID().isEmpty()){
            Glide.with(mParentActivity).load("http://img.youtube.com/vi/"+mItems.get(i).getYID()+"/0.jpg").into(viewHolder.itemImage);
        }else {
            checkType(mItems.get(i).getType(),viewHolder.itemImage);
        }
        viewHolder.type.setText(mItems.get(i).getType());

    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_name)TextView itemName;
        @BindView(R.id.item_image)ImageView itemImage;
        @BindView(R.id.type)TextView type;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable("item", mItems.get(getAdapterPosition()));
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(mParentActivity, ItemDetailActivity.class);
                intent.putExtra("item", mItems.get(getAdapterPosition()));
                mParentActivity.startActivity(intent);
            }
        }
    }
    private void checkType(String type, ImageView itemImage){
        ImageLoadingAsyncTask imageLoadingAsyncTask=new ImageLoadingAsyncTask(itemImage);
        imageLoadingAsyncTask.execute(type);
    }

    void changeSorting(ArrayList<Result> items){
        this.mItems=items;
        notifyDataSetChanged();
    }

}
