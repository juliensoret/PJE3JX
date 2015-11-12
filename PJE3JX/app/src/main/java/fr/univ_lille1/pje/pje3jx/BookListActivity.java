package fr.univ_lille1.pje.pje3jx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    private Dao<Book, Integer> bookDao;
    private List<Book> bookList;
    ListView mListView;
    int bId, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            bookDao =  getHelper().getBookDao();
            bookList = bookDao.queryForAll();
            Log.d("d booklist", bookList.size()+"");
            if(bookList.isEmpty()) {
                fillBookListWithExamples();
            }

            mListView = (ListView) findViewById(R.id.listView);

            final BookAdapter adapter = new BookAdapter(
                    BookListActivity.this, bookList
            );
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    bId = bookList.get(pos).getId();
                    Intent intent = new Intent(BookListActivity.this, BookDisplayActivity.class);
                    intent.putExtra("id", bId);
                    startActivity(intent);
                }
            });

            mListView.setLongClickable(true);
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                    bId = bookList.get(pos).getId();
                    position = pos;

                    AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);

                    builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(BookListActivity.this, R.string.text_bookdeleted, Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(BookListActivity.this, BookAddActivity.class);
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
