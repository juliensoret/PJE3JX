package fr.univ_lille1.pje.pje3jx;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class BookLibrary {

    private static List<Book> list = new ArrayList<>();

    public static List<Book> getBooks() {
        if(list.isEmpty())
            fillWithExamples();
        return list;
    }

    public static void addBook(Book b) {
        if(list.isEmpty())
            fillWithExamples();
        list.add(b);
    }

    public static void deleteBook(Book b) {
        if(list.isEmpty())
            fillWithExamples();
        list.remove(b);
    }

    public static void deleteBook(int position) {
        if(list.isEmpty())
            fillWithExamples();
        list.remove(list.get(position));
    }

    public static void fillWithExamples() {
        list.add(
                new Book(
                        "Tintin en Chine",
                        "Hergé",
                        Color.RED,
                        "BD humour"
                )
        );
        list.add(
                new Book(
                        "Cuisiner la morue",
                        "Manuel Delaveiro",
                        Color.BLUE,
                        "Cuisine"
                )
        );
        list.add(
                new Book(
                        "Android pour les nuls",
                        "Mark Truite",
                        Color.GREEN,
                        "Technologie"
                )
        );
    }
}
