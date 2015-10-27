package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class AddBookActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    EditText edTitle, edAuthor, edGenre, edDate;
    Button addButton;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edTitle = (EditText) findViewById(R.id.editTextTitle);
        edAuthor = (EditText) findViewById(R.id.editTextAuthor);
        edGenre = (EditText) findViewById(R.id.editTextGenre);
        edDate = (EditText) findViewById(R.id.editTextDate);
        addButton = (Button) findViewById(R.id.buttonAdd);
        error = (TextView) findViewById(R.id.textViewError);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkValues()){
                    try {
                        final Dao<Book, Integer> bookDao = getHelper().getBookDao();

                        bookDao.create(new Book(
                                edTitle.getText().toString(),
                                edAuthor.getText().toString(),
                                edGenre.getText().toString(),
                                Integer.parseInt(edDate.getText().toString())
                            )
                        );

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(AddBookActivity.this, "Livre ajouté !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddBookActivity.this, ListBooksActivity.class);
                    startActivity(intent);
                }
                else {
                    error.setText("Veuillez remplir tous les champs !");
                }
            }
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

    public boolean checkValues(){
        if(!edTitle.getText().toString().trim().equals(""))
            if(!edAuthor.getText().toString().trim().equals(""))
                if(!edGenre.getText().toString().trim().equals(""))
                    if(!edDate.getText().toString().trim().equals(""))
                        return true;
        return false;
    }

}
