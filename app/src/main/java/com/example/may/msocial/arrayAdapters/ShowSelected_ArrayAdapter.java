package com.example.may.msocial.arrayAdapters;

import java.util.List;

import com.example.may.msocial.R;
import com.example.may.msocial.models.MsRow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ShowSelected_ArrayAdapter extends ArrayAdapter<MsRow>
{
    private LayoutInflater layoutInflater;

    public ShowSelected_ArrayAdapter(Context context, List<MsRow> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ShowSelected_Holder holder = null;
        if (convertView == null) {
            holder = new ShowSelected_Holder();

            convertView = layoutInflater.inflate(R.layout.list_symptoms_selected_row, null);
            holder.setTextViewTitle((TextView) convertView.findViewById(R.id.itemTitle));
            convertView.setTag(holder);
        } else {
            holder = (ShowSelected_Holder) convertView.getTag();
        }

        final MsRow row = getItem(position);
        holder.getTextViewTitle().setText(row.getTitle());
        holder.getTextViewTitle().setTextSize(18);
        final View fila = convertView;
        changeBackground(getContext(), fila, row.isChecked());

        return convertView;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void changeBackground(Context context, View row, boolean checked)
    {
        if (row != null)
        {
            Drawable drawable = context.getResources().getDrawable(R.drawable.listview_selector_checked);
            if (checked)
            {
                drawable = context.getResources().getDrawable(R.drawable.listview_selector_checked);
            }
            else
            {
                drawable = context.getResources().getDrawable(R.drawable.selected_symptom_listview);
            }
            int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                row.setBackgroundDrawable(drawable);
            } else {
                row.setBackground(drawable);
            }
        }
    }

}

class ShowSelected_Holder
{


    TextView textViewTitle;
    TextView textViewSubtitle;


    public TextView getTextViewTitle()
    {
        return textViewTitle;
    }

    public void setTextViewTitle(TextView textViewTitle)
    {
        this.textViewTitle = textViewTitle;
    }

    public TextView getTextViewSubtitle()
    {
        return textViewSubtitle;
    }

    public void setTextViewSubtitle(TextView textViewSubtitle)
    {
        this.textViewSubtitle = textViewSubtitle;
    }


}

