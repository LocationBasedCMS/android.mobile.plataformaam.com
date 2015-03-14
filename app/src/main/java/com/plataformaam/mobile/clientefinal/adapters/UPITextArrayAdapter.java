package com.plataformaam.mobile.clientefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bernard on 18/01/2015.
 */
public class UPITextArrayAdapter extends ArrayAdapter<UPI> {
    private final Context context;
    private final List<UPI> values;
    private final int rowLayoutID;

    public UPITextArrayAdapter(Context context, List<UPI> objects) {
        super(context, 0, objects);
        this.context = context;
        this.values =objects;
        this.rowLayoutID =0;
    }


    public UPITextArrayAdapter(Context context, int rowLayoutID, List<UPI> objects) {
        super(context, rowLayoutID, objects);
        this.context = context;
        this.values =objects;
        this.rowLayoutID = rowLayoutID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //OBTEM O TEMPLATE DA LINHA
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.rowLayoutID, parent, false);

        //OBTEM OS CAMPOS DO TEMPLATE
        TextView title = (TextView) rowView.findViewById(R.id.txtRowUPITextTitle);
        TextView content = (TextView) rowView.findViewById(R.id.txtRowUPITextContent);

        //ASSOCIA OS CAMPOS DA UPI AOS CAMPOS DO LAYOUT
        UPI upi = values.get(position);
        title.setText(upi.getTitle());
        content.setText(upi.getContent());

        return rowView;
    }

    public List<UPI> getData(){
        return values;
    }

    @Override
    public int getCount() {
        if( values != null ) {
            return values.size();
        }else{
            return 0;
        }
    }
}
