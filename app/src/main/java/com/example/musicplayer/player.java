package com.example.musicplayer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chibde.visualizer.LineBarVisualizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class player extends AppCompatActivity {

    Button previoussong,playpausesong,nextsong;
    TextView durationend,durationstart,songname;
    ImageView icon;
    SeekBar seekbar;
    MediaPlayer mediaPlayer;
    ArrayList<File> mysongs;
    int position;
    String sname;
    ArrayList<String>arr;
    Thread seek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        previoussong=findViewById(R.id.previoussong);
        playpausesong=findViewById(R.id.playpausesong);
        nextsong=findViewById(R.id.nextsong);
        durationend=findViewById(R.id.durationend);
        durationstart=findViewById(R.id.durationstart);
        songname=findViewById(R.id.songname);
        icon=findViewById(R.id.icon);
        seekbar=findViewById(R.id.seekbar);

        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        animate();
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate();
            }
        });
        Intent i=getIntent();
        Bundle b=i.getExtras();
        mysongs=(ArrayList)b.getParcelableArrayList("songs");
        position=b.getInt("pos",0);
        sname=b.getString("songname");
        arr=b.getStringArrayList("arr");
        songname.setText(sname);
        Uri uri=Uri.parse(mysongs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this, uri);
        mediaPlayer.start();



        playpausesong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        nextsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p=(position+1)%mysongs.size();
                changetrack(p);

            }
        });
        previoussong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p=((position-1)<0)?(mysongs.size()-1):(position-1);
                changetrack(p);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextsong.performClick();
            }
        });

        seek();

        durationend.setText(time(mediaPlayer.getDuration()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                durationstart.setText(time(mediaPlayer.getCurrentPosition()));
                new Handler().postDelayed(this,1000);
            }
        },1000);

    }
    public void animate()
    {
        ObjectAnimator ob=ObjectAnimator.ofFloat(icon,"rotation",0f,360f);
        ob.setDuration(1000);
        AnimatorSet animationSet=new AnimatorSet();
        animationSet.playTogether(ob);
        animationSet.start();
    }
    public void seek(){
           seek=new Thread(){
               @Override
               public void run() {
                   int getd= mediaPlayer.getDuration();
                   int currd=0;
                   while(currd<getd)
                   {
                       try{
                           sleep(50);
                           currd= mediaPlayer.getCurrentPosition();
                           seekbar.setProgress(currd);
                       }catch (IllegalStateException e){
                           e.printStackTrace();
                       } catch (InterruptedException e) {
                           throw new RuntimeException(e);
                       }
                   }
               }
           };
           seekbar.setMax(mediaPlayer.getDuration());
           seek.start();
           seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
           seekbar.getThumb().setColorFilter(getResources().getColor(R.color.red),PorterDuff.Mode.SRC_IN);
           seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                   if (b) {
                       mediaPlayer.seekTo(i);
                   }
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
               }
           });
    }
    public void play(){
        try {
            if (mediaPlayer.isPlaying()) {
                playpausesong.setBackgroundResource(R.drawable.playbutton);
                mediaPlayer.pause();
            } else {
                playpausesong.setBackgroundResource(R.drawable.pausebutton);
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public void changetrack(int p){
        try{
            mediaPlayer.stop();
            mediaPlayer.release();
            String s = arr.get(p).toString();
            songname.setText(s);
            Uri uri = Uri.parse(mysongs.get(p).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            animate();
            mediaPlayer.start();
            playpausesong.setBackgroundResource(R.drawable.pausebutton);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public String time(int d)
    {
        String time="";
        int m=d/1000/60;
        int s=d/1000%60;
        time+=m+":";
        if(s<10)
            time+="0";
        time+=s;
        return time;
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        mediaPlayer.release();
        startActivity(new Intent(this,MainActivity.class));
    }
}