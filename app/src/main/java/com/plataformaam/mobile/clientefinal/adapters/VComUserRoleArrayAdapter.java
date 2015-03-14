package com.plataformaam.mobile.clientefinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plataformaam.mobile.clientefinal.R;
import com.plataformaam.mobile.clientefinal.models.vcloc.roles.VComUserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by bernard on 09/03/2015.
 */
public class VComUserRoleArrayAdapter extends ArrayAdapter<VComUserRole> {
    private final Context context;
    private final List<VComUserRole> values;
    private final int rowLayoutID;


    public VComUserRoleArrayAdapter(Context context, int rowLayoutID, List<VComUserRole> roles) {
        super(context, rowLayoutID, roles);
        this.context = context;
        this.values = roles;
        this.rowLayoutID = rowLayoutID;


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //OBTEM O TEMPLATE DA LINHA
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(this.rowLayoutID, parent, false);

        //TODO - Corrigir os campos  --- apona para o roles 
        TextView title = (TextView) rowView.findViewById(R.id.txtRowVComUserRoleName);

        VComUserRole role = values.get(position);
        title.setText(role.getName());

        rowView.setBackgroundColor(rowView.getResources().getColor(R.color.pam_bg_vcom_list_row_signed));


        return rowView;
    }


    public int getCount() {
        if (values != null) {
            return values.size();
        } else {
            return 0;
        }
    }

    public List<VComUserRole> getData() {
        return this.values;
    }


}
