package fr.univ_lille1.pje.pje3jx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            viewHolder.info = (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(viewHolder);
        }

        Book book = getItem(position);

        viewHolder.title.setText(book.getTitle());
        viewHolder.author.setText(book.getAuthor());
        viewHolder.info.setText(book.getDate() + " - " + book.getPublisher());
        if(book.getImage()!=null)
            viewHolder.avatar.setImageBitmap(book.getImage());
        else
            viewHolder.avatar.setImageResource(R.mipmap.defaultphoto);

        return convertView;
    }

    private class BookViewHolder{
        public TextView title;
        public TextView author;
        public ImageView avatar;
        public TextView info;
    }
}
