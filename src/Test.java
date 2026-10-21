
public class Test {
    public static void main(String[] args) {
        MusicPlayer player = new MusicPlayer();

        System.out.println("--- Build the playlist ---");
        System.out.println(player.addSong("s1", "Night Drive", "Artist A"));
        System.out.println(player.addSong("s2", "Slow Tide", "Artist B"));
        System.out.println(player.addSong("s3", "Copper Sky", "Artist C"));
        System.out.println(player.addSong("s4", "Static Bloom", "Artist D"));
        System.out.println(player.addSong("s5", "Low Orbit", "Artist E"));
        System.out.println(player.addSong("s6", "Glass Rivers", "Artist F"));
        System.out.println("duplicate s3: " + player.addSong("s3", "Copper Sky", "Artist C"));
        
        System.out.println("--- 2. Look up songs --- ");
        System.out.println("find s4: " + (player.findSong("s4") != null ? player.findSong("s4").title  : "null"));
        System.out.println("find s9 " + (player.findSong("s9") != null ? player.findSong("s9").title  : "null"));

        System.out.println("--- 3. Play and move forward ---");
        Song playFirst = player.play("s1");
        System.out.println("play s1: " + (playFirst != null ? playFirst.title : "null"));

        Song playSecond = player.play("s9");
        System.out.println("play s9: " + (playSecond != null ? playSecond.title : "null"));

        Song firstNext = player.next();
        System.out.println("next: " + (firstNext != null ? firstNext.title : "null"));
        Song secondNext = player.next();
        System.out.println("next: " + (secondNext != null ? secondNext.title : "null"));
        System.out.println("history: " + player.showHistory());

        System.out.println("--- 4. Up next ---");
        System.out.println(player.queueLater("s6"));
        System.out.println(player.queueNext("s5"));
        System.out.println(player.queueNext("s1"));
        System.out.println("queue s9: " + player.queueLater("s9"));
        System.out.println("up next: " + player.showUpNext());
        Song queuedNext = player.next();
        System.out.println("next: " + (queuedNext != null ? queuedNext.title : "null"));
        System.out.println("now playing: " + (player.nowPlaying() != null ? player.nowPlaying().title : "null"));
        System.out.println("history: " + player.showHistory());

        System.out.println("--- 5. Back button ---");
        Song firstBack = player.back();
        System.out.println("back: " + (firstBack != null ? firstBack.title : "null"));

        System.out.println("up next: " + player.showUpNext());
        System.out.println("history: " + player.showHistory());

        System.out.println("--- 6. Remove songs ---");
        System.out.println("remove s5: " + player.removeSong("s5"));
        System.out.println("remove current (s3): " + player.removeSong("s3"));
        System.out.println("remove s9: " + player.removeSong("s9"));
        System.out.println("playlist: " + player.showPlaylist());
        System.out.println("up next: " + player.showUpNext());

        System.out.println("--- 7. Removed songs are skipped ---");
        Song firstResult = player.next();
        System.out.println("next: " + (firstResult != null ? firstResult.title : "null"));
        Song secondResult = player.next();
        System.out.println("next: " + (secondResult != null ? secondResult.title : "null"));
        Song endResult = player.next();
        System.out.println("next at end: " + (endResult != null ? endResult.title : "null"));
        System.out.println("now playing: " + (player.nowPlaying() != null ? player.nowPlaying().title : "null"));
        Song previous = player.back();
        System.out.println("back: " + (previous != null ? previous.title : "null"));
        System.out.println("up next: " + player.showUpNext());

        System.out.println("--- 8. Play counts ---");
        System.out.println("s1: " + player.playCount("s1"));
        System.out.println("s3: " + player.playCount("s3"));
        System.out.println("s6: " + player.playCount("s6"));
        System.out.println("s5 (removed): " + player.playCount("s5"));

        System.out.println("--- 9. Empty player ---");
        MusicPlayer emptyPlayer = new MusicPlayer();
        Song emptyNext = emptyPlayer.next();
        System.out.println("next: " + (emptyNext != null ? emptyNext.title : "null"));
        Song emptyBack = emptyPlayer.back();
        System.out.println("back: " + (emptyBack != null ? emptyBack.title : "null"));
        System.out.println("now playing: " + (emptyPlayer.nowPlaying() != null ? emptyPlayer.nowPlaying().title : "null"));
        System.out.println("playlist: " + emptyPlayer.showPlaylist());
    }

}
