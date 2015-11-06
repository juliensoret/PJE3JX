package fr.univ_lille1.pje.pje3jx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FiltersListAddActivity extends AppCompatActivity {

    EditText edName, edFTitle, edFAuthor, edFGenre, edFYearStart, edFYearEnd;
    Button addButton;
    TextView error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterslist_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edName = (EditText) findViewById(R.id.editTextName);
        edFTitle = (EditText) findViewById(R.id.editTextFTitle);
        edFAuthor = (EditText) findViewById(R.id.editTextFAuthor);
        edFGenre = (EditText) findViewById(R.id.editTextFGenre);
        edFYearStart = (EditText) findViewById(R.id.editTextFYearStart);
        edFYearEnd = (EditText) findViewById(R.id.editTextFYearEnd);
        addButton = (Button) findViewById(R.id.addButton);
        error = (TextView) findViewById(R.id.textViewError);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkValues()){

                    BookFilter newFilter = new BookFilter(edName.getText().toString());

                    if(!edFTitle.getText().toString().equals(""))
                        newFilter.addFilter("title", edFTitle.getText().toString());
                    if(!edFAuthor.getText().toString().equals(""))
                        newFilter.addFilter("author", edFAuthor.getText().toString());
                    if(!edFGenre.getText().toString().equals(""))
                        newFilter.addFilter("genre", edFGenre.getText().toString());
                    if(!edFYearStart.getText().toString().equals(""))
                        newFilter.addFilter("yearStart", edFYearStart.getText().toString());
                    if(!edFYearEnd.getText().toString().equals(""))
                        newFilter.addFilter("yearEnd", edFYearEnd.getText().toString());

                    Toast.makeText(
                            FiltersListAddActivity.this, R.string.text_listadded, Toast.LENGTH_SHORT
                    ).show();

                    BookFilterCatalog.getInstance().add(newFilter);

                    finish();
                }
                else {
                        error.setText(R.string.error_allfieldsnotfilled);
                }
            }
        });
    }

    public boolean checkValues(){
        if(!edName.getText().toString().trim().equals(""))
            if(
                !(
                    edFTitle.getText().toString().trim().equals("")
                    && edFAuthor.getText().toString().trim().equals("")
                    && !edFGenre.getText().toString().trim().equals("")
                    && !edFYearStart.getText().toString().trim().equals("")
                    && !edFYearEnd.getText().toString().trim().equals("")
                )
            )
                return true;
        return false;
    }
}
