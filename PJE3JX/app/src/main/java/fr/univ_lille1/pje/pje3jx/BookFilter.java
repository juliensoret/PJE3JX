package fr.univ_lille1.pje.pje3jx;

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
                if (cleanString(b.getGenre()).equals(cleanString(detail))){
                    return true;
                }
                else
                    break;
        }

        return false;
    }

    public String cleanString(String s) {
        return s.toLowerCase().replaceAll(" ","");
    }
}
