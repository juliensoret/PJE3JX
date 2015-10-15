package fr.univ_lille1.pje.pje3jx;

import java.util.ArrayList;
import java.util.List;

public class BookFilterCatalog extends BookFilter{

    private static List<String> filterLists = new ArrayList<>();

    public BookFilterCatalog(String standard, String detail) {
        super(standard,detail);
    }

    public static List<String> getFilterLists() {
        if(filterLists.isEmpty())
            fillWithExamples();
        return filterLists;
    }

    public List<Book> getFilteredList() {
        List<Book> list = new ArrayList<>();
        for(Book b:BookLibrary.getInstance())
            if (isSelected(b))
                list.add(b);
        return list;
    }

    public static void addList(String genre){
        filterLists.add(genre);
    }

    public static void deleteList(int position) {
        if(filterLists.isEmpty())
            fillWithExamples();
        filterLists.remove(filterLists.get(position));
    }

    public static void fillWithExamples() {
        filterLists.add("Technologie");
        filterLists.add("BD humour");
        filterLists.add("Roman");
    }

}
