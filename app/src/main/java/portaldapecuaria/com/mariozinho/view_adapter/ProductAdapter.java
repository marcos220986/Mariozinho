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
import portaldapecuaria.com.mariozinho.model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    private List<Product> items;

    public ProductAdapter(Context context, int textViewResourceID, List<Product> items){
        super(context, textViewResourceID, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            Context ctx = getContext();
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_model_product, null);
        }
        Product product = items.get(position);
        if(product != null){
            ((TextView) v.findViewById(R.id.lblProduct)).setText(product.getName());
            /*
            if(product.getListEntreies().size() > 0){
                int qtd = product.getListEntreies().size();
                ((TextView) v.findViewById(R.id.lblShelflife)).setText("Validade: "+ product.getListEntreies().get(qtd-1).getValidade());
            } */
            ((TextView) v.findViewById(R.id.lblProductBarCode)).setText(product.getBarCode());
            ((TextView) v.findViewById(R.id.lblPurchasePrice)).setText("| " + new DecimalFormat("#,##0.00").format(product.getSupplierValue()) + " |");
            ((TextView) v.findViewById(R.id.lblSaleValue)).setText("| R$ " + new DecimalFormat("#,##0.00").format(product.getSaleValue()) + " |");
            //int total = product.totalEntriesStock() - product.getTotalOutput();
           // ((TextView) v.findViewById(R.id.lblStock)).setText("Estoque: " + total);
        }
       return v;
    }

}
