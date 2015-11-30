package fr.univ_lille1.pje.pje3jx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class FiltersListAddActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    EditText edName, edFTitle, edFAuthor, edFCollection, edFGenre, edFYearStart, edFYearEnd;
    Button addButton;
    TextView error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterslist_add);

        edName = (EditText) findViewById(R.id.editTextName);
        edFTitle = (EditText) findViewById(R.id.editTextFTitle);
        edFAuthor = (EditText) findViewById(R.id.editTextFAuthor);
        edFCollection = (EditText) findViewById(R.id.editTextFCollection);
        edFGenre = (EditText) findViewById(R.id.editTextFGenre);
        edFYearStart = (EditText) findViewById(R.id.editTextFYearStart);
        edFYearEnd = (EditText) findViewById(R.id.editTextFYearEnd);
        addButton = (Button) findViewById(R.id.addButton);
        error = (TextView) findViewById(R.id.textViewError);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkValues()){

                    FiltersList newFilter = new FiltersList(edName.getText().toString());

                    if(!edFTitle.getText().toString().equals(""))
                        newFilter.setTitleFilter(edFTitle.getText().toString());
                    if(!edFAuthor.getText().toString().equals(""))
                        newFilter.setAuthorFilter(edFAuthor.getText().toString());
                    if(!edFCollection.getText().toString().equals(""))
                        newFilter.setCollectionFilter(edFCollection.getText().toString());
                    if(!edFGenre.getText().toString().equals(""))
                        newFilter.setGenreFilter(edFGenre.getText().toString());
                    if(!edFYearStart.getText().toString().equals(""))
                        newFilter.setStartYearFilter(Integer.parseInt(edFYearStart.getText().toString()));
                    if(!edFYearEnd.getText().toString().equals(""))
                        newFilter.setEndYearFilter(Integer.parseInt(edFYearEnd.getText().toString()));

                    Toast.makeText(
                            FiltersListAddActivity.this, R.string.text_listadded, Toast.LENGTH_SHORT
                    ).show();

                    try {
                        final Dao<FiltersList, Integer> filtersListDao = getHelper().getFiltersListDao();
                        filtersListDao.create(newFilter);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
                else {
                        error.setText(R.string.error_minimumfieldsnotfilled);
                }
            }
        });
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

    /**
     * Checks the name of the filtersList and at least one of the filters are correctly filled
     */
    public boolean checkValues(){
        if(!edName.getText().toString().trim().equals(""))
            if(
                !edFTitle.getText().toString().trim().equals("")
                || !edFAuthor.getText().toString().trim().equals("")
                || !edFCollection.getText().toString().trim().equals("")
                || !edFGenre.getText().toString().trim().equals("")
                || !edFYearStart.getText().toString().trim().equals("")
                || !edFYearEnd.getText().toString().trim().equals("")
            )
                return true;
        return false;
    }
}
