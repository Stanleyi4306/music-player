/**
 * 
 * // static because an Action object doesn't need to be attached to a particular MusicPlayer object to exist
 */

class Action {
    String type;
    Node node;
    Node previous;
    Node next;
    int playCount;
    public Action(String type, Node node, int playCount) {
        this.type = type;
        this.node = node;
        this.previous = null;
        this.next = null;
        this.playCount = playCount;
    } 
}
