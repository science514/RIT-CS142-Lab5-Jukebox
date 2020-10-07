package jukebox;

public class Song implements Comparable<Song> {

    private String artist;
    private String song;

    public Song(String artist, String song) {
        this.artist = artist;
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Song))
            return false;
        else {
            Song tmp = (Song) obj;

            return (this.artist.equals(tmp.artist) && this.song.equals(tmp.song));

        }

    }

    @Override
    public int hashCode() {
        return this.artist.hashCode() + this.song.hashCode();

    }

    @Override
    public String toString() {
        return "ARTIST: " + this.artist + " SONG: " + this.song;
    }

    @Override
    public int compareTo(Song o) {
        // TODO Auto-generated method stub
        int artistCompare = this.artist.compareTo(o.artist);
        if (artistCompare != 0) {
            return artistCompare;
        } else {
            return this.song.compareTo(o.song);
        }
    }

}
