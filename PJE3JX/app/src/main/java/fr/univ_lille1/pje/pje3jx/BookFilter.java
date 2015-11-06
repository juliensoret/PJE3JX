package fr.univ_lille1.pje.pje3jx;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookFilter {
    private String name;
    private HashMap<String,String> criterias;

    public BookFilter(String n){
        name = n;
        criterias = new HashMap<>();
    }

    /**
     * Add a search criteria.
     * @param field field of the book to compare
     * @param s string to compare
     */
    public void addFilter(String field, String s){
        criterias.put(field, s);
    }

    /**
     * Remove a search criteria
     * @param field field to delete
     */
    public void removeFilter(String field){
        criterias.remove(field);
    }

    /**
     * Tells if a book passes the filter
     * @param b the book
     * @return true if the book validates all the criterias
     */
    public boolean isSelected(Book b){
        for (String mapKey : criterias.keySet()){

            String filterValue = cleanString(criterias.get(mapKey));
            String bookValueString = "";
            int bookValueInt = 0;

            switch(mapKey) {
                case "name":
                    bookValueString = cleanString(b.getName());
                    break;
                case "author":
                    bookValueString = cleanString(b.getAuthor());
                    break;
                case "genre":
                    bookValueString = cleanString(b.getGenre());
                    break;
                case "yearStart":
                    bookValueInt = b.getDate();
                    break;
                case "yearEnd":
                    bookValueInt = b.getDate();
                    break;
            }

            if(mapKey.equals("name") || mapKey.equals("author") || mapKey.equals("genre")) {
                if(!filterValue.equals(bookValueString))
                    return false;
            }
            else if(mapKey.equals("yearStart")){
                if(bookValueInt < Integer.parseInt(filterValue))
                    return false;
            }
            else if(mapKey.equals("yearEnd")){
                if(bookValueInt > Integer.parseInt(filterValue))
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns the books passing the filter from the list given as parameters
     * @param list List of books to filter
     * @return the filtered list
     */
    public List<Book> getFilteredList(List<Book> list) {
        List<Book> results = new ArrayList<>();
        for(Book b:list)
            if (isSelected(b))
                results.add(b);
        return results;
    }

    public String getName(){
        return name;
    }

    /**
     * Clean the string by removing spaces and rewriting with lowercase.
     * @param s String to clean
     * @return the cleaned string
     */
    public String cleanString(String s) {
        if(s != null)
            return s.toLowerCase().replaceAll(" ","");
        return "";
    }
}
