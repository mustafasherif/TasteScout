package com.tastescout.ItemsList;

import android.os.AsyncTask;
import android.widget.ImageView;

import com.tastescout.R;

import java.lang.ref.WeakReference;

public class ImageLoadingAsyncTask extends AsyncTask<String,Void,Void> {
    private WeakReference<ImageView> itemImageRefrence;

    ImageLoadingAsyncTask(ImageView itemImage) {
        this.itemImageRefrence = new WeakReference<ImageView>(itemImage);
    }

    @Override
    protected Void doInBackground(String... strings) {
        if (itemImageRefrence != null) {
            ImageView itemImage = itemImageRefrence.get();
            if (itemImage != null) {
                switch (strings[0]) {
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
        }
        return null;
    }

}