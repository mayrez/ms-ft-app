package com.example.may.msocial.arrayAdapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.may.msocial.R;
import com.example.may.msocial.models.Phrase;

import java.util.List;


public class PhrasesListArrayAdapter extends ArrayAdapter<Phrase> {
    private LayoutInflater layoutInflater;

    public PhrasesListArrayAdapter(Context context, List<Phrase> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Phrases_Holder holder = null;
        if (convertView == null) {
            holder = new Phrases_Holder();

            convertView = layoutInflater.inflate(R.layout.phrases_listview_row, null);
            holder.setTextViewTitle((TextView) convertView.findViewById(R.id.phraseSelectionList));
            convertView.setTag(holder);
        } else {
            holder = (Phrases_Holder) convertView.getTag();
        }

        final Phrase row = getItem(position);
        holder.getTextViewTitle().setText(row.getPhrase());
        holder.getTextViewTitle().setTextSize(18);
        return convertView;
    }


    class Phrases_Holder {

        TextView textViewTitle;


        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public void setTextViewTitle(TextView textViewTitle) {
            this.textViewTitle = textViewTitle;
        }


    }

}
