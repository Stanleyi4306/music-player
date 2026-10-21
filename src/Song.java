/**
 * Song = What the track is (title, artist).
 */
public class Song {

    public String id;
    public String title;
    public String artist;

    public Song(String id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }
    public String getId() {return id;}
    public String getTitle() {return title;}
    public String getArtist() {return artist;}
    
}


