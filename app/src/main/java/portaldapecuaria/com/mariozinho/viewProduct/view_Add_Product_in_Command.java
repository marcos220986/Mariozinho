package portaldapecuaria.com.mariozinho.viewProduct;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.model.CheckNotification;
import portaldapecuaria.com.mariozinho.model.Command;
import portaldapecuaria.com.mariozinho.model.CommandProduct;
import portaldapecuaria.com.mariozinho.model.Product;
import portaldapecuaria.com.mariozinho.viewComand.view_Command_ListProduct;
import portaldapecuaria.com.mariozinho.viewComand.view_ListCommands;
import portaldapecuaria.com.mariozinho.view_adapter.ProductAdapter;

public class view_Add_Product_in_Command extends AppCompatActivity {

    //Componentes de Tela
    Command c;
    CheckNotification checkNotification = new CheckNotification();
    private TextView tv_commandUid_in_add_product_in_command;//Id da Commanda
    private ListView listV_seach_product;
    private ProductAdapter productAdapter;
    String command_uid;
    String seachWord = "";
    EditText input_text;

    //Lista de Produtos
    private List<Product> listProducts = new ArrayList<Product>();
    private ArrayAdapter<Product> arrayAdapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_add_product_in_command_item_new);

        starComponentes();

        Intent intent = getIntent();
        c = (Command) intent.getExtras().getSerializable("COMMAND");
        //command_uid = uid_command.getStringExtra("command_Uid");
        setTitle("Add..: " + c.getName());

    }

    public void starComponentes() {
        listV_seach_product = findViewById(R.id.listV_seach_product);
    }

    //Trabalhando Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        ((MenuItem) menu.findItem(R.id.itemProductdNew)).setVisible(false);
        //((MenuItem) menu.findItem(R.id.itemProductStoque)).setVisible(false);
        ((MenuItem) menu.findItem(R.id.itemProductListSend)).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemProductSeach:
                LayoutInflater li = getLayoutInflater();
                View vi = li.inflate(R.layout.dialog_input_text, null);
                input_text = vi.findViewById(R.id.etInput_Text);
                //final EditText inputSeach = new EditText(view_Add_Product_in_Command.this);
                //inputSeach.setInputType(InputType.TYPE_CLASS_TEXT);
                new AlertDialog.Builder(view_Add_Product_in_Command.this)
                        .setIcon(R.drawable.ic_baseline_search_24)
                        .setTitle("Pesquisando Produto..:")
                        .setMessage("Digite sua pesquisa.:")
                        .setView(vi)
                        .setPositiveButton("Pesquisar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //False para 'Recebidas'
                                if (input_text.getText().toString().trim().length() > 0) {
                                    seachWord = input_text.getText().toString();
                                    gerList();
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
            case R.id.itemCPBarCode:
                IntentIntegrator integrator = new IntentIntegrator(view_Add_Product_in_Command.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Leitor de Barras");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Trabalhando Code barras
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                ms_LONG("Leitura cancelada.");
            }else {
                boolean itemAdd = true;
                for(int i = 0; i < listProducts.size(); i++){
                    if(listProducts.get(i).getBarCode().contains(result.getContents())){
                        addItem_inCommand(1, listProducts.get(i));
                        itemAdd = false;
                    }
                }
                if(itemAdd){ ms_LONG("Nenhum produto cadastrado." );}
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void ms_SHORT(String m){
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
    public void ms_LONG(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    private void gerList() {
        listProducts.clear();
        new ConnectDB().ordeByName(view_Add_Product_in_Command.this, "Product")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                            Product p = objSnapshot.getValue(Product.class);
                            if (seachWord != "") {
                                if (p.getName().toLowerCase().contains(seachWord.toLowerCase())) {
                                    listProducts.add(p);
                                }
                            } else {
                                listProducts.add(p);
                            }

                        }
                        productAdapter = new ProductAdapter(getBaseContext(), R.layout.item_model_product, listProducts);
                        //arrayAdapterProduct = new ArrayAdapter<Product>(view_Add_Product_in_Command.this, android.R.layout.simple_list_item_1, listProducts);
                        listV_seach_product.setAdapter(productAdapter);
                        makingClickable(); //Tornar lista Clicável
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void makingClickable() {
        listV_seach_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product chosem_Product = listProducts.get(i);
                LayoutInflater li = getLayoutInflater();
                View vi = li.inflate(R.layout.dialog_input_int, null);
                //final EditText input = new EditText(view_Add_Product_in_Command.this);
                //input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input_text = vi.findViewById(R.id.etInput_int);
                new AlertDialog.Builder(view_Add_Product_in_Command.this)
                        .setTitle(chosem_Product.getName())
                        .setMessage("Digite a quantidade abaixo:")
                        .setView(vi)
                        .setCancelable(false)
                        .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (input_text.getText().toString().trim().length() > 0) {
                                    int qtd = Integer.parseInt(input_text.getText().toString());
                                    addItem_inCommand(qtd, chosem_Product);
                                    seachWord = "";
                                    gerList();
                                    dialogInterface.dismiss();
                                } else {
                                    int qtd = 1;
                                    addItem_inCommand(qtd, chosem_Product);
                                    seachWord = "";
                                    gerList();
                                    dialogInterface.dismiss();
                                }
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

            }
        });
    }

    private void addItem_inCommand(int qtd, Product chosem_Product) {
        CommandProduct cp = new CommandProduct();
        cp.insert(c.getUid(), chosem_Product.getUid(), chosem_Product.getName(), chosem_Product.getSaleValue(), qtd);
        c.getListProducts().add(cp);
        Toast.makeText(view_Add_Product_in_Command.this, "Adicionado: "
                + cp.getProduct_quantity()
                + " de " + cp.getName()
                + " adicionado.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        gerList();
    }

    //Retornando tela anterior com Botão principal do Android
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); //Fechando Tela Atual
        returnSaved();
    }

    public void returnSaved() {
        Intent cancelReturn;
        if (c.getListProducts().isEmpty()) {
            Toast.makeText(getBaseContext(), "Comanda vazia. Nada foi salvo.", Toast.LENGTH_LONG).show();
            cancelReturn = new Intent(getBaseContext(), view_ListCommands.class);
        } else {
            new ConnectDB().updateItem(view_Add_Product_in_Command.this, "Command", c.getUid()).setValue(c);
            cancelReturn = new Intent(view_Add_Product_in_Command.this, view_Command_ListProduct.class);
            cancelReturn.putExtra("COMMAND", c);
            checkNotification.insert(c.getName(), "Atualizada, em: ", c.getUid());//Criando notificação
            new ConnectDB().insertDB(view_Add_Product_in_Command.this, "CheckNotification", checkNotification.getUid()).setValue(checkNotification);
        }
        startActivity(cancelReturn);
    }

    public void btnSaved(View v) {
        returnSaved();
    }

}