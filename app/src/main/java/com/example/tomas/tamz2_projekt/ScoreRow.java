package com.example.tomas.tamz2_projekt;

public class ScoreRow {
    public long id;
    public String name;
    public long time;
    public int score;

    public ScoreRow(long id, String name, long time, int score){
        this.id=id;
        this.name=name;
        this.time=time;
        this.score=score;
    }
}
