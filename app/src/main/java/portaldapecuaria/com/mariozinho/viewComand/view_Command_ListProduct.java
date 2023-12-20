package portaldapecuaria.com.mariozinho.viewComand;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.databinding.ViewCommandListProductsBinding;
import portaldapecuaria.com.mariozinho.databinding.ViewCreatNewProductBinding;
import portaldapecuaria.com.mariozinho.model.CheckNotification;
import portaldapecuaria.com.mariozinho.model.Command;
import portaldapecuaria.com.mariozinho.model.CommandProduct;
import portaldapecuaria.com.mariozinho.model.PaymentOptions;
import portaldapecuaria.com.mariozinho.viewProduct.view_Add_Product_in_Command;
import portaldapecuaria.com.mariozinho.view_adapter.CommandProductAdaper;

public class view_Command_ListProduct extends AppCompatActivity {

    private ViewCommandListProductsBinding binding;

    //Location location;

    private Command c;
    private CommandProduct cp;
    private PaymentOptions po;
    private CheckNotification checkNotification = new CheckNotification();
    private Double totalCPvalue = 0.00;
    private String optionsPyament = "| ";
    private CommandProductAdaper commandProductAdaper;
    double totalCommand = 0;


    //Lista de Comandas
    private List<CommandProduct> listProduct_in_command = new ArrayList<CommandProduct>();
    private List<PaymentOptions> listPaymentOptions = new ArrayList<>();
    //private ArrayAdapter<CommandProduct> arrayAdapterProduct_in_Command;
    //private FusedLocationProviderClient fusedLocationProviderClient; //Necessário para localiza

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewCommandListProductsBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.view_command_list_products);
        setContentView(binding.getRoot());
        //tv_commandUid = (TextView) findViewById(R.id.tv_Commands_in_product);
        //listV_product_in_command = (ListView) findViewById(R.id.listV_product_of_command);
        Intent intent = getIntent();
        c = (Command) intent.getExtras().getSerializable("COMMAND");
        completeArrayAdapterP_in_C();
        setTitle("Pedidos");
        String sendNote = "*Mensagem Automática:*\n *Lista de Pedidos:* " + ".\n \n \n";
        binding.tvCommandsInProduct.setText("Commanda de: " + c.getName());
        //tv_commandUid.setText();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.command_list_products_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCPAdd:
                finish();
                startActivity(new Intent(this, view_Add_Product_in_Command.class).putExtra("COMMAND", c));
                break;
            case R.id.itemCPAReceive:
                toReceive();//Recebendo
                break;
            case R.id.itemCPSend:
                openLink();//Enviando Lista
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Recebendo Comanda
    public void toReceive() {
        totalCommand = 0;
        for (int i = 0; i < c.getListProducts().size(); i++) {
            totalCommand += c.getListProducts().get(i).totalValue();
        }
        if (totalCommand > 0) {
            LayoutInflater li = getLayoutInflater();
            View vi = li.inflate(R.layout.dialog_input_float, null);
            EditText input_float = (EditText) vi.findViewById(R.id.etInput_float);
            new AlertDialog.Builder(view_Command_ListProduct.this)
                    .setTitle("Valor da Commanda: R$ " + new DecimalFormat("#,##0.00").format(totalCommand))
                    .setMessage("Para calcular troco. Digite valor recebido e aperte em TROCO. Ou em RECEBER para marcar como recebido.")
                    .setView(vi)
                    .setPositiveButton("Receber", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finalizaReceive();
                        }
                    }).setNegativeButton("Troco", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (input_float.getText().toString().length() > 0) {
                        double totalReceive = Double.parseDouble(input_float.getText().toString());
                        if (totalReceive > totalCommand) {
                            double valueThing = totalReceive - totalCommand;
                            new AlertDialog.Builder(view_Command_ListProduct.this)
                                    .setTitle("Troco: " + new DecimalFormat("#,##0.00").format(valueThing) + " de R$ " + new DecimalFormat("#,##0.00").format(totalReceive))
                                    .setMessage("Valor da Comanda R$ " + new DecimalFormat("#,##0.00").format(totalCommand) + ". Marcar como Recebido?")
                                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finalizaReceive();
                                        }
                                    }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                        } else {
                            msComandListProduct("Adicione um HAVER na comanda de R$ " + new DecimalFormat("#,##0.00").format(totalReceive) + " na comanda.");
                        }
                    }

                }
            }).show();
        }
    }

    private void finalizaReceive() {
        Date date = new Date();
        c.setSituation("#");
        c.setDate(date);
        new ConnectDB().updateItem(view_Command_ListProduct.this, "Command", c.getUid()).setValue(c);
        //Notificando
        checkNotification.insert(c.getName(), "COMANDA recebida em: ", c.getUid());
        new ConnectDB().insertDB(view_Command_ListProduct.this, "CheckNotification", checkNotification.getUid()).setValue(checkNotification);
        onBackPressed();
    }


    public void gerListProducts() {
    }

    private void completeArrayAdapterP_in_C() {
        commandProductAdaper = new CommandProductAdaper(getBaseContext(), R.layout.item_model_command_product, c.getListProducts());
        //arrayAdapterProduct_in_Command = new ArrayAdapter<CommandProduct>(view_Command_ListProduct.this, android.R.layout.simple_list_item_1, listProduct_in_command);
        binding.listVProductOfCommand.setAdapter(commandProductAdaper);
        //Cique Curto
        binding.listVProductOfCommand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LayoutInflater li = getLayoutInflater();
                View vi = li.inflate(R.layout.dialog_input_int, null);
                EditText input_int = (EditText) vi.findViewById(R.id.etInput_int);
                new AlertDialog.Builder(view_Command_ListProduct.this)
                        .setTitle("Item: " + c.getListProducts().get(position).getName())
                        .setMessage("Quantidade atual: " + c.getListProducts().get(position).getProduct_quantity())
                        .setView(vi)
                        .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (input_int.getText().toString().trim().length() > 0) {
                                    c.getListProducts().get(position).setProduct_quantity(c.getListProducts().get(position).getProduct_quantity() + Integer.parseInt(input_int.getText().toString()));
                                    saveUpdate(c);
                                    msComandListProduct("Foi adicionado: +" + Integer.parseInt(input_int.getText().toString()) + " unidade(s)");
                                    dialogInterface.dismiss();
                                } else {
                                    c.getListProducts().get(position).setProduct_quantity(c.getListProducts().get(position).getProduct_quantity() + 1);
                                    saveUpdate(c);
                                    msComandListProduct("Foi adicionado: +" + 1 + " unidade(s)");
                                    dialogInterface.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Retirar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (input_int.getText().toString().trim().length() > 0) {
                                    int qtd_product = c.getListProducts().get(position).getProduct_quantity();
                                    if (qtd_product >= Integer.parseInt(input_int.getText().toString())) {
                                        qtd_product -= Integer.parseInt(input_int.getText().toString());
                                        c.getListProducts().get(position).setProduct_quantity(qtd_product);
                                        saveUpdate(c);//Salvando alterações
                                        dialogInterface.dismiss();
                                    } else {
                                        msComandListProduct("Quantidade atual não pode ser menor que '0' zero");
                                    }
                                } else {
                                    int qtd_product = c.getListProducts().get(position).getProduct_quantity();
                                    if (qtd_product >= 1) {
                                        qtd_product -= 1;
                                        c.getListProducts().get(position).setProduct_quantity(qtd_product);
                                        saveUpdate(c);//Salvando alterações
                                        dialogInterface.dismiss();
                                    } else {
                                        msComandListProduct("Quantidade atual não pode ser menor que '0' zero");
                                    }

                                }

                            }
                        }).show();
            }

        });
        //Clique Longo
        binding.listVProductOfCommand.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommandProduct chosen_product2 = c.getListProducts().get(i);
                new AlertDialog.Builder(view_Command_ListProduct.this).setTitle("Excuindo Item")
                        .setMessage("Deseja excluir: " + c.getListProducts().get(i).getName())
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                c.getListProducts().remove(chosen_product2);
                                if (c.getListProducts().isEmpty()) {
                                    new ConnectDB().removeItem(view_Command_ListProduct.this, "Command", c.getUid());
                                    Intent openAddItem = new Intent(getBaseContext(), view_Add_Product_in_Command.class);
                                    openAddItem.putExtra("COMMAND", c);
                                    startActivity(openAddItem);
                                } else {
                                    saveUpdate(c);
                                }
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                return true;
            }
        });
    }


    public void openLink() {
        LayoutInflater li = getLayoutInflater();
        View vi = li.inflate(R.layout.dialog_send_payment, null);
        Button btn_send_list = (Button) vi.findViewById(R.id.btn_send_list);
        Button btn_send_debit = (Button) vi.findViewById(R.id.btn_send_debit);
        btn_send_debit.setText(" Débito com 3%: R$" + new DecimalFormat("#,##0.00").format((c.totalCommandValue() + (c.totalCommandValue() * 0.03))));
        Button btn_send_credit = (Button) vi.findViewById(R.id.btn_send_credit);
        btn_send_credit.setText(" Crédito com 5%: R$" + new DecimalFormat("#,##0.00").format((c.totalCommandValue() + (c.totalCommandValue() * 0.05))));
        Button btn_send_other = (Button) vi.findViewById(R.id.btn_send_other);
        btn_send_other.setText(" Valor com 10%: R$" + new DecimalFormat("#,##0.00").format((c.totalCommandValue() + (c.totalCommandValue() * 0.1))));
        Button btn_send_0 = (Button) vi.findViewById(R.id.btn_send_0);
        btn_send_0.setText("Valor Avista: R$" + c.totalCommandValue());
        new AlertDialog.Builder(view_Command_ListProduct.this).setView(vi).show();



                        /*
                        for (int li = 0; li < c.getListProducts().size(); li++) {
                            sendNote += "```" + c.getListProducts().get(li).getName()
                                    + "```" + "\n" + "_" + c.getListProducts().get(li).Description()
                                    + "_ *" + c.getListProducts().get(li).totalValue_format() + "* \n \n";
                        }
                        sendNote = sendNote + "\n Valor *aproximado* na Data de de hoje: *"
                                + new DecimalFormat("#,##0.00").format(c.totalCommandValue()) + "* \n\n"
                                + "Nosso PIX *99 991633616* \n" + "```Em nome de: Jessiane Sousa```";

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, sendNote.toString());
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        */

    }

    //Retornando Guia anterior com botão de Retorno do Android
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent cancelReturn = new Intent(this, view_ListCommands.class);// Abrindo tela Inicial
        startActivity(cancelReturn);

    }

    public void gerListPaymentsOptions() {
        listPaymentOptions.clear();
        new ConnectDB().ordeByName(view_Command_ListProduct.this, "PaymentOptions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot obSnapshot : snapshot.getChildren()) {
                    po = obSnapshot.getValue(PaymentOptions.class);
                    listPaymentOptions.add(po);
                }
                ArrayAdapter<PaymentOptions> adapter = new ArrayAdapter<PaymentOptions>(view_Command_ListProduct.this, android.R.layout.simple_spinner_item, listPaymentOptions);
                final Spinner spinner = (Spinner) findViewById(R.id.spinner_payment_options);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        PaymentOptions chosen_po = listPaymentOptions.get(i);
                        msComandListProduct(chosen_po.getName().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void msComandListProduct(String ms) {//Para todas as mensagens nesta View
        Toast.makeText(view_Command_ListProduct.this, ms, Toast.LENGTH_LONG).show();
    }

    public void saveUpdate(Command c) {
        new ConnectDB().updateItem(view_Command_ListProduct.this, "Command", c.getUid()).setValue(c);//Salvando alterações
        commandProductAdaper.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gerListProducts();
    }


}