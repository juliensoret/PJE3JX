package fr.univ_lille1.pje.pje3jx;

import android.graphics.Color;

import java.util.ArrayList;

public class BookLibrary extends ArrayList<Book> {

    /* singleton */
    private static BookLibrary list = new BookLibrary();

    /* only method to get singleton */
    public static BookLibrary getInstance() {
        if(list.isEmpty())
            fillWithExamples();
        return list;
    }

    public void addBook(Book b) {
        if(list.isEmpty())
            fillWithExamples();
        list.add(b);
    }

    public void deleteBook(Book b) {
        if(list.isEmpty())
            fillWithExamples();
        list.remove(b);
    }

    public void deleteBook(int position) {
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
