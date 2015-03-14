package com.plataformaam.mobile.clientefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plataformaam.mobile.clientefinal.R;

import com.plataformaam.mobile.clientefinal.models.vcloc.rules.UPIAggregationRuleStart;


import java.util.List;

/**
 * Created by bernard on 06/03/2015.
 */
public class PublishRuleDialogArrayAdapter  extends ArrayAdapter<UPIAggregationRuleStart> {

    private final Context context;
    private final List<UPIAggregationRuleStart> values;
    private final int rowLayoutID;
    private final long rowOdd = 0xDDAAAAFF;
    private final long rowEven = 0xDDAAFFAA;



    public PublishRuleDialogArrayAdapter(Context context, List<UPIAggregationRuleStart> objects) {
        super(context, 0, objects);
        this.context = context;
        this.values =objects;
        this.rowLayoutID =0;
    }


    public PublishRuleDialogArrayAdapter(Context context, int rowLayoutID, List<UPIAggregationRuleStart> objects) {
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
        TextView title = (TextView) rowView.findViewById(R.id.dialogRowTxtTitle);
        TextView content = (TextView) rowView.findViewById(R.id.dialogRowTxtDescription);

        //ASSOCIA OS CAMPOS DA UPI AOS CAMPOS DO LAYOUT
        UPIAggregationRuleStart publicationRule = values.get(position);
        title.setText(publicationRule.getName()  );
        content.setText(publicationRule.getDescription());

        if(position %2 == 0 ) {
            rowView.setBackgroundColor( rowView.getResources().getColor(R.color.pam_bg_upi_list_row_odd) );
        }else{
            rowView.setBackgroundColor( rowView.getResources().getColor(R.color.pam_bg_upi_list_row_even) );
        }
        return rowView;
    }

    public List<UPIAggregationRuleStart> getData(){
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
