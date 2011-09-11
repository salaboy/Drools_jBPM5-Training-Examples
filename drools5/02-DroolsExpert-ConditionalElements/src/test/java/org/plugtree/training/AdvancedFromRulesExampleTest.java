package org.plugtree.training;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;


import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.plugtree.training.model.Artist;
import org.plugtree.training.model.Playlist;
import org.plugtree.training.model.Song;
import org.plugtree.training.model.util.HibernateUtil;

public class AdvancedFromRulesExampleTest {

    private StatefulKnowledgeSession ksession;

    @Before
    public void setUp() throws Exception {
       

        PackageBuilderConfiguration pkgConf = new PackageBuilderConfiguration();
        pkgConf.addAccumulateFunction("songsWithALetterOnTheirTitlesFunction", SongsWithALetterOnTheirTitlesFunction.class);

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(pkgConf);
        kbuilder.add(new ClassPathResource("/rules/AdvancedFromRules.drl", getClass()), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
    }
    @Test
    public void rulesActivation() {
        List<Playlist> playlists = this.createPlaylists();
        
        Session session = HibernateUtil.getSession();
        session.beginTransaction().begin();

        //Persists the playlists and songs
        for (Playlist playlist : playlists) {
            session.save(playlist);
        }

        //adds Hibernate Session as a global
        ksession.setGlobal("session", session);
        
        //inserts only the playlists.
        for (Playlist playlist : playlists) {
            ksession.insert(playlist);
        }
        
        //adds the two lists needed
        List<String> playListsContainingSongsWithLetterA = new ArrayList<String>();
        ksession.setGlobal("list", playListsContainingSongsWithLetterA);
        List<String> songsFromThe80s = new ArrayList<String>();
        ksession.setGlobal("songsFromThe80s", songsFromThe80s);

        ksession.fireAllRules();

        Assert.assertEquals(1, playListsContainingSongsWithLetterA.size());
        Assert.assertTrue(playListsContainingSongsWithLetterA.contains("A playlist"));

        Assert.assertEquals(3, songsFromThe80s.size());
        Assert.assertTrue(songsFromThe80s.contains("Thriller"));
        Assert.assertTrue(songsFromThe80s.contains("The final countdown"));
        Assert.assertTrue(songsFromThe80s.contains("No For The Inocent"));

        ksession.dispose();
    }

    /**
     * @return the created playlists
     */
    private List<Playlist> createPlaylists() {

        List<Playlist> playlists = new ArrayList<Playlist>();

        //playlist A
        Playlist playlistA = new Playlist();
        playlistA.setName("A playlist");
        playlistA.addSong(this.createTheFinalCountdownSong());
        playlistA.addSong(this.createAdagioSong());
        playlists.add(playlistA);

        //playlist B (the titles of its songs don't contain the 'a' letter
        Playlist playlistB = new Playlist();
        playlistB.setName("B playlist");
        playlistB.addSong(this.createThrillerSong());
        playlistB.addSong(this.createNoForTheInocentSong());
        playlists.add(playlistB);

        return playlists;
    }

    private Song createThrillerSong(){
        Song song = new Song("Thriller", Song.Genre.POP, 6540, 1982);
        song.addArtist(new Artist("Michael", "Jackson"));
        return song;
    }

    private Song createAdagioSong(){
        Song song = new Song("Adagio", Song.Genre.CLASSICAL, 2561, 1708);
        song.addArtist(new Artist("Johann Sebastian", "Bach"));
        return song;
    }

    private Song createTheFinalCountdownSong(){
        Song song = new Song("The final countdown", Song.Genre.ROCK, 300, 1985);
        song.addArtist(new Artist("Joey", "Tempest"));
        song.addArtist(new Artist("John", "Norum"));
        return song;
    }
    
    private Song createNoForTheInocentSong(){
        Song song = new Song("No For The Inocent", Song.Genre.ROCK, 263,1983);
        song.addArtist(new Artist("Gene ", "Simmons"));
        song.addArtist(new Artist("Vinnie", "Vincent"));
        return song;
    }
}
