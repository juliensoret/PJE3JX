package fr.univ_lille1.pje.pje3jx;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class BookDisplayActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    TextView textViewIsbn, textViewTitle, textViewAuthor, textViewCollection, textViewPublisher,
            textViewGenre, textViewDate, textViewLanguage, textViewDescription, textViewComment,
            textViewRating;
    ImageView imageViewCover, imageViewRead, imageViewRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_display);

        textViewIsbn = (TextView) findViewById(R.id.textViewIsbn);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewAuthor = (TextView) findViewById(R.id.textViewAuthor);
        textViewCollection = (TextView) findViewById(R.id.textViewCollection);
        textViewPublisher = (TextView) findViewById(R.id.textViewPublisher);
        imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        textViewGenre = (TextView) findViewById(R.id.textViewGenre);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewLanguage = (TextView) findViewById(R.id.textViewLanguage);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewComment = (TextView) findViewById(R.id.textViewComment);
        imageViewRead = (ImageView) findViewById(R.id.imageViewRead);
        imageViewRating = (ImageView) findViewById(R.id.imageViewRating);
        textViewRating = (TextView) findViewById(R.id.textViewRating);

        final int id = this.getIntent().getIntExtra("id", -1);
        if (id >= 0) {
            try {
                final Dao<Book, Integer> bookDao = getHelper().getBookDao();
                Book b = bookDao.queryForId(id);
                textViewIsbn.setText(b.getIsbn());
                textViewTitle.setText(b.getTitle());
                textViewAuthor.setText(b.getAuthor());
                textViewCollection.setText(b.getCollection());
                textViewPublisher.setText(b.getPublisher());
                imageViewCover.setImageDrawable(new ColorDrawable(b.getImage()));
                textViewGenre.setText(b.getGenre());
                if(b.getDate()!=0) textViewDate.setText(b.getDate()+"");
                textViewLanguage.setText(b.getLanguage());
                textViewDescription.setText(b.getDescription());
                textViewComment.setText(b.getComment());
                if(b.isRead())
                    imageViewRead.setImageResource(R.mipmap.icon_check);
                if(b.getRating() > 4) {
                    imageViewRating.setImageResource(R.mipmap.icon_rating_5);
                    textViewRating.setText(R.string.book_rating_5);
                }
                else if(b.getRating() > 3) {
                    imageViewRating.setImageResource(R.mipmap.icon_rating_4);
                    textViewRating.setText(R.string.book_rating_4);
                }
                else if(b.getRating() > 2) {
                    imageViewRating.setImageResource(R.mipmap.icon_rating_3);
                    textViewRating.setText(R.string.book_rating_3);
                }
                else if(b.getRating() > 1) {
                    imageViewRating.setImageResource(R.mipmap.icon_rating_2);
                    textViewRating.setText(R.string.book_rating_2);
                }
                else if(b.getRating() > 0) {
                    imageViewRating.setImageResource(R.mipmap.icon_rating_1);
                    textViewRating.setText(R.string.book_rating_1);
                }
                else {
                    imageViewRating.setImageResource(R.mipmap.icon_rating_0);
                    textViewRating.setText(R.string.book_rating_0);
                }
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
