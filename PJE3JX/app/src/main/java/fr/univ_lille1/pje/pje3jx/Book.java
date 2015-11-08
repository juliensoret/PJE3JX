package fr.univ_lille1.pje.pje3jx;

import android.graphics.Color;
import com.j256.ormlite.field.DatabaseField;

public class Book {

    @DatabaseField(generatedId = true, columnName = "book_id")
    private int bookId;

    private String name;
    private String author;
    private int image;
    private String genre;
    private int date;

    public Book(){}

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

    public int getId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getImage() {
        return image;
    }

    public String getGenre() {
        return genre;
    }

    public int getDate() {
        return date;
    }

    public void update(String nName, String nAuthor, String nGenre, int nDate) {
        name = nName;
        author = nAuthor;
        genre = nGenre;
        date = nDate;
    }

}
