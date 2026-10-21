/**
 * Data Structures used for this MusicPlayer are:
 * 1. ArrayList
 * 2. HashMap
 * 3. LinkedList/Doubly LinkedList
 * 4. Stack
 * 5. Queue
 * 
 * I will use the built-in Java Collections to implement these Data Structures
 * 
 */
import java.util.*;

public class MusicPlayer {

    private Song current; // Current song being played

    // Library (Map ID -> Node/Song)
    private Map<String, Node> library;

    // Up Next (Deque)
    private Deque<Song> upNext;

    // History (Stack / Deque)
    private Deque<Song> history;

    // Play Counts (Map ID -> Count)
    private Map <String, Integer> playCounts = new HashMap<>();

    // Custom Doubly Linked List head/tail for playlist
    private Node head;
    private Node tail;

    
    private Deque<Action> undoStack = new ArrayDeque<>();
    private Deque<Song> recentlyPlayed = new ArrayDeque<>();
    private boolean repeat = false;

    public MusicPlayer() {
        this.current = null;
        this.library = new HashMap<>();
        this.upNext = new ArrayDeque<>();
        this.history = new ArrayDeque<>();
        this.playCounts = new HashMap<>();
        this.head = null; 
        this.tail = null;
    }
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean addSong(String id, String title, String artist) {
        if (library.containsKey(id)) {
            return false; // Don't add a song if it already exists
        }

        Song newSong = new Song(id, title, artist); // Create a new Song to add to the library
        Node newNode = new Node(newSong); // Create a new Node for the song
      
        // Add to the library map
        library.put(id, newNode);

        // Add to the end of the playlist (Doubly Linked List)
        if (head == null) { // if Playlist is empty
            head = newNode;
            tail = newNode; // if Playlist is empty, head and tail both point to the new node
        } else {
            tail.next = newNode; // append to the end of the tail and point to the new node
            newNode.prev = tail;
            tail = newNode; // Move the tail to the new node
        }
        return true;
    }

    public Song findSong(String id) {
        Node node = library.get(id); // gets node in library in O(1) time
        if (node == null) {
            return null;
        }
        return node.song; // return the song in the node
    }

    public List<String> showPlaylist() {
        List<String> titles = new ArrayList<>();
        Node current = head;

        // Traverse down the doubly linked list (playlist) from head to tail
        while (current != null) {
            titles.add(current.song.title); // add the title of the song to the list
            current = current.next; // now move to the next node in the playlist
        }
        return titles; // return the list of song titles in the playlist
    }

    // Helper method that makes a song current and adds 1 to its play count
    private Song startSong(Song song) {
        current = song; // set the current song to the song passed in
        playCounts.merge(song.getId(), 1, Integer::sum); // increment the play count for the song by 1

        // If this song was alreadt recent, move it to the front
        recentlyPlayed.remove(song);
        recentlyPlayed.addFirst(song);

        if (recentlyPlayed.size() > 10) { // Keep only the 10 most recent distinct songs
            recentlyPlayed.removeLast();
        }
        return song; // return the song that is now current
        // returning either current or song is fine
    }
    public List<Song> recentlyPlayed() {
        return new ArrayList<>(recentlyPlayed);
    }

    public Song play(String id) {
        Node node = library.get(id); // WHen you play a song, you want to get it from the library in O(1) time
        if (node == null) {
            return null; // if the song doesn't exist, return null
        }
        // push the current song to the top of the history stack.
        if (current != null) {history.push(current);}
        return startSong(node.song);

    }
    public Song nowPlaying() {return current;}

    public int playCount(String id) {
        return playCounts.getOrDefault(id, 0); // return the play count for the song, or 0 if it doesn't exist
    }

    public boolean queueNext(String id) {
        Node node = library.get(id);
        if (node == null) {
            return false; // if the next song doesn't exist.
        }
        upNext.addFirst(node.song); // add the song to the front of the upNext queue
        return true;
    }

    public boolean queueLater(String id) {
        Node node = library.get(id); // get the song from the library in O(1) time
        if (node == null) {
            return false;
        }
        upNext.addLast(node.song); // addLast is a method of the Deque interface that adds an element to the end of the deque
        return true;
    }

    public List<String> showUpNext() {
        List<String> titles = new ArrayList<>(); // create a new list to hold the titles of the songs in the upNext queue

        for (Song song : upNext) {
            if (library.containsKey(song.getId())) {
            titles.add(song.title); // add the title of the song to the list 
            }
        }
        return titles; // returns a list of the titles of the songs in the upNext queue
    }

    public Song next() {
            Song nextSong = null;
            if (!upNext.isEmpty()) {
                nextSong = upNext.peekFirst(); // get the next song from the upNext queue without removing it 

                // Skip queued songs that have since been removed from the library
                while (nextSong != null && !library.containsKey(nextSong.getId())) { 
                    upNext.removeFirst(); // remove the song from the upNext queue if it's been removed from the library
                    nextSong = upNext.peekFirst(); // get the next song from the upNext queue
                }

                if (nextSong != null) {
                    upNext.removeFirst();  
                }
        }

        // If upNext has no usable song, follow playlist order
        if (nextSong == null && current != null) {
            Node currentNode = library.get(current.getId());

            if (currentNode != null && currentNode.next != null) { // if the current song is there and the song after it is there, then play the next song in the playlist
                nextSong = currentNode.next.song;
            } else if (currentNode != null && repeat && head != null) {
                nextSong = head.song;
            }
        }

        // No next song: leave current and history unchanged.
        if (nextSong == null) {return null;}
        if (current != null) {history.push(current);} // push the current song to the history stack
        return startSong(nextSong); // Finally start the next song and return it
        }

        public Song back() {
            Song previous = null;
            
            while (!history.isEmpty()) {
                Song lastPlayed = history.pop();// pop the last played song from the history stack
                if (library.containsKey(lastPlayed.getId())) {
                    previous = lastPlayed;
                    break;
                }
            }
           if (previous == null) {return null;} // if there is no previous song, return null
           if (current != null) {upNext.addFirst(current);} // add the current song to the front of the upNext queue
              return startSong(previous); // start the previous song and return it
        }

        public List<String> showHistory() {
            List<String> titles = new ArrayList<>();
            for (Song song : history) {
                titles.add(song.title); // add the title of the song to the list
            }
            return titles; // return the title of the songs in the history (stack)
        }

        public boolean removeSong(String id) {
            Node node = library.get(id);
            if (node == null) {return false;}

            // The current playing song cannot be removed 
            if (current != null && current.getId().equals(id)) { // if the current song playing is attempted to be removed return false
                return false;  
            }

            // to remove a song we have to unlink it from the doubly linked playlist.
            if (node.prev == null) {
                head = node.next;   // Removing the head
            } else {
                node.prev.next = node.next; 
            }

            if (node.next == null) {
                tail = node.prev;   // Removing the tail
            } else {
                node.next.prev = node.prev;
            }

            // now remove the song from the library and its play count map.
            library.remove(id);
            playCounts.remove(id);
            return true;

           
        }
        public boolean undo() {
            if (undoStack.isEmpty()) {return false;}
            // Pop the most recent action
            Action lastAction = undoStack.peek();
            if (lastAction.node == null) {return false;}

            Node node = lastAction.node;
            String id = node.song.getId();

            // Reverse the action based on its type
            if ("ADD".equals(lastAction.type)) {
                // can't remove the current song playing
                if (current != null && current.getId().equals(id)) {
                    return false;
                }
                // Unlink the song added
                if (node.prev == null) { // Undoing an ADD will mean removing the song from the linked list and library
                    head = node.next;
                } else {
                    node.prev.next = node.next;
                }

                if (node.next == null) {
                    tail = node.prev;
                } else {
                    node.next.prev = node.prev;
                }
                library.remove(id);
                playCounts.remove(id);
                // After the action is done, remove it from the undo stack.
                undoStack.pop();
                return true;
            } 
             if ("REMOVE".equals(lastAction.type)) {
            // Undoing a REMOVE means putting the song back into the playlist and library
            if (library.containsKey(id)) {return false;} // if it somehow already exits, don't re-add
            Node previous = lastAction.previous;
            Node next = lastAction.next;

            // Reconnect the node at its original position
            node.prev = previous;
            node.next = next;

            if (previous == null) {
                head = node;
            } else {
                previous.next = node;
            }

            if (next == null) {
                tail = node;
            } else {
                next.prev = node;
            }
            library.put(id, node);
            playCounts.put(id, lastAction.playCount);

            }
            return false;
        }


    public List<String> topSongs(int n) {
    List<String> topTitles = new ArrayList<>();

    if (n <= 0) {return topTitles;}

    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(playCounts.entrySet());

    sortedEntries.sort((a, b) ->
            Integer.compare(b.getValue(), a.getValue()));

    for (Map.Entry<String, Integer> entry : sortedEntries) {
        Node node = library.get(entry.getKey());

        if (node != null) {
            topTitles.add(node.song.title);

            if (topTitles.size() == n) {
                break;
            }
        }
    }

    return topTitles;
}

public void shuffleUpNext() {
    List<Song> shuffledSongs = new ArrayList<>();

    // copy songs from the playlist without changing the links
    Node node = head;
    while (node != null) {
        shuffledSongs.add(node.song); // use node.song instead of current because it adds the song stored in playlist rather than adding the same playing song repeatedly 
        node = node.next;
    }
    Collections.shuffle(shuffledSongs);

    // replace up next queue with the shuffled playlist when this method is called
    upNext.clear();
    for (Song song : shuffledSongs) {
        upNext.addLast(song);
    }
}
        

}

        

    
        

    





