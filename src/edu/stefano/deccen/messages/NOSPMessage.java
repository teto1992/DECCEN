package edu.stefano.deccen.messages;

public final class NOSPMessage {

    private long identifier; // the source node
    private long weight; // the number of shortest paths from the source to the currently sending node

    public NOSPMessage() {
    }

    public NOSPMessage(long id, long w) {
        this.setIdentifier(id);
        this.setWeight(w);
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long id) {
        identifier = id;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long w) {
        weight = w;
    }

    public String toString() {
        return ("[" + identifier + ", " + weight + "]");
    }

}
