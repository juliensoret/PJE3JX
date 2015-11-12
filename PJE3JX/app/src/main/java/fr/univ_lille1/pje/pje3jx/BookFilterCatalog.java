package fr.univ_lille1.pje.pje3jx;

import java.util.ArrayList;
import java.util.List;

public class BookFilterCatalog extends ArrayList<BookFilter> {

    /* singleton */
    private static BookFilterCatalog filterLists = new BookFilterCatalog();

    private BookFilterCatalog(){
    }

    /* only method to get singleton instance */
    public static BookFilterCatalog getInstance() {
        if(filterLists.isEmpty())
            fillWithExamples();
        return filterLists;
    }

    /**
     * Gives a list of all filters lists' names.
     * @return filters lists' names
     */
    public List<String> getFilterListNames(){
        ArrayList<String> namesList = new ArrayList<>();
        for (BookFilter bf : getInstance()) {
            namesList.add(bf.getListName());
        }
        return namesList;
    }

    /**
     * Delete a filters list
     */
    public void deleteList(int position) {
        filterLists.remove(getInstance().get(position));
    }

    public static void fillWithExamples() {
        BookFilter bf;

        bf = new BookFilter("Livres de Hergé");
        bf.addFilter("author", "hergé");
        filterLists.add(bf);

        bf = new BookFilter("Pour la Cuisine");
        bf.addFilter("genre", "cuisine");
        filterLists.add(bf);

        bf = new BookFilter("Mes Romans");
        bf.addFilter("genre", "roman");
        filterLists.add(bf);

        bf = new BookFilter("Après l'année 2000");
        bf.addFilter("yearStart", "2000");
        filterLists.add(bf);
    }

}
