package fr.univ_lille1.pje.pje3jx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class FilteredListBooksActivity extends AppCompatActivity{
    private DatabaseHelper databaseHelper = null;
    private Dao<Book, Integer> bookDao;
    ListView mListView;
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BookFilterCatalog bfc = BookFilterCatalog.getInstance();
        BookFilter filter = bfc.get(this.getIntent().getIntExtra("position", 0));

        setTitle("Filtre : " + filter.getName());

        try {
            bookList = getHelper().getBookDao().queryForAll();
            mListView = (ListView) findViewById(R.id.listView);
            final BookAdapter adapter = new BookAdapter(
                    FilteredListBooksActivity.this, filter.getFilteredList(bookList)
            );
            mListView.setAdapter(adapter);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
