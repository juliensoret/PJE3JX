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

/**
 * Created by fan on 15/11/19.
 */
public class BookListFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;
    private Dao<Book, Integer> bookDao;
    private List<Book> bookList;
    ListView mListView;
    int bId, position;
    private boolean isTwoPane;


    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);//this
        }
        return databaseHelper;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        try {
            bookDao = getHelper().getBookDao();
            bookList = bookDao.queryForAll();
            if (bookList.isEmpty()) {
                fillBookListWithExamples();
            }

            mListView = (ListView) view.findViewById(R.id.listView);


            final BookAdapter adapter = new BookAdapter(
                    getActivity(), bookList
            );
            mListView.setAdapter(adapter);



                /*bookdisplay*/
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    bId = bookList.get(pos).getId();

                    if (getActivity().findViewById(R.id.details_layout) != null) {
                        isTwoPane = true;
                    } else {
                        isTwoPane = false;
                    }
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
                                bookDao.delete(bookList.get(position));
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
                            startActivity(intent);
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





    public void fillBookListWithExamples() {
                try {
                    final Dao<Book, Integer> bookDao = getHelper().getBookDao();

                    bookDao.create(new Book("Tintin en Chine", "Hergé", "BD humour", 2005));
                    bookDao.create(new Book("L'Affaire Tintin ", "Hergé", "BD humour", 1985));
                    bookDao.create(new Book("Les recettes de Tintin", "Hergé", "Cuisine", 2015));
                    bookDao.create(new Book("Cuisiner la morue", "Manuel Delaveiro", "Cuisine", 1995));
                    bookDao.create(new Book("Android pour les nuls", "Mark Truite", "Technologie", 2013));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

}
