package fr.univ_lille1.pje.pje3jx;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;


import fr.univ_lille1.pje.pje3jx.data.DatabaseHelper;

public class BookDisplayFragment extends android.app.Fragment {
    private DatabaseHelper databaseHelper = null;
    TextView textViewIsbn, textViewTitle, textViewAuthor, textViewCollection, textViewPublisher,
            textViewGenre, textViewDate, textViewLanguage, textViewDescription, textViewComment,
            textViewRating;
    ImageView imageViewCover, imageViewRead, imageViewRating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_display, container, false);

        textViewIsbn = (TextView) view.findViewById(R.id.textViewIsbn);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewAuthor = (TextView) view.findViewById(R.id.textViewAuthor);
        textViewCollection = (TextView) view.findViewById(R.id.textViewCollection);
        textViewPublisher = (TextView) view.findViewById(R.id.textViewPublisher);
        imageViewCover = (ImageView) view.findViewById(R.id.imageViewCover);
        textViewGenre = (TextView) view.findViewById(R.id.textViewGenre);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewLanguage = (TextView) view.findViewById(R.id.textViewLanguage);
        textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
        textViewComment = (TextView) view.findViewById(R.id.textViewComment);
        imageViewRead = (ImageView) view.findViewById(R.id.imageViewRead);
        imageViewRating = (ImageView) view.findViewById(R.id.imageViewRating);
        textViewRating = (TextView) view.findViewById(R.id.textViewRating);

        final int id = getArguments().getInt("id");
        if (id >= 0) {
            try {
                final Dao<Book, Integer> bookDao = getHelper().getBookDao();
                Book b = bookDao.queryForId(id);
                textViewIsbn.setText(b.getIsbn());
                textViewTitle.setText(b.getTitle());
                textViewAuthor.setText(b.getAuthor());
                textViewCollection.setText(b.getCollection());
                textViewPublisher.setText(b.getPublisher());
                textViewGenre.setText(b.getGenre());
                if(b.getDate()!=0) textViewDate.setText(b.getDate()+"");
                textViewLanguage.setText(b.getLanguage());
                textViewDescription.setText(b.getDescription());
                textViewComment.setText(b.getComment());
                if(b.getImage()!=null)
                    imageViewCover.setImageBitmap(b.getImage());
                else
                    imageViewCover.setImageResource(R.mipmap.defaultphoto);
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
        return view;
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
