/**
 * Data Structures used for this MusicPlayer are:
 * 1. ArrayList
 * 2. HashMap
 * 3. LinkedList/Doubly LinkedList
 * 4. Stack
 * 5. Queue
 * 
 * I will use the buitl in Java Collections to implement these Data Structres
 * 
 */
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


public class MusicPlayer {

    private Song current; // current playing song, or Node in the playlist

    // Library (Map ID -> Node/Song)
    private Map<String, Node> library;

    // Up Next (Deque)
    private Deque<Song> upNext;

    // History (Stack / Deque)
    private Deque<Song> history;

    // Play Counts (Map ID -> Count)
    private Map <String, Integer> playCounts;

    // Custom Doubly Linked List head/tail for playlist
    private Node head;
    private Node tail;

    public MusicPlayer() {
        this.current = null;
        this.library = new HashMap<>();
        this.upNext = new ArrayDeque<>();
        this.history = new ArrayDeque<>();
        this.playCounts = new HashMap<>();
        this.head = null;
        this.tail = null;
    }


    
}



