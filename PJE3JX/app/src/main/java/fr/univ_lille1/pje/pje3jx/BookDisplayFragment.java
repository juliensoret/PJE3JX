package fr.univ_lille1.pje.pje3jx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

/**
 * Created by fan on 15/11/19.
 */
public class BookDisplayFragment extends android.app.Fragment {
    private DatabaseHelper databaseHelper = null;
    TextView textViewTitle, textViewAuthor, textViewGenre, textViewDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_display, container, false);

        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewAuthor = (TextView) view.findViewById(R.id.textViewAuthor);
        textViewGenre = (TextView) view.findViewById(R.id.textViewGenre);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);

        Integer id = getArguments().getInt("id");
        if (id >= 0) {
            try {
                final Dao<Book, Integer> bookDao = getHelper().getBookDao();
                Book b = bookDao.queryForId(id);
                textViewTitle.setText(b.getTitle());
                textViewAuthor.setText(b.getAuthor());
                textViewGenre.setText(b.getGenre());
                textViewDate.setText(b.getDate()+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
