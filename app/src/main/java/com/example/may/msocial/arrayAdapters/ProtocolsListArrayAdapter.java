package com.example.may.msocial.arrayAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.may.msocial.R;
import com.example.may.msocial.models.Protocol;

import java.util.List;


public class ProtocolsListArrayAdapter extends ArrayAdapter<Protocol> {

    private LayoutInflater layoutInflater;

    public ProtocolsListArrayAdapter(Context context, List<Protocol> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ProtocolsHolder holder = null;
        if (convertView == null)
        {
            holder = new ProtocolsHolder();
            convertView = layoutInflater.inflate(R.layout.protocol_list_row, null);
            holder.setTextViewDescription((TextView) convertView.findViewById(R.id.problemNameRow));
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBoxProtocolList));
            convertView.setTag(holder);
        }
        else
        {
            holder = (ProtocolsHolder) convertView.getTag();
        }

        final Protocol row = getItem(position);

        holder.getTextViewDescription().setText(row.getDescription());
        holder.getTextViewDescription().setTextSize(18);
        holder.getCheckBox().setEnabled(true);
        holder.getCheckBox().setTag(row.getDescription());
        holder.getCheckBox().setChecked(row.isChecked());
        final View fila= convertView;
        changeBackground(getContext(), fila, row.isChecked());
        final ListView listView = (ListView)parent;

        holder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked)
            {
                if (row.getDescription().equals(view.getTag().toString()))
                {

                    changeBackground(ProtocolsListArrayAdapter.this.getContext(), fila, isChecked);
                    notifyDataSetChanged();

                }
            }
        });


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
                drawable = context.getResources().getDrawable(R.drawable.listview_selector);
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

class ProtocolsHolder {

    TextView description;
    CheckBox checkBox;

    public TextView getTextViewDescription(){
        return description;
    }


    public void setTextViewDescription(TextView description)
    {
        this.description = description;
    }
    public CheckBox getCheckBox()
    {
        return checkBox;
    }
    public void setCheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }

}
