package com.example.may.msocial.arrayAdapters;

        import java.io.InputStream;
        import java.util.List;

        import com.example.may.msocial.R;
        import com.example.may.msocial.models.MsRow;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.CompoundButton.OnCheckedChangeListener;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;


public class SymptomsListArrayAdapter extends ArrayAdapter<MsRow>
{
    private LayoutInflater layoutInflater;

    public SymptomsListArrayAdapter(Context context, List<MsRow> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            holder = new Holder();

            convertView = layoutInflater.inflate(R.layout.listview_row, null);
            holder.setImageView((ImageView)convertView.findViewById(R.id.symptomimg));
            holder.setTextViewTitle((TextView) convertView.findViewById(R.id.itemTitle));
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBoxList));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        final MsRow row = getItem(position);
        new DownloadImageTask(holder.getImageView()).execute(row.getUrl());

        holder.getTextViewTitle().setText(row.getTitle());
        holder.getTextViewTitle().setTextSize(18);
        holder.getCheckBox().setEnabled(true);
        holder.getCheckBox().setTag(row.getTitle());
        holder.getCheckBox().setChecked(row.isChecked());
        final View fila= convertView;
        changeBackground(getContext(), fila, row.isChecked());


        holder.getCheckBox().setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked)
            {


                if (row.getTitle().equals(view.getTag().toString()))
                {
                    changeBackground( SymptomsListArrayAdapter.this.getContext(), fila, isChecked);
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

class Holder
{
    ImageView imageView;
    String url;
    TextView textViewTitle;
    TextView textViewSubtitle;
    CheckBox checkBox;

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
    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }
    public ImageView getImageView (){ return imageView;}
    public CheckBox getCheckBox()
    {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }




}



class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}