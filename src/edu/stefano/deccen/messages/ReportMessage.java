package edu.stefano.deccen.messages;

public class ReportMessage {

    private long t; // the current node
    private long s; // the source node
    private long weight; // the number of shortest paths between s and t
    private long distance; // the distance between s and t

    public ReportMessage(long t, long s, long sigma, long distance) {
        this.distance = distance;
        this.s = s;
        this.t = t;
        this.weight = sigma;

    }

    public long getT() {
        return t;
    }

    public void setT(long n) {
        t = n;
    }

    public long getS() {
        return s;
    }

    public void setS(long n) {
        t = n;
    }

    public long getSigma() {
        return weight;
    }

    public void setSigma(long d) {
        weight = d;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String toString() {
        return ("(" + t + ", " + s  + " dist = " + distance);
        //+ ") o = " + weight
    }

}
