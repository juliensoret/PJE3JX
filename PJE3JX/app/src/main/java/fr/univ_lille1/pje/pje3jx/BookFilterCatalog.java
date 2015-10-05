package fr.univ_lille1.pje.pje3jx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 15/10/4.
 */
public class BookFilterCatalog extends BookFilter{

    private List<Book> list;
    private String standard;

    public BookFilterCatalog(String standard) {
        super(standard);
        BookLibrary library = new BookLibrary();
        List<Book> list_books = library.getBooks();
         for(Book b:list_books)
                {
                   if (isSelected(b)){
                }    list.add(b)  ;
        }

    }

    public List<Book> getBooks() {
        return list;
    }

    public Book createBook(String name, String author, int image, String genre) {
        return new Book(name, author, image, genre);
    }

    public void addBook(Book b) {
        list.add(b);
    }

    public void deleteBook(Book b) {
        list.remove(b);
    }
}
