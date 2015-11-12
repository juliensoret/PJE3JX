package fr.univ_lille1.pje.pje3jx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class BookDisplayActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    TextView textViewTitle, textViewAuthor, textViewGenre, textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_display);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewAuthor = (TextView) findViewById(R.id.textViewAuthor);
        textViewGenre = (TextView) findViewById(R.id.textViewGenre);
        textViewDate = (TextView) findViewById(R.id.textViewDate);

        final int id = this.getIntent().getIntExtra("id", -1);
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
