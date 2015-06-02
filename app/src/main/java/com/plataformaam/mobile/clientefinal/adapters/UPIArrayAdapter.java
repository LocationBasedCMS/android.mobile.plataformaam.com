package com.plataformaam.mobile.clientefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.configurations.MyAppConfig;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPI;

import java.util.List;

/**
 * Created by bernard on 18/01/2015.
 */
public class UPIArrayAdapter extends ArrayAdapter<UPI> {
    private final Context context;
    private final List<UPI> values;
    private final int rowTextLayoutID;
    private final int rowImageLayoutID;
    private static final int rowOdd = 0xAAAAFFFF;
    private static final int rowEven = 0xAADDFFAA;


    public UPIArrayAdapter(Context context, List<UPI> objects) {
        super(context, 0, objects);
        this.context = context;
        this.values =objects;
        this.rowTextLayoutID = 0;
        this.rowImageLayoutID = 0;
    }


    public UPIArrayAdapter(Context context, int rowTextLayoutID, int rowImageLayoutID, List<UPI> objects) {
        super(context, rowTextLayoutID, objects);
        this.context = context;
        this.values =objects;
        this.rowTextLayoutID = rowTextLayoutID;
        this.rowImageLayoutID = rowImageLayoutID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        //OBTEM OS CAMPOS DO TEMPLATE

        //ASSOCIA OS CAMPOS DA UPI AOS CAMPOS DO LAYOUT
        UPI upi = values.get(position);



        //OBTEM O TEMPLATE DA LINHA
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if( upi.getUpiType() != null ){
            if( upi.getUpiType().getId() == MyAppConfig.UpiType_Data_Code.UPI_IMAGE  ){
                rowView = generateImageRowView(parent, upi, inflater);
            }else{
                rowView = getTextRowView(parent, upi, inflater);
            }
        }else{
            rowView = getTextRowView(parent, upi, inflater);
        }
        if(position %2 == 0 ) {
            rowView.setBackgroundColor( rowView.getResources().getColor(R.color.pam_bg_upi_list_row_odd) );
        }else{
            rowView.setBackgroundColor( rowView.getResources().getColor(R.color.pam_bg_upi_list_row_even) );
        }
        return rowView;
    }

    private View getTextRowView(ViewGroup parent, UPI upi, LayoutInflater inflater) {
        View rowView;
        rowView = inflater.inflate(this.rowTextLayoutID, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.txtRowUPITitle);
        TextView content = (TextView) rowView.findViewById(R.id.txtRowUPITextContent);
        title.setText(upi.getTitle());
        content.setText(upi.getContent());
        return rowView;
    }

    private View generateImageRowView(ViewGroup parent, UPI upi, LayoutInflater inflater) {
        View rowView;
        rowView = inflater.inflate(this.rowImageLayoutID, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.txtRowUPITitle);
        title.setText(upi.getTitle());
        ImageView imgContent = (ImageView) rowView.findViewById(R.id.imgRowUpiContent);
        imgContent.setImageResource(R.drawable.ic_image_row_list);
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
