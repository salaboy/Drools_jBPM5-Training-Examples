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

import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.plugtree.training.model.Artist;
import org.plugtree.training.model.Playlist;
import org.plugtree.training.model.Song;

public class ExistsRulesExampleTest  {

    private StatefulKnowledgeSession ksession;
    private final List<String> firedRules = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
        

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("/rules/ExistsRules.drl", getClass()), ResourceType.DRL);
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
        //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        ksession.addEventListener(new DefaultAgendaEventListener() {

            @Override
            public void afterActivationFired(AfterActivationFiredEvent event) {
                firedRules.add(event.getActivation().getRule().getName());
            }
        });
    }
    @Test
    public void testRulesActivation() {

        ksession.insert(this.createLongPlaylist());
        ksession.insert(this.createShortPlaylist());
        ksession.fireAllRules();

        Assert.assertEquals(3, firedRules.size());

        //Even when we have 2 playlists with Michael Jackson's songs this rule
        //is activated just once. 
        Assert.assertTrue(firedRules.contains("Warn when we have a Michael Jackson's song is in a playlist"));

        Assert.assertTrue(firedRules.contains("Remove playlists with more than two songs"));

        //This rule was fired after "Remove playlists with more than two songs"
        //retracted the 3 songs playlist.
        Assert.assertTrue(firedRules.contains("Warn when there are no playlists with more than two songs in my world"));
        
        ksession.dispose();
    }

    /**
     * Creates a new playlist with 2 songs.
     * @return the created playlist
     */
    private Playlist createLongPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setName("My favorite songs");


        Song song = new Song("Thriller", Song.Genre.POP, 6540, 1982);
        song.addArtist(new Artist("Michael", "Jackson"));
        playlist.addSong(song);

        song = new Song("Adagio", Song.Genre.CLASSICAL, 2561, 1708);
        song.addArtist(new Artist("Johann Sebastian", "Bach"));
        playlist.addSong(song);

        song = new Song("The final countdown", Song.Genre.ROCK, 300, 1985);
        song.addArtist(new Artist("Joey", "Tempest"));
        song.addArtist(new Artist("John", "Norum"));
        playlist.addSong(song);

        return playlist;
    }

    private Playlist createShortPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setName("Rock songs");

        Song song = new Song("The final countdown", Song.Genre.ROCK, 300, 1985);
        song.addArtist(new Artist("Joey", "Tempest"));
        song.addArtist(new Artist("John", "Norum"));
        playlist.addSong(song);

        song = new Song("Thriller", Song.Genre.POP, 6540, 1982);
        song.addArtist(new Artist("Michael", "Jackson"));
        playlist.addSong(song);

        return playlist;
    }
}
