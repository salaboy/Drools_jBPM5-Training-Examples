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
public class Song {


    public enum Genre { JAZZ, ROCK, POP, CLASSICAL};
    private String title;
    private Genre type;
    private List<Artist> artists;
    private int duration;

    public Song(){
        this.artists = new ArrayList<Artist>();
    }

    public Song(String name, Genre type, int duration) {
        this();
        this.title = name;
        this.type = type;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public Genre getGenre() {
        return type;
    }

    public void setGenre(Genre type) {
        this.type = type;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public boolean addArtist(Artist artist) {
        return artists.add(artist);
    }



    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        
        String result =  "Song {title= '"+this.getTitle()+"', genre= '"+this.getGenre()+"', duration= '"+this.getDuration()+"', artists= [";
        String separator = "";

        for(int i = 0; i < this.artists.size(); i++){
            result += separator+this.artists.get(i);

            if (i==0){
                separator = ", ";
            }

        }

        return result+"]}";

    }

    
}
