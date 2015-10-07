package fr.univ_lille1.pje.pje3jx;

/**
 * Created by fan on 15/10/4.
 */
public class BookFilter {
    protected String standard;
    protected String detail;


    public BookFilter(String standard, String detail){

        this.standard = standard;
        this.detail = detail;
    }
    public boolean isSelected( Book b){

        switch(standard) {
            case "name":
                if (b.getName().equals(detail))
                    return true;
                else
                    break;

            case "author":
                if (b.getAuthor().equals(detail))
                    return true;
                else
                    break;

            case "Genre":
                if (b.getGenre().equals(detail)){
                    return true;
                }
                else
                    break;
        }

        return false;
    }
}
