package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class FiltersListResultsActivity extends AppCompatActivity{
    private DatabaseHelper databaseHelper = null;
    ListView mListView;
    List<Book> bookList, filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BookFilterCatalog bfc = BookFilterCatalog.getInstance();
        final BookFilter filter = bfc.get(this.getIntent().getIntExtra("position", 0));

        setTitle("Filtre : " + filter.getListName());

        try {
            bookList = getHelper().getBookDao().queryForAll();
            filteredList = filter.getFilteredList(bookList);

            mListView = (ListView) findViewById(R.id.listView);
            final BookAdapter adapter = new BookAdapter(
                    FiltersListResultsActivity.this, filteredList
            );
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    Intent intent = new Intent(FiltersListResultsActivity.this, BookDisplayActivity.class);
                    intent.putExtra("id", filteredList.get(pos).getId());
                    startActivity(intent);
                }
            });
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
