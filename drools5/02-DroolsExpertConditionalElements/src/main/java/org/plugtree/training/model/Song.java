/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

/**
 *
 * @author salaboy
 */

@Entity
@NamedQuery(name="Song.getAll", query="SELECT s FROM Song s")
public class Song {


    public enum Genre { JAZZ, ROCK, POP, CLASSICAL};

    private Integer id;
    private String title;
    private Genre type;
    private List<Artist> artists;
    private int duration;
    private int year;

    public Song(){
        this.artists = new ArrayList<Artist>();
    }

    public Song(String name, Genre type, int duration,int year) {
        this();
        this.title = name;
        this.type = type;
        this.duration = duration;
        this.year = year;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @ManyToMany(cascade=CascadeType.ALL)
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    

    @Override
    public String toString() {
        
        String result =  "Song {id= "+this.id+", name= '"+this.getTitle()+"', year= "+this.year+", genre= '"+this.getGenre()+"', duration= '"+this.getDuration()+"', artists= [";
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
