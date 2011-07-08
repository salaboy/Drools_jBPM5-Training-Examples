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
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;
import org.plugtree.training.model.Artist;
import org.plugtree.training.model.Playlist;
import org.plugtree.training.model.Song;


public class SimpleRulesExampleTest {

    private final List<String> firedRules = new ArrayList<String>();

   
    @Test
    public void simpleRules() {
        

        StatefulKnowledgeSession ksession = this.createKSession();

        //Creates a single song.
        ksession.insert(createAdagio());
        ksession.fireAllRules();

        Assert.assertEquals(1,firedRules.size());
        //because there are no playlists, the only activated/fired rule
        //is "Songs by Johann Sebastian Bach"
        Assert.assertTrue(firedRules.contains("Warn when we have Songs by Johann Sebastian Bach"));

        firedRules.clear();

        //creates a playlist 
        ksession.insert(createPlaylist());
        ksession.fireAllRules();

        Assert.assertEquals(2,firedRules.size());
        Assert.assertTrue(firedRules.contains("Warn when we have a Playlist with more than one song"));
        Assert.assertTrue(firedRules.contains("Warn when we have a POP songs inside a playlist"));

        //"Warn when we have Songs by Johann Sebastian Bach" is not fired again because no new
        //activation occurs. (i.e. no new Bach's song was inserted/updated).

        //"Warn when we have a POP songs and Playlist" is not fired because the existing POP song
        //is inside the playlist. When you insert a "Complex" object, the
        //objects references that it contains are not inserted.

    }

    /**
     * Compiles resources and creates a new Ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSession(){

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/rules.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        
        // We can add the Runtime Logger to see what is happening inside the Engine
        // KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        //We add an AgendaEventListener to keep track of fired rules.
        ksession.addEventListener(new DefaultAgendaEventListener(){
            @Override
            public void afterActivationFired(AfterActivationFiredEvent event) {
                firedRules.add(event.getActivation().getRule().getName());
            }
        });

        return ksession;
    }

    /**
     * Creates a new playlist with 2 songs.
     * @return the created playlist
     */
    private Playlist createPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setName("My favorite songs");
        
        playlist.addSong(createThriller());
        playlist.addSong(createAdagio());

        return playlist;
    }

    /**
     * Creates a Michael Jackson song ("Thriller").
     * @return the created song.
     */
    private Song createThriller() {
        Song song = new Song("Thriller", Song.Genre.POP,6540);
        song.addArtist(new Artist("Michael", "Jackson"));
        return song;
    }

    /**
     * Creates a Bach song ("Adagio").
     * @return the created song.
     */
    private Song createAdagio() {
        Song song = new Song("Adagio", Song.Genre.CLASSICAL,2561);
        song.addArtist(new Artist("Johann Sebastian", "Bach"));
        return song;
    }
}
