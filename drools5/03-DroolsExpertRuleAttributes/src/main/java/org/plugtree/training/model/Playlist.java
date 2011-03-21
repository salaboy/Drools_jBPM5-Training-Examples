/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author salaboy
 */

public class Playlist {

    private Integer id;
    private String name;
    private List<Song> songs;
    private boolean playing;
    

    public Playlist() {
        this.songs = new ArrayList<Song>();
    }

    public Playlist(String name) {
        this();
        this.name = name;
    }

    public boolean isEmpty() {
        return songs.isEmpty();
    }

    public int getSize(){
        return this.songs.size();
    }
    

    public boolean containsSong(String songName){
        System.out.println("Searching for a song named '"+songName+"'");
        for (Song song : songs) {
            if (song.getName().equalsIgnoreCase(songName)){
                return true;
            }
        }

        return false;
    }

    public boolean isLongerThan(int seconds){
        System.out.println("Calculating  playlist lenght");

        int calculatedTime = 0;
        for (Song song : songs) {
            calculatedTime += song.getDuration();
            if (calculatedTime > seconds){
                return true;
            }
        }

        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public boolean addSong(Song song) {
        return songs.add(song);
    }

    public void play(){
        this.playing = true;
    }

    public boolean isPlaying(){
        return this.playing;
    }

    @Override
    public String toString() {

        String result =  "Playlist {id= "+this.id+", name= '"+this.getName()+"', songs= [";
        String separator = "";

        for(int i = 0; i < this.songs.size(); i++){
            result += separator+this.songs.get(i);

            if (i==0){
                separator = ", ";
            }

        }

        return result+"]}";

    }

   

}
