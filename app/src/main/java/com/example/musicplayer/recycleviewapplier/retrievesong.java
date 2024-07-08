package com.example.musicplayer.recycleviewapplier;

import android.os.Environment;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

public class retrievesong {


    ArrayList<String> arrayList;

    public retrievesong(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<File> addsong(File filess)
    {
        ArrayList<File> songs = new ArrayList<>();
        File[] files = filess.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && !file.isHidden()) {
                    songs.addAll(addsong(file)); // Recursively add songs from subdirectories
                } else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {
                    songs.add(file);
                }
            }
        }
        return songs;
    }
    public ArrayList<File> displaysong() {
        ArrayList<File> songs = addsong(Environment.getExternalStorageDirectory());

        for (int i = 0; i < songs.size(); i++) {
            arrayList.add(songs.get(i).getName().replace(".mp3", "").replace(".wav", "")) ;
        }
        return songs;
    }


}
