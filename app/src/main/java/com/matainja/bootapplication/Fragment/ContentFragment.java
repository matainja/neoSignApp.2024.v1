package com.matainja.bootapplication.Fragment;

import static com.matainja.bootapplication.session.SessionManagement.PAIRING_CODE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.matainja.bootapplication.R;
import com.matainja.bootapplication.session.SessionManagement;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ContentFragment extends Fragment {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;
    private int currentPage = 0;
    private final int NUM_PAGES = 2; // Total number of pages in the ViewPager
    private final long DELAY_MS = 0; // Delay between page changes in milliseconds
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        requireActivity().setContentView(R.layout.fragment_content);

        viewPager = (ViewPager) requireActivity().findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(getContext());
        viewPager.setAdapter(myViewPagerAdapter);


        final long PERIOD_MS = 50000; // Time between page changes
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    if (currentPage == myViewPagerAdapter.getCount() - 1) {
                        currentPage = 0;
                    } else {
                        currentPage++;
                    }
                    viewPager.setCurrentItem(currentPage, true); // Smooth scroll to the next page
                });
            }
        }, DELAY_MS, PERIOD_MS);


        return view;
    }
    public class MyViewPagerAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private VideoView myVideoView;
        private ImageView content_image;
        private ProgressDialog progressDialog;
        private MediaController mediaControls;
        private int position = 0;
        private static final String Videos_URL = "https://app.neosign.tv/storage/app/public/content/147/videos/BXS0VN61JxLgDjtWbhqMgEb9nCEobaKDRdL1J1YI.mp4";
        public MyViewPagerAdapter(Context context) {
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;

            switch (position) {
                case 0:
                    view = layoutInflater.inflate(R.layout.fragment_mp4_video, container, false);
                    myVideoView = (VideoView) view.findViewById(R.id.videoView);
                    if (mediaControls == null) {
                        mediaControls = new MediaController(requireContext());
                    }
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
                                mp.setLooping(true);
                                myVideoView.start();
                            } else {
                                myVideoView.pause();
                            }
                        }
                    });
                    break;
                case 1:
                    view = layoutInflater.inflate(R.layout.fragment_image, container, false);
                    content_image = (ImageView) view.findViewById(R.id.content_image);
                    try {
                        content_image.setImageResource(R.drawable.neo_logo);
                    } catch (Exception e) {
                        Log.e("Error>>>>>", e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                // Add more cases for additional layouts if needed
            }

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            // Return the number of pages (layouts) you want to display
            return 2; // In this example, we have two layouts
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }
}