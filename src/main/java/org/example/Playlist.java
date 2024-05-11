package org.example;

import lombok.Getter;

@Getter
public class Playlist implements Listable{
    private String name;
    private int id_playlist;
    private int id_account;
    public Playlist(String name, int id_playlist, int id_account){
        this.name = name;
        this.id_playlist = id_playlist;
        this.id_account = id_account;
    }

    @Override
    public String toString() {
        return name + " [ID: " + id_playlist + "]";
    }
}
