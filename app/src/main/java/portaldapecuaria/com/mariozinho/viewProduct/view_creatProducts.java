package portaldapecuaria.com.mariozinho.viewProduct;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.databinding.ViewCreatNewProductBinding;
import portaldapecuaria.com.mariozinho.model.Product;

public class view_creatProducts extends AppCompatActivity {

    //Dados de Banco de Dados
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private  ViewCreatNewProductBinding binding;


    //Classe Produto
    private Product p;
    private String uid_edit_product = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_creat_new_product);
        binding = ViewCreatNewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Iniciando Banco de Dados
        startFirebase();

        //Pesquisando dados
        Intent intent = getIntent();


        if (intent.hasExtra("PRODUCT")) {
            p = (Product) intent.getExtras().getSerializable("PRODUCT");//Recuperando produto selecionado
            uid_edit_product =p.getUid();
            setTitle("Editando: " + p.getName());
            binding.editProductName.setText(p.getName());
            binding.editProductName.setText(p.getName());
            binding.editSupplierValue.setText(String.valueOf(new BigDecimal(p.getSupplierValue()).setScale(2, RoundingMode.HALF_EVEN)));
            binding.editSaleValue.setText(String.valueOf(new BigDecimal(p.getSaleValue()).setScale(2, RoundingMode.HALF_EVEN)));
        } else {
            setTitle("Criando novo Produto");
        }

        //Calculando margem de lucro
        binding.btnToCalcProduct.setOnClickListener(view -> {
            if(binding.tvProfitMargin.getText().length() > 0){
                float percentage = Float.parseFloat(binding.editProfitMargin.getText().toString());
                float value_product = Float.parseFloat(binding.editSupplierValue.getText().toString());
                float value_total = value_product + (value_product * percentage) / 100;
                binding.tvProfitMargin.setText(value_total + "");
            }
        });

    }


    public void saveProduct(View v) {
        if (uid_edit_product != "") {
            p.updateDB(binding.editProductName.getText().toString(),
                    Double.parseDouble(binding.editSupplierValue.getText().toString()),
                    Double.parseDouble(binding.editSaleValue.getText().toString()));

            new AlertDialog.Builder(view_creatProducts.this)
                    .setTitle("Salvar")
                    .setMessage("Deseja salvar as alterações de: " + p.getName() + " por R$ ")
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("Product").child(p.getUid()).setValue(p);
                            Toast.makeText(view_creatProducts.this, "O produto " + p.getName() + " foi atualizado com sucesso", Toast.LENGTH_SHORT).show();
                            finish(); //Fechando Tela Atual
                            startActivity(new Intent(view_creatProducts.this, view_ListProducts.class));
                        }
                    })
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        } else {
            Product p = new Product();
            p.insertDB(binding.editProductName.getText().toString(),
                    Double.parseDouble(binding.editSupplierValue.getText().toString()),
                    Double.parseDouble(binding.editSaleValue.getText().toString()));

            new AlertDialog.Builder(view_creatProducts.this)
                    .setTitle("Salvar")
                    .setMessage("Deseja salvar: " + p.getName() + " por R$" + p.getSaleValue())
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("Product").child(p.getUid()).setValue(p);

                            Toast.makeText(view_creatProducts.this, "O produto " + p.getName() + " foi salva com sucesso", Toast.LENGTH_SHORT).show();
                            finish(); //Fechando Tela Atual
                            startActivity(new Intent(view_creatProducts.this, view_ListProducts.class));
                        }
                    })
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //não exclui, apenas fecha a mensagem
                            dialog.dismiss();
                        }
                    })
                    .show();
        }


    }

    /* public void deleteProduct(View v) {
        if (uid_edit_product == "") {
            name_product.setText("");
            supplierValue_product.setText("");
            saleValue_product.setText("");
            provider_whatsApp_product.setText("");
        } else {
            new AlertDialog.Builder(view_creatProducts.this)
                    .setTitle("Excluindo Item")
                    .setMessage("Deseja realmente excluir " + p.getName().toUpperCase() + "?")
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("Product").child(uid_edit_product).removeValue();
                            Toast.makeText(view_creatProducts.this, "O produto " + p.getName() + " foi EXCLUIDO com sucesso", Toast.LENGTH_SHORT).show();
                            Intent start_view_ListCommands = new Intent(view_creatProducts.this, view_ListProducts.class);
                            startActivity(start_view_ListCommands);
                        }
                    })
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //não exclui, apenas fecha a mensagem
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    } */

    //Retornando tela anterior com botão Padrão do Android
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); //Fechando Tela Atual
        Intent start_view_ListCommands = new Intent(this, view_ListProducts.class);
        startActivity(start_view_ListCommands);
    }

    //Iniciando Banco de dados
    private void startFirebase() {
        FirebaseApp.initializeApp(view_creatProducts.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}