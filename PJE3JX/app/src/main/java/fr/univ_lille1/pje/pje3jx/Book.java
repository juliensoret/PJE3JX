package fr.univ_lille1.pje.pje3jx;

import android.graphics.Color;

import java.util.Random;

public class Book {

    private String name;
    private String author;
    private int image;
    private String genre;
    private int date;

    public Book(String name, String author, int image, String genre, int date) {
        this.name = name;
        this.author = author;
        this.image = image;
        this.genre = genre;
        this.date = date;
    }

    public Book(String name, String author, String genre, int date) {
        this(
                name,
                author,
                Color.rgb(
                        (int) ( Math.random() * 200) + 50,
                        (int) ( Math.random() * 200) + 50,
                        (int) ( Math.random() * 200) + 50
                ),
                genre,
                date)
        ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
