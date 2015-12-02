package fr.univ_lille1.pje.pje3jx;

import android.graphics.Color;
import com.j256.ormlite.field.DatabaseField;

public class Book {

    @DatabaseField(generatedId = true, columnName = "book_id")
    private int bookId;

    private String isbn;
    private String title;
    private String author;
    private String collection;
    private String publisher;
    private int image;
    private String genre;
    private int date;
    private String language;
    private String description;
    private String comment;
    private boolean read;
    private int rating;


    public Book(){}

    public Book(String isbn, String title, String author, String publisher, int date,
                String language) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.date = date;
        this.language = language;
    }

    public void update(String nisbn, String ntitle, String nauthor, String npublisher, int ndate,
                       String nlanguage) {
        this.isbn = nisbn;
        this.title = ntitle;
        this.author = nauthor;
        this.publisher = npublisher;
        this.date = ndate;
        this.language = nlanguage;
    }

    public Book setImage() {
        this.image = Color.rgb(
                (int) (Math.random() * 200) + 50,
                (int) (Math.random() * 200) + 50,
                (int) (Math.random() * 200) + 50
        );
        return this;
    }

    public Book setCollection(String ncollection) {
        this.collection = ncollection;
        return this;
    }

    public Book setGenre(String ngenre) {
        this.genre = ngenre;
        return this;
    }

    public Book setDescription(String ndescription) {
        this.description = ndescription;
        return this;
    }

    public Book setComment(String ncomment) {
        this.comment = ncomment;
        return this;
    }

    public Book setRead(boolean nread) {
        this.read = nread;
        return this;
    }

    public Book setRating(int nrating) {
        this.rating = nrating;
        return this;
    }

    public int getId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCollection() {
        return collection;
    }

    public String getPublisher() {
        return publisher;
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

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public String getComment() {
        return comment;
    }

    public boolean isRead() {
        return read;
    }

    public int getRating() {
        return rating;
    }

}
