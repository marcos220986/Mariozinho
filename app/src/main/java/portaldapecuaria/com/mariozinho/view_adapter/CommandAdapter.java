package portaldapecuaria.com.mariozinho.view_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.model.Command;

public class CommandAdapter extends ArrayAdapter<Command> {
    private List<Command> items;

    public CommandAdapter(Context context, int textViewResourceID, List<Command> items){
        super(context, textViewResourceID, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            Context ctx = getContext();
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_model_command, null);
        }
        Command command = items.get(position);
        if(command != null){
            ((TextView) v.findViewById(R.id.lblCommand)).setText(command.getName());
            if(command.getSituation().equals("#")){
                ((TextView) v.findViewById(R.id.lblCommandSituation)).setText("Recebido");
            }
            ((TextView) v.findViewById(R.id.lblDateCreate)).setText(command.dateCreate());
            ((TextView) v.findViewById(R.id.lblTotalValue)).setText("R$ " + new DecimalFormat("#,##0.00").format(command.totalCommandValue()));
        }
        return v;
    }
}
