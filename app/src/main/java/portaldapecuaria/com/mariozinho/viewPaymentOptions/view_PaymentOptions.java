package portaldapecuaria.com.mariozinho.viewPaymentOptions;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import portaldapecuaria.com.mariozinho.MainActivity;
import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.model.PaymentOptions;

public class view_PaymentOptions extends AppCompatActivity {

    //Campos formumlarios
    String uidPO = "";
    EditText namePO; //paymentoptions nome
    EditText ratePO; //Taxa de pagamento
    LinearLayout editing_rate;
    PaymentOptions chose_PaymentOptions;

    //Botões
    Button savePO, deletePO;
    TextView tv_newPO;
    //Lista de Formas de Pagamento
    private ListView listV_po; //Lista de Formas de Pagamento
    private List<PaymentOptions> listPO = new ArrayList<PaymentOptions>();
    private ArrayAdapter<PaymentOptions> arrayAdapterPO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profit_margin_list);

        //Obtendo Campos
        namePO = (EditText) findViewById(R.id.edit_PaymentOptions_name);
        ratePO = (EditText) findViewById(R.id.edit_PaymentOptions_rate);
        tv_newPO = (TextView) findViewById(R.id.tv_newPaimentOptions);
        listV_po = findViewById(R.id.listView_po);//po é igual PaymentOptions
        editing_rate = (LinearLayout) findViewById(R.id.editing_rate);
        //Preenchendo Lista
        gerListPO();

        Intent datePO = getIntent();
        if (datePO.hasExtra("uidPO")) {
            uidPO = datePO.getStringExtra("uidPO");
            Query query;
            query = new ConnectDB().ConnectDB(view_PaymentOptions.this).child("PaymentOptions").orderByChild("uid").equalTo(uidPO).limitToFirst(1);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                        PaymentOptions po = objSnapshot.getValue(PaymentOptions.class);
                        setTitle("Editando Opções de Pagamento");
                        tv_newPO.setText(po.getName());
                        namePO.setText(po.getName().toString());
                        ratePO.setText(String.valueOf(new BigDecimal(po.getRatePayment()).setScale(2, RoundingMode.HALF_EVEN)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            setTitle("Criando");
            tv_newPO.setText("Nova Forma de Pagamento");
        }
    }

    //Gerando lista
    public void gerListPO() {
        listPO.clear();
        new ConnectDB().ordeByName(view_PaymentOptions.this, "PaymentOptions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    PaymentOptions po = objSnapshot.getValue(PaymentOptions.class);
                    listPO.add(po);
                }
                arrayAdapterPO = new ArrayAdapter<PaymentOptions>(view_PaymentOptions.this, android.R.layout.simple_list_item_1, listPO);
                listV_po.setAdapter(arrayAdapterPO);
                listV_po.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        chose_PaymentOptions = listPO.get(i);
                        setTitle("Editando Opções de Pagamento");
                        ((LinearLayout) findViewById(R.id.editing_rate)).setVisibility(View.VISIBLE);
                        tv_newPO.setText(chose_PaymentOptions.getUid());
                        uidPO = chose_PaymentOptions.getUid().toString();
                        namePO.setText(chose_PaymentOptions.getName().toString());
                        ratePO.setText(String.valueOf(new BigDecimal(chose_PaymentOptions.getRatePayment()).setScale(2, RoundingMode.HALF_EVEN)));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void savePO(View v) {
        if (uidPO != "" && namePO.getText().toString() != "" && ratePO.getText().toString() != "") {
            chose_PaymentOptions.setUid(uidPO);
            chose_PaymentOptions.updateDB(namePO.getText().toString(), Double.parseDouble(ratePO.getText().toString()));
            //Alertando
            new AlertDialog.Builder(view_PaymentOptions.this)
                    .setTitle("Salvar").setMessage("Deseja salvar essa Alteração na forma de pagamento: ")
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            listPO.clear();
                            new ConnectDB().updateItem(view_PaymentOptions.this, "PaymentOptions", chose_PaymentOptions.getUid()).setValue(chose_PaymentOptions);
                            Toast.makeText(view_PaymentOptions.this, "Atualizado com sucesso.", Toast.LENGTH_LONG).show();
                            cleanForm();//Limpando Formulario
                            dialog.dismiss();
                        }
                    }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else if (namePO.getText().toString() != "" && ratePO.getText().toString() != "") {
            PaymentOptions po = new PaymentOptions();
            po.insertDB(namePO.getText().toString(), Double.parseDouble(ratePO.getText().toString()));
            //Alertando
            new AlertDialog.Builder(view_PaymentOptions.this)
                    .setTitle("Salvar").setMessage("Deseja salvar essa forma de pagamento: " + po.getName())
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            listPO.clear();
                            new ConnectDB().insertDB(view_PaymentOptions.this, "PaymentOptions", po.getUid()).setValue(po);
                            Toast.makeText(view_PaymentOptions.this, "Criado com sucesso.", Toast.LENGTH_LONG).show();
                            cleanForm();//Limpando Formulario
                            dialog.dismiss();
                        }
                    }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
           }).show();
        }
    }

    public void deletPO(View v) {
        if (uidPO == "") {
            Toast.makeText(view_PaymentOptions.this, "Limpando Formulario", Toast.LENGTH_SHORT).show();
            cleanForm();//Limpando Formulario
        } else {
            new AlertDialog.Builder(view_PaymentOptions.this)
                    .setTitle("Excluindo Opção de Pagamento")
                    .setMessage("Deseja realmente excluir a opção" + chose_PaymentOptions.getName() + "?")
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            listPO.clear();
                            new ConnectDB().removeItem(view_PaymentOptions.this, "PaymentOptions", chose_PaymentOptions.getUid());
                            //new ConnectDB().ConnectDB(view_PaymentOptions.this).child("PaymentOptions").child(chose_PaymentOptions.getUid()).removeValue();
                            Toast.makeText(view_PaymentOptions.this, "Limpando Formulario", Toast.LENGTH_SHORT).show();
                            chose_PaymentOptions.setUid("");
                            cleanForm();//Limpando Formulario
                            Toast.makeText(view_PaymentOptions.this, "A opção de pagamento " + chose_PaymentOptions.getName() + " foi EXCLUIDO com sucesso", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent cancelReturn = new Intent(this, MainActivity.class);
        startActivity(cancelReturn);
    }
    public void cleanForm(){ //Limpando Formulario
        namePO.setText("");
        ratePO.setText("");
    }
}