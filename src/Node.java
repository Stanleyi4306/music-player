/**
 * Node = Where the track lives in the playlist chain (pointing to its neighbors).
 */
public class Node {
    public Song song;
    public Node prev;
    public Node next;

// Constructor to create a new node with a given song
public Node(Song song) {
    this.song = song;
    this.prev = null;
    this.next = null;
}

}

