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
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;


import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.plugtree.training.model.Artist;
import org.plugtree.training.model.Playlist;
import org.plugtree.training.model.Song;

public class SimpleRulesExampleTest  {

    private StatefulKnowledgeSession ksession;
    private final List<String> firedRules = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
       

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("/rules/SimpleRules.drl", getClass()), ResourceType.DRL);
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
        
        // We can Activate the Runtime Logger to see what is happening inside the Engine
        //KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        ksession.addEventListener(new DefaultAgendaEventListener() {

            @Override
            public void afterActivationFired(AfterActivationFiredEvent event) {
                firedRules.add(event.getActivation().getRule().getName());
            }
        });
    }
    @Test
    public void rulesActivation() {

        Playlist playlist = this.createFullPlaylist();

        ksession.insert(playlist);
        ksession.fireAllRules();

        Assert.assertEquals(2,firedRules.size());
        Assert.assertTrue(firedRules.contains("Warn when we have a Playlist with more than two songs AND containing 'Thriller'"));
        Assert.assertTrue(firedRules.contains("Warn when we have a Playlist with one song OR containing 'Thriller'"));
        firedRules.clear();

        ksession.insert(createPlaylistWithOneSong());
        ksession.fireAllRules();
        Assert.assertEquals(1,firedRules.size());
        Assert.assertTrue(firedRules.contains("Warn when we have a Playlist with one song OR containing 'Thriller'"));

        ksession.dispose();
    }

    /**
     * Creates a new playlist with 3 songs.
     * @return the created playlist
     */
    private Playlist createFullPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setName("My favorite songs");
        
        playlist.addSong(createThrillerSong());
        playlist.addSong(createAdagioSong());
        playlist.addSong(createTheFinalCountdownSong());

        return playlist;
    }

    /**
     * Creates a new playlist containing just one song: "The final countdown"
     * @return a new playlist containing just one song: "The final countdown"
     */
    private Playlist createPlaylistWithOneSong() {
        Playlist playlist = new Playlist();
        playlist.setName("Single song playlist");
        
        playlist.addSong(createTheFinalCountdownSong());

        return playlist;
    }

    /**
     * Creates a new playlist containing just one song: "Thriller"
     * @return a new playlist containing just one song: "Thriller"
     */
    private Song createThrillerSong(){
        Song song = new Song("Thriller", Song.Genre.POP, 6540, 1982);
        song.addArtist(new Artist("Michael", "Jackson"));
        return song;
    }

    /**
     * Creates a new playlist containing just one song: "Adagio"
     * @return a new playlist containing just one song: "Adagio"
     */
    private Song createAdagioSong(){
        Song song = new Song("Adagio", Song.Genre.CLASSICAL, 2561, 1708);
        song.addArtist(new Artist("Johann Sebastian", "Bach"));
        return song;
    }

    /**
     * Creates a new playlist containing just one song: "The final countdown"
     * @return a new playlist containing just one song: "The final countdown"
     */
    private Song createTheFinalCountdownSong(){
        Song song = new Song("The final countdown", Song.Genre.ROCK, 300, 1985);
        song.addArtist(new Artist("Joey", "Tempest"));
        song.addArtist(new Artist("John", "Norum"));
        return song;
    }
}
