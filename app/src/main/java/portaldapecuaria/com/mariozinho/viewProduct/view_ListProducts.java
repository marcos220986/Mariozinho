package portaldapecuaria.com.mariozinho.viewProduct;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import portaldapecuaria.com.mariozinho.MainActivity;
import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.databinding.ViewProductsListBinding;
import portaldapecuaria.com.mariozinho.model.Command;
import portaldapecuaria.com.mariozinho.model.Product;
import portaldapecuaria.com.mariozinho.viewComand.view_ListCommands;
import portaldapecuaria.com.mariozinho.view_adapter.ProductAdapter;


public class view_ListProducts extends AppCompatActivity {

    //Biding
    private ViewProductsListBinding binding;

    //Componetes em Tela
    private List<Command> listCommands = new ArrayList<Command>();
    private EditText editSeachProduct, input_nameProduct;
    private TextView input_barCode;
    private ListView listV_pesquisa_Product;
    private String seachWord = "";
    private double grandTotal;
    //Dados de Pedisos
    int totalRequest = 0;
    String sendNoteRequest = "*Lista de Pedidos:* " + ".\n \n \n";

    //Conexão com Banco de Dados
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //Lista de Comandas
    private List<Product> listProduct = new ArrayList<Product>();
    private ProductAdapter productAdapter;
    private ArrayAdapter<Product> arrayAdapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewProductsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startComponent();
        setTitle("Produtos");
    }

    //Trabalhando Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemProductSeach:
                final EditText inputSeach = new EditText(view_ListProducts.this);
                inputSeach.setInputType(InputType.TYPE_CLASS_TEXT);
                new AlertDialog.Builder(view_ListProducts.this)
                        .setIcon(R.drawable.ic_baseline_search_24)
                        .setTitle("Pesquisando Produto..:")
                        .setMessage("Digite sua pesquisa.:")
                        .setView(inputSeach)
                        .setPositiveButton("Pesquisar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //False para 'Recebidas'
                                if (inputSeach.getText().toString().trim().length() > 0) {
                                    seachWord = inputSeach.getText().toString();
                                    gerListProducts();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                break;
            case R.id.itemProductdNew: //Criando novo Produto
                startActivity(new Intent(this, view_creatProducts.class));
                break;

            case R.id.itemProductListSend:
                String sendNote = "*Mensagem Automática:*\n *Lista de Produtos:* " + ".\n \n \n";
                for (int i = 0; i < listProduct.size(); i++) {
                    sendNote += listProduct.get(i).getName() + "\n";
                }
                sendNote += "\n *Total de Produtos:* " + listProduct.size();
                sendLink(sendNote);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void sendLink(String sendNote) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendNote.toString());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void startComponent() {
        listV_pesquisa_Product = binding.listVSeachProduct; //findViewById(R.id.listV_seach_product);

    }

    @Override
    protected void onResume() {
        super.onResume();
        grandTotal = 0.00;
        gerListCommands();
        gerListProducts();
    }

    private void gerListCommands() {
        listCommands.clear();
        new ConnectDB().ordeByName(view_ListProducts.this, "Command").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Command c = objSnapshot.getValue(Command.class);
                    listCommands.add(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gerListProducts() {
        listProduct.clear();
        new ConnectDB().ordeByName(view_ListProducts.this, "Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Product p = objSnapshot.getValue(Product.class);
                    p.setTotalOutput(0);
                    int total = 0;
                    /*
                    for (int i1 = 0; i1 < listCommands.size(); i1++) {
                        for (int i2 = 0; i2 < listCommands.get(i1).getListProducts().size(); i2++) {
                            if (p.getUid().equals(listCommands.get(i1).getListProducts().get(i2).getUidProduct())) {
                                total += listCommands.get(i1).getListProducts().get(i2).getProduct_quantity();
                            }
                        }
                    } */
                    if (total > 0) {
                        p.setTotalOutput(total);
                        new ConnectDB().updateItem(view_ListProducts.this, "Product", p.getUid()).setValue(p);
                    }


                    //grandTotal += p.valueQuantity();
                    if (seachWord != "") {
                        if (p.getName().toLowerCase().contains(seachWord.toLowerCase())) {
                            listProduct.add(p);
                        }
                    } else {
                        listProduct.add(p);
                    }
                }

                productAdapter = new ProductAdapter(getBaseContext(), R.layout.item_model_product, listProduct);
                listV_pesquisa_Product.setAdapter(productAdapter);
                listV_pesquisa_Product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Product chosen_Product = listProduct.get(i);
                        startActivity(new Intent(view_ListProducts.this, view_creatProducts.class).putExtra("PRODUCT", chosen_Product));
                    }
                });
                listV_pesquisa_Product.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Product chosen_Product = listProduct.get(i);
                        new AlertDialog.Builder(view_ListProducts.this)
                                .setTitle("Deseja excluir este produto?")
                                .setMessage(chosen_Product.getName())
                                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new ConnectDB().ConnectDB(view_ListProducts.this).child("Product").child(chosen_Product.getUid()).removeValue();
                                        Toast.makeText(view_ListProducts.this, "O produto " + chosen_Product.getUid() + " foi EXCLUIDO com sucesso", Toast.LENGTH_SHORT).show();
                                        finish(); //Fechando Tela Atual
                                        startActivity(new Intent(view_ListProducts.this, view_ListProducts.class));
                                    }
                                }).setNegativeButton(R.string.No , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() { //Retornar guia anterior com Botão Padrão do Android
        super.onBackPressed();
        finish();
        Intent start_view_createProduct = new Intent(this, MainActivity.class);
        startActivity(start_view_createProduct);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //Trabalhando Code barras
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                ms_LONG("Leitura cancelada.");
            } else {
                input_barCode.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //Mensagems Toast

    public void ms_LONG(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

}