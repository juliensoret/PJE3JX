package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class BookListFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;
    private BookAdapter adapter;
    private Dao<Book, Integer> bookDao;
    private List<Book> bookList;
    ListView mListView;
    int bId, position;
    private boolean isTwoPane;
    private final int RESULT_OK = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        try {
            bookDao = getHelper().getBookDao();
            bookList = bookDao.queryForAll();
            if (bookList.isEmpty()) {
                fillBookListWithExamples();
                bookList = bookDao.queryForAll();
            }
            mListView = (ListView) view.findViewById(R.id.listView);
            adapter = new BookAdapter(
                    getActivity(), bookList
            );
            mListView.setAdapter(adapter);

            /*bookdisplay*/
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    bId = bookList.get(pos).getId();

                    isTwoPane = (getActivity().findViewById(R.id.details_layout) != null);

                    if (isTwoPane) {
                        BookDisplayFragment fragment = new BookDisplayFragment();
                        Bundle nBundle = new Bundle();
                        nBundle.putInt("id", bId);
                        fragment.setArguments(nBundle);
                        getFragmentManager().beginTransaction().replace(R.id.details_layout, fragment).commit();

                    } else {
                        Intent intent = new Intent(getActivity(), BookDisplayActivity.class);
                        intent.putExtra("id", bId);
                        startActivity(intent);
                    }
                }
            });

            /*edit,delete*/
            mListView.setLongClickable(true);
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                    bId = bookList.get(pos).getId();
                    position = pos;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getActivity(), R.string.text_bookdeleted, Toast.LENGTH_SHORT).show();
                            try {
                                bookDao.deleteById(bId);
                                bookList.remove(position);
                                mListView.invalidateViews();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    builder.setNeutralButton(R.string.action_edit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), BookAddActivity.class);
                            intent.putExtra("id", bId);
                            startActivityForResult(intent, RESULT_OK);
                        }
                    });

                    builder.setNegativeButton(R.string.text_cancel, null);

                    builder.setMessage(R.string.text_editdeletemessage)
                            .setTitle(R.string.action_editdelete);

                    builder.create().show();

                    return true;
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RESULT_OK:
                bookList.clear();
                try {
                    bookDao = getHelper().getBookDao();
                    bookList.addAll(bookDao.queryForAll());
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void fillBookListWithExamples() throws SQLException {
        bookDao.create(
                new Book("9782253094142", "Germinal", "Émile Zola", "Le Livre de Poche", 2011, "French")
                        .setImage()
                        .setGenre("Roman")
                        .setDescription("Voici, dans la France moderne et industrielle, les " +
                                "\" Misérables \" de Zola. Ce roman des mineurs, c'est aussi " +
                                "l'Enfer, dans un monde dantesque, où l'on \" voyage au bout de " +
                                "la nuit \". Mais à la fin du prodigieux itinéraire au centre de " +
                                "la terre, du fond du souterrain où il a vécu si longtemps " +
                                "écrasé, l'homme enfin se redresse et surgit dans une révolte " +
                                "pleine d'espoirs.\n" +
                                "C'est la plus belle et la plus grande œuvre de Zola, le poème " +
                                "de la fraternité dans la misère, et le roman de la condition " +
                                "humaine.")
                        .setComment("Un peu long")
                        .setRead(true)
                        .setRating(3)
        );
        bookDao.create(
                new Book("9781781101032", "Harry Potter à L’école des Sorciers",
                        "J.K. Rowling, Jean-François Ménard", "Pottermore", 1997, "French")
                        .setImage()
                        .setGenre("Roman")
                        .setDescription("Le jour de ses onze ans, Harry Potter, un orphelin élevé" +
                                " par un oncle et une tante qui le détestent, voit son existence" +
                                " bouleversée. Un géant vient le chercher pour l’emmener à " +
                                "Poudlard, une école de sorcellerie!")
                        .setComment("Très intéressant. La première partie est la meilleure!")
                        .setRead(true)
                        .setRating(5)
        );
        bookDao.create(
                new Book("9782709638821", "Steve Jobs", "Walter Isaacson", "JC Lattès", 2011, "French")
                        .setImage()
                        .setGenre("Biographie")
                        .setDescription("Suggéré par le créateur d’Apple, qui fait face à une " +
                                "maladie redoutable, Steve Jobs, à partir de plus de quarante " +
                                "entretiens menés sur plus de deux ans et d’interviews d’une " +
                                "centaine de membres de sa famille, amis, rivaux, concurrents " +
                                "et collègues.")
                        .setComment("Très mauvais, je préfère Android.")
                        .setRead(true)
                        .setRating(0)
        );
    }

}
