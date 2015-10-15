package fr.univ_lille1.pje.pje3jx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddBookActivity extends AppCompatActivity {

    EditText edTitle, edAuthor, edGenre;
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
        addButton = (Button) findViewById(R.id.buttonAdd);
        error = (TextView) findViewById(R.id.textViewError);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkValues()){
                    BookLibrary.getInstance().addBook(
                            new Book(
                                    edTitle.getText().toString(),
                                    edAuthor.getText().toString(),
                                    edGenre.getText().toString()
                            )
                    );

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

    public boolean checkValues(){
        if(!edTitle.getText().toString().equals(""))
            if(!edAuthor.getText().toString().equals(""))
                if(!edGenre.getText().toString().equals(""))
                    return true;
        return false;
    }

}
