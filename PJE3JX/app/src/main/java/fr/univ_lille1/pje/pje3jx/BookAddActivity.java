package fr.univ_lille1.pje.pje3jx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class BookAddActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    EditText edIsbn, edTitle, edAuthor, edCollection, edPublisher, edGenre, edDate, edLanguage,
            edDescription, edComment;
    CheckBox edRead;
    SeekBar edRating;
    Button addButton;
    Book editable;
    TextView error, textViewRating;
    ImageView imageViewRating;
    LinearLayout linearLayoutRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edIsbn = (EditText) findViewById(R.id.editTextIsbn);
        edTitle = (EditText) findViewById(R.id.editTextTitle);
        edAuthor = (EditText) findViewById(R.id.editTextAuthor);
        edCollection = (EditText) findViewById(R.id.editTextCollection);
        edPublisher = (EditText) findViewById(R.id.editTextPublisher);
        edGenre = (EditText) findViewById(R.id.editTextGenre);
        edDate = (EditText) findViewById(R.id.editTextDate);
        edLanguage = (EditText) findViewById(R.id.editTextLanguage);
        edDescription = (EditText) findViewById(R.id.editTextDescription);
        edComment = (EditText) findViewById(R.id.editTextComment);
        edRead = (CheckBox) findViewById(R.id.checkBoxRead);
        linearLayoutRating = (LinearLayout) findViewById(R.id.linearLayoutRating);
        edRating = (SeekBar) findViewById(R.id.seekBarRating);
        imageViewRating = (ImageView) findViewById(R.id.imageViewRating);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        addButton = (Button) findViewById(R.id.buttonAdd);
        error = (TextView) findViewById(R.id.textViewError);

        final int id = this.getIntent().getIntExtra("id", -1);
        if (id >= 0) {
            this.setTitle(R.string.title_activity_edit_book);
            addButton.setText(R.string.action_edit);
            try {
                final Dao<Book, Integer> bookDao = getHelper().getBookDao();
                editable = bookDao.queryForId(id);
                edIsbn.setText(editable.getIsbn());
                edTitle.setText(editable.getTitle());
                edAuthor.setText(editable.getAuthor());
                edCollection.setText(editable.getCollection());
                edPublisher.setText(editable.getPublisher());
                edGenre.setText(editable.getGenre());
                edDate.setText(editable.getDate()+"");
                edLanguage.setText(editable.getLanguage());
                edDescription.setText(editable.getDescription());
                edComment.setText(editable.getComment());
                edRead.setChecked(editable.isRead());
                edRating.setProgress(editable.getRating());
                updateRatingViews(editable.getRating());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!edRead.isChecked()) {
            linearLayoutRating.setVisibility(View.INVISIBLE);
            edRating.setVisibility(View.INVISIBLE);
        }

        edRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    linearLayoutRating.setVisibility(View.VISIBLE);
                    edRating.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutRating.setVisibility(View.INVISIBLE);
                    edRating.setVisibility(View.INVISIBLE);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkValues()) {
                    try {
                        final Dao<Book, Integer> bookDao = getHelper().getBookDao();

                        Book newBook;
                        if (editable != null) {
                            newBook = editable;
                            newBook.update(
                                    edIsbn.getText().toString(),
                                    edTitle.getText().toString(),
                                    edAuthor.getText().toString(),
                                    edPublisher.getText().toString(),
                                    Integer.parseInt(edDate.getText().toString()),
                                    edLanguage.getText().toString()
                            );
                            Toast.makeText(
                                    BookAddActivity.this, R.string.text_bookedited, Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            newBook = new Book(
                                    edIsbn.getText().toString(),
                                    edTitle.getText().toString(),
                                    edAuthor.getText().toString(),
                                    edPublisher.getText().toString(),
                                    Integer.parseInt(edDate.getText().toString()),
                                    edLanguage.getText().toString()
                            );
                            newBook.setImage()
                            ;

                            Toast.makeText(
                                    BookAddActivity.this, R.string.text_bookadded, Toast.LENGTH_SHORT
                            ).show();
                        }

                        newBook.setCollection(edCollection.getText().toString())
                                .setGenre(edGenre.getText().toString())
                                .setDescription(edDescription.getText().toString())
                                .setComment(edComment.getText().toString())
                                .setRead(edRead.isChecked())
                                .setRating(edRating.getProgress())
                        ;
                        bookDao.createOrUpdate(newBook);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    finish();
                } else {
                    error.setText(R.string.error_allfieldsnotfilled);
                }
            }
        });

        edRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateRatingViews(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this,DatabaseHelper.class);
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

    public boolean checkValues() {
        if(!edTitle.getText().toString().trim().equals(""))
            if(!edAuthor.getText().toString().trim().equals(""))
                return true;
        return false;
    }

    public void updateRatingViews(int rating) {
        if(rating > 4) {
            imageViewRating.setImageResource(R.mipmap.icon_rating_5);
            textViewRating.setText(R.string.book_rating_5);
        }
        else if(rating > 3) {
            imageViewRating.setImageResource(R.mipmap.icon_rating_4);
            textViewRating.setText(R.string.book_rating_4);
        }
        else if(rating > 2) {
            imageViewRating.setImageResource(R.mipmap.icon_rating_3);
            textViewRating.setText(R.string.book_rating_3);
        }
        else if(rating > 1) {
            imageViewRating.setImageResource(R.mipmap.icon_rating_2);
            textViewRating.setText(R.string.book_rating_2);
        }
        else if(rating > 0) {
            imageViewRating.setImageResource(R.mipmap.icon_rating_1);
            textViewRating.setText(R.string.book_rating_1);
        }
        else {
            imageViewRating.setImageResource(R.mipmap.icon_rating_0);
            textViewRating.setText(R.string.book_rating_0);
        }
    }

}
