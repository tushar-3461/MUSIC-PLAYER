package com.example.musicplayer.recycleviewapplier;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.player;

import java.io.File;
import java.util.ArrayList;

public class recycleviewadapter extends RecyclerView.Adapter<recycleviewadapter.ViewHolder> {

    Context context;
    ArrayList<String> arrayList;
    Intent i;
    ArrayList<File> file;
    public recycleviewadapter(Context context, ArrayList<String> arrayList, ArrayList<File> file) {
        this.context = context;
        this.arrayList = arrayList;
        this.file=file;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycleformat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.musicname.setText(arrayList.get(position));
        setanimation(holder.musicname);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(context, player.class);
                i.putExtra("songname",holder.musicname.getText().toString());
                i.putExtra("songs",file);
                i.putExtra("pos",position);
                i.putExtra("arr",arrayList);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView musicname;
        ImageView image;
        LinearLayout linearLayout;
        public ViewHolder(View v){
            super(v);
            musicname=v.findViewById(R.id.musicname);
            image=v.findViewById(R.id.image);
            linearLayout=v.findViewById(R.id.linearlayout);

        }
    }
    public void setanimation(View v)
    {
        Animation animation= AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
        v.startAnimation(animation);
    }

}
