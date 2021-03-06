package com.plataformaam.mobile.clientefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.models.vcloc.VComComposite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bernard on 31/01/2015.
 */

public class VComCompositeArrayAdapter  extends ArrayAdapter<VComComposite> {
    private final Context context;
    private final List<VComComposite> values;
    private final int rowLayoutID;
    private Map<Integer,VComComposite> myComposites;

    public VComCompositeArrayAdapter(Context context,int rowLayoutID, List<VComComposite> composites,Map<Integer,VComComposite> myComposites){
        super(context,rowLayoutID,composites);
        this.context = context;
        this.values = composites;
        this.rowLayoutID = rowLayoutID;
        this.myComposites=myComposites;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //OBTEM O TEMPLATE DA LINHA
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.rowLayoutID, parent, false);

        TextView title = (TextView) rowView.findViewById(R.id.txtRowVComCompositeName);
        TextView content = (TextView) rowView.findViewById(R.id.txtRowVComCompositeDescription);

        VComComposite composite = values.get(position);
        title.setText(composite.getName());
        content.setText(composite.getDescription());

        if( myComposites.containsKey(composite.getId())){
            rowView.setBackgroundColor( 0xCCBBFFFF );
        } else {
            rowView.setBackgroundColor( 0xCCFFBBBB );
        }

        return rowView;
    }


    public int getCount(){
        return values.size();
    }

    public List<VComComposite> getData(){
        return this.values;
    }
}
