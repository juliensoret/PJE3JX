package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class BookAddActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    EditText edIsbn, edTitle, edAuthor, edCollection, edPublisher, edGenre, edDate, edLanguage,
            edDescription, edComment;
    CheckBox edRead;
    SeekBar edRating;
    Button addButton, addPhoto;
    Book editable;
    TextView error, textViewRating;
    ImageView imageViewRating, edImage;
    LinearLayout linearLayoutRating;
    static final int IMAGE_RESULT_CODE = 1;//use the camera to take photo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

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
        edImage = (ImageView) findViewById(R.id.photo);
        addPhoto = (Button) findViewById(R.id.buttonPhoto);

        // If there is a book to edit, pre-fill the EditTexts
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
        // If the book is not read, hide the rating part
        if (!edRead.isChecked()) {
            linearLayoutRating.setVisibility(View.INVISIBLE);
            edRating.setVisibility(View.INVISIBLE);
        }
        // Display/Hide the rating part depending on Read CheckBox
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
        //Take photo
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState(); // juge if have SD card
                if (state.equals(Environment.MEDIA_MOUNTED)) { // use the camera
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, IMAGE_RESULT_CODE);
                } else {
                    Toast.makeText(BookAddActivity.this, "verifier si il y a de SD card", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkValues()) {
                    try {
                        final Dao<Book, Integer> bookDao = getHelper().getBookDao();
                        int date = edDate.getText().toString().length() > 0 ?
                                Integer.parseInt(edDate.getText().toString()) : 0;

                        Book newBook;
                        if (editable != null) {
                            newBook = editable;
                            newBook.update(
                                    edIsbn.getText().toString(),
                                    edTitle.getText().toString(),
                                    edAuthor.getText().toString(),
                                    edPublisher.getText().toString(),
                                    date,
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
                                    date,
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
        // Update the related views when the rating changes
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

    /*obtain the photo, display and save in the SD card */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_RESULT_CODE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            edImage.setImageBitmap(bitmap);//display
            //save in the SD card
            File appDir = new File(Environment.getExternalStorageDirectory(), "PJEImage");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = edTitle.getText().toString() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Check if the Title and the Author are correctly filled
     */
    public boolean checkValues() {
        if(!edTitle.getText().toString().trim().equals(""))
            if(!edAuthor.getText().toString().trim().equals(""))
                return true;
        return false;
    }

    /**
     * Update ImageView and TextView with the corresponding image and text of the rating
     */
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
