package jukebox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Jukebox {

    private HashMap<Song, Integer> songStore = new HashMap<Song, Integer>();

    private HashMap<String, HashSet<Song>> artistIndex = new HashMap<String, HashSet<Song>>();

    private List<Song> songs;

    private Random random;

    private int playCount = 0;

    private static final int PRINT_MAX = 5;

    private static final int ATTEMPTS = 50000;

    private static final String SEPARATION_STRING = "<SEP>";

    public Jukebox(String filePath, int seed) throws IOException {

        this.random = new Random(seed);
        File inputFile = new File(filePath);

        BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));

        String line;
        while ((line = buff.readLine()) != null) {

            String[] parts = line.split(SEPARATION_STRING, 4);

            String artist = parts[2];
            String song = parts[3];

            Song s = new Song(artist, song);

            this.songStore.put(s, 0);
            if (!artistIndex.containsKey(artist)) {
                HashSet<Song> songsForArtist = new HashSet<Song>();
                songsForArtist.add(s);
                this.artistIndex.put(artist, songsForArtist);
            } else {
                artistIndex.get(artist).add(s);
            }
        }

        buff.close();

        this.songs = new ArrayList<Song>(this.songStore.keySet());

    }

    private Song getRandomSong() {
        return songs.get(this.random.nextInt(songs.size()));
    }

    private int getTotalPlayedSongs() {
        return this.playCount;
    }

    private Song getMostPlayed() {
        return this.songStore.keySet().stream().max((Song o1, Song o2) -> {
            return Integer.compare(songStore.get(o1), songStore.get(o2));
        }).get();
    }

    private int getNumberOfSongs() {
        return songs.size();
    }

    private void getSongsByArtist(Song mostPlayed) {
        System.out.println(mostPlayed);
        this.artistIndex.get(mostPlayed.getArtist()).stream().sorted().forEach(x -> {
            System.out.println(x + " with " + songStore.get(x) + " plays.");
        });
    }

    public void run() {
        long start = System.currentTimeMillis();
        System.out.println("Songs in jukebox: " + this.getNumberOfSongs());
        System.out.println("Average songs played before duplicate: " + getAverageForDuplicate());
        System.out.println("Total songs played " + this.getTotalPlayedSongs());
        this.getSongsByArtist(this.getMostPlayed());
        long end = System.currentTimeMillis();

        long time = (end - start) / 1000;
        System.out.println(time + " seconds.");
    }

    private int playUntilRepeat() {
        HashSet<Song> played = new HashSet<Song>();

        for (Song s = null; played.add(s = this.getRandomSong()); this.songStore.replace(s,
                this.songStore.get(s) + 1), playCount++) {

            if (playCount < PRINT_MAX) {
                System.out.println(s);
            }

        }
        return played.size();

    }

    private int getAverageForDuplicate() {

        return (IntStream.generate(() -> this.playUntilRepeat()).limit(ATTEMPTS).sum()) / ATTEMPTS;
    }

    public static void main(String[] args) throws NumberFormatException, IOException {

        Jukebox j = (args.length == 2) ? new Jukebox(args[0], Integer.parseInt(args[1])) : null;

        if (j != null) {
            j.run();
        } else {
            System.err.println("Incorrect number of arguments.");
            System.exit(-1);
        }
    }
}
