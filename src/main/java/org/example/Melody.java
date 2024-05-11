package org.example;

import lombok.Getter;

import java.awt.print.Pageable;

@Getter
public class Melody implements Listable {
    private String name;
    private String author;
    private int year;
    private int id_melody;

    Melody(String name, String author, int year, int id_melody) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.id_melody = id_melody;
    }

    @Override
    public String toString() {
        return name + " - " + author + " (" + year + ") [ID: " + id_melody + "]";
    }
}
