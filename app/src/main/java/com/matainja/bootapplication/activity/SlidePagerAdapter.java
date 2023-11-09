package com.matainja.bootapplication.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.matainja.bootapplication.Model.ContentModel;
import com.matainja.bootapplication.R;


import java.util.List;

public class SlidePagerAdapter extends PagerAdapter {

    private List<ContentModel> slideItems;
    private LayoutInflater layoutInflater;
    private Context context;
    ImageView imageView;
    VideoView videoView;
    RelativeLayout parentVideoView,parentContentImage;
    private ProgressDialog progressDialog;
    private int position = 0;

    public SlidePagerAdapter(Context context, List<ContentModel> slideItems) {
        this.context = context;
        this.slideItems = slideItems;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return slideItems.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_pager_item, container, false);
        imageView = view.findViewById(R.id.content_image);
        videoView = view.findViewById(R.id.videoView);
        parentContentImage=view.findViewById(R.id.parentContentImage);
        parentVideoView=view.findViewById(R.id.parentVideoView);
        ContentModel item = slideItems.get(position);

        if (item.getType().equals("image")){
            parentVideoView.setVisibility(View.GONE);
            parentContentImage.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(item.getUrl())
                    .placeholder(R.drawable.neo_logo)
                    .error(R.drawable.neo_logo)
                    .into(imageView);
        }else if(item.getType().equals("video")){
            parentContentImage.setVisibility(View.GONE);
            parentVideoView.setVisibility(View.VISIBLE);
            // Create a progressbar
            progressDialog = new ProgressDialog(context);
            // Set progressbar message
            progressDialog.setMessage("Loading...");

            progressDialog.setCancelable(false);
            // Show progressbar
            progressDialog.show();

            try {
                Uri video = Uri.parse(item.getUrl());
                videoView.setVideoURI(video);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            //myVideoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoView.seekTo(position);
                    if (position == 0) {
                        mp.setLooping(true);
                        videoView.start();
                    } else {
                        videoView.pause();
                    }
                }
            });
        }



        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
