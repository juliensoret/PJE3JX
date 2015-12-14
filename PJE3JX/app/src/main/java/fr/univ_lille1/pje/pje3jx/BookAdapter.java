package fr.univ_lille1.pje.pje3jx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_book,parent, false);
        }

        BookViewHolder viewHolder = (BookViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new BookViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            viewHolder.genre = (TextView) convertView.findViewById(R.id.genre);
            convertView.setTag(viewHolder);
        }


        Book book = getItem(position);

        viewHolder.title.setText(book.getTitle());
        viewHolder.author.setText(book.getAuthor());
        viewHolder.avatar.setImageBitmap(getPhoto(book.getImage()));
        viewHolder.genre.setText(book.getGenre());

        return convertView;
    }

    public Bitmap getPhoto(String path){
         /*display photo*/
        String filepath;
        filepath = path;
        Bitmap bm;
        File file = new File(filepath);
        if (file.exists()) {
            bm = BitmapFactory.decodeFile(filepath);
        }
        else{
            bm = BitmapFactory.decodeFile("/sdcard/PJEImage/2.jpg");//photo default
        }
        return bm;
    }

    private class BookViewHolder{
        public TextView title;
        public TextView author;
        public ImageView avatar;
        public TextView genre;
    }
}
