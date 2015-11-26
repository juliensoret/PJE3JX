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

            bookDao.create(
                    new Book("1111111111111", "Tintin en Chine", "Hergé", "Casterman", 2005, "French")
                            .setImage()
                            .setGenre("BD humour")
                            .setDescription("L'histoire de Tintin sur le sol chinois.")
                            .setComment("Très drôle")
                            .setRead(true)
                            .setRating(5)
            );
            bookDao.create(
                    new Book("2222222222222", "Affaire Tintin", "Hergé", "Casterman", 1985, "French")
                            .setImage()
                            .setGenre("BD humour")
                            .setDescription("Tintin est jugé pour avoir mangé Milou.")
                            .setComment("Un peu cru, difficile à lire")
                            .setRead(true)
                            .setRating(3)
            );
            bookDao.create(
                    new Book("3333333333333", "Les recettes de Tintin", "Hergé", "Casterman", 2015, "French")
                            .setImage()
                            .setGenre("Cuisine")
                            .setDescription("Cuisine comme Tintin avec ses 48 recettes originales.")
                            .setComment("J'ai testé les 4 premières recettes, c'était bof.")
                            .setRead(true)
                            .setRating(2)
            );
            bookDao.create(
                    new Book("4444444444444", "Cuisiner la morue", "Manuel Delaveiro", "Portubooks", 1995, "French")
                            .setImage()
                            .setGenre("Cuisine")
                            .setDescription("Les recettes portugaises de Manuel.")
            );
            bookDao.create(
                    new Book("5555555555555", "Android pour les nuls", "Mark Truite", "TechnoD", 2013, "French")
                            .setImage()
                            .setGenre("Technologie")
                            .setDescription("Aide à l'utilisation d'Android.")
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
