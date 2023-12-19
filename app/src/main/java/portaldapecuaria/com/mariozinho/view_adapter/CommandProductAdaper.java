package portaldapecuaria.com.mariozinho.view_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.model.CommandProduct;


public class CommandProductAdaper extends ArrayAdapter {
    private List<CommandProduct> items;

    public CommandProductAdaper(Context context, int textViewResourceId, List<CommandProduct> items){
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            Context ctx = getContext();
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_model_command_product, null);
        }
        CommandProduct commandProduct = items.get(position);
        if(commandProduct != null){
            ((TextView) v.findViewById(R.id.lblUserID)).setText("Inserido por: " + commandProduct.getUidUser());
            ((TextView) v.findViewById(R.id.lblProduct)).setText(commandProduct.getName());
            ((TextView) v.findViewById(R.id.lblValueTotal)).setText(commandProduct.totalValue_format());
            ((TextView) v.findViewById(R.id.lblDescription)).setText(commandProduct.Description());
        }
        return v;
    }
}
