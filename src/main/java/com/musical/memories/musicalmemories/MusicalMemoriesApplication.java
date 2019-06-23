package com.musical.memories.musicalmemories;

import com.musical.memories.musicalmemories.api.Genius;
import com.musical.memories.musicalmemories.api.Spotify;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicalMemoriesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MusicalMemoriesApplication.class, args);
       // Spotify spotify = new Spotify();
        //String val = spotify.authorize();
    }
    @Override
    public void run(String... args) {
        //Spotify spotify = new Spotify();
        //String val = spotify.authorize();
        //Genius genius = new Genius();
        //System.out.println(genius.findTopHitTitle("Kendrick"));
       // spotify.requestToken();
    }

}
