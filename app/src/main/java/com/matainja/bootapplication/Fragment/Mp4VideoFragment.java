package com.matainja.bootapplication.Fragment;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.matainja.bootapplication.R;

public class Mp4VideoFragment extends Fragment {
    private static final String Videos_URL = "https://app.neosign.tv/storage/app/public/content/147/videos/BXS0VN61JxLgDjtWbhqMgEb9nCEobaKDRdL1J1YI.mp4";
    private VideoView myVideoView;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private int position = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mp4_video, container, false);

        // Find your VideoView in your video_main.xml layout
        myVideoView = (VideoView) view.findViewById(R.id.videoView);
        if (mediaControls == null) {
            mediaControls = new MediaController(requireContext());
        }
        videoView();

        return view;
    }

    private void videoView(){
        // Create a progressbar
        progressDialog = new ProgressDialog(requireContext());
        // Set progressbar message
        progressDialog.setMessage("Loading...");

        progressDialog.setCancelable(false);
        // Show progressbar
        progressDialog.show();

        try {
            Uri video = Uri.parse(Videos_URL);
            myVideoView.setVideoURI(video);
            myVideoView.setMediaController(mediaControls);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });
    }
}