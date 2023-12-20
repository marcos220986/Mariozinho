package portaldapecuaria.com.mariozinho.viewComand;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import portaldapecuaria.com.mariozinho.MainActivity;
import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.model.CheckNotification;
import portaldapecuaria.com.mariozinho.model.Command;
import portaldapecuaria.com.mariozinho.viewProduct.view_Add_Product_in_Command;
import portaldapecuaria.com.mariozinho.view_adapter.CommandAdapter;

public class view_ListCommands extends AppCompatActivity {

    //Componetes em Tela
    private ListView listV_pesquisa_command;
    EditText input_text;
    private String seachWord = "";
    private Boolean seachWord_ = true;//True para comanda 'Á Receber' e false para 'Recebidas'
    private double grandTotal;

    //Conexão com Banco de Dados
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //Lista de Comandas
    private List<Command> listCommands = new ArrayList<Command>();
    private List<Command> listCommands2 = new ArrayList<Command>();
    private ArrayAdapter<Command> arrayAdapterCommand;
    CommandAdapter commandAdapter;
    CheckNotification checkNotification = new CheckNotification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commands_viewlist);//R.layout.activity_view_commands
        startComponents();
        setTitle("Comandas");
    }

    //Inicio Geração de Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.command_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Cliques de Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        double totalMoney = 0.00;//Soma total geral de Commandas
        switch (item.getItemId()) {
            case R.id.itemCommandSeach:
                final EditText inputSeach = new EditText(view_ListCommands.this);
                inputSeach.setInputType(InputType.TYPE_CLASS_TEXT);
                new AlertDialog.Builder(view_ListCommands.this)
                        .setIcon(R.drawable.ic_baseline_search_24)
                        .setTitle("Em qual lista deseja pesquisar?")
                        .setMessage("Digite sua pesquisa.:")
                        .setView(inputSeach)
                        .setPositiveButton("|RECEBIDAS |", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //False para 'Recebidas'
                                    seachWord_ = false;
                                    seachWord = inputSeach.getText().toString().toLowerCase();
                                    msDialog("Pesquisando... " + seachWord + " em : RECEBIDAS.");
                                    gerList();

                            }
                        })
                        .setNegativeButton("|À RECEBER |", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //True para comanda 'Á Receber'
                                    seachWord = inputSeach.getText().toString().toLowerCase();
                                    seachWord_ = true;
                                    msDialog("Pesquisando... " + seachWord + " em : À RECEBER.");
                                    gerList();
                            }
                        }).show();

                break;
            case R.id.itemCommandNew:
                LayoutInflater li = getLayoutInflater();
                View vi = li.inflate(R.layout.dialog_input_text, null);
                new AlertDialog.Builder(view_ListCommands.this)
                        .setTitle("Criar Nova Comanda?")
                        .setMessage("Digite nome da COMANDA abaixo.:")
                        .setView(vi)
                        .setPositiveButton("Criar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                input_text = (EditText) vi.findViewById(R.id.etInput_Text);
                                if(input_text.getText().toString().trim().length() > 3){
                                    Command c = new Command();
                                    c.insertCommand(input_text.getText().toString());
                                    Intent open_ListProducts_in_Command = new Intent(getBaseContext(), view_Add_Product_in_Command.class);
                                    open_ListProducts_in_Command.putExtra("COMMAND", c);
                                    finish(); //Fechando Tela Atual
                                    startActivity(open_ListProducts_in_Command);
                                }else {
                                    msDialog("Nome inválido");
                                }
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                /*
                Intent start_view_createCommands = new Intent(this, view_createCommands.class);
                startActivity(start_view_createCommands);*/
                break;
            case R.id.itemCommandTotal:
                for (int i = 1; i < listCommands.size(); i++) {
                    totalMoney += listCommands.get(i).totalCommandValue();
                }
                setTitle("RS " + new DecimalFormat("#,##0.00").format(totalMoney) + " | " + listCommands.size());
                break;
            case R.id.itemCommandSend:
                String sendNote = "*Mensagem Automática:*\n *Lista de Comandas:* " + ".\n \n \n";
                for (int i = 1; i < listCommands2.size(); i++) {
                    if (listCommands2.get(i).getSituation().equals("")) {
                        totalMoney += listCommands2.get(i).totalCommandValue();
                        sendNote += listCommands2.get(i).getName()
                                + "| *RS"
                                + new DecimalFormat("#,##0.00").format(listCommands2.get(i).totalCommandValue())
                                + "*\n";
                    }
                }
                sendNote += "\n Total Geral: *" + new DecimalFormat("#,##0.00").format(totalMoney) + "* para Receber.";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendNote.toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void startComponents() {
        listV_pesquisa_command = (ListView) findViewById(R.id.listV_pesquisa_command);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gerList();
    }

    private void gerList() {
        listCommands2.clear();
        new ConnectDB().ordeByDate(view_ListCommands.this, "Command")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                            Command c = objSnapshot.getValue(Command.class);
                            listCommands2.add(c);
                        }
                        filterCommands();
                        setAddapter_command();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void filterCommands() {
        listCommands.clear();
        if (seachWord_) {
            if (seachWord.equals("")) {
                for (int i = 0; i < listCommands2.size(); i++) {//Lista todas comandas não pagas
                    if (!listCommands2.get(i).getSituation().equals("#")) {
                        listCommands.add(listCommands2.get(i));
                    }
                }
            } else {
                for (int i = 0; i < listCommands2.size(); i++) {//Pesquisa em todas as Commandas não pagas
                    if (listCommands2.get(i).getName().toUpperCase().contains(seachWord.toUpperCase()) && !listCommands2.get(i).getSituation().equals("#")) {
                        listCommands.add(listCommands2.get(i));
                    }
                }
            }
        } else {
            if (seachWord.equals("")) {
                for (int i = 0; i < listCommands2.size(); i++) {//Todas comandas pagas
                    if (listCommands2.get(i).getSituation().equals("#")) {
                        listCommands.add(listCommands2.get(i));
                    }
                }
            } else {
                for (int i = 0; i < listCommands2.size(); i++) {//Pesquisa em comandas pagas
                    if (listCommands2.get(i).getSituation().equals("#") && listCommands2.get(i).getName().toUpperCase().contains(seachWord.toUpperCase())) {
                        listCommands.add(listCommands2.get(i));
                        grandTotal += listCommands2.get(i).totalCommandValue();
                    }
                }
            }
        }
    }

    private void setAddapter_command() {
        commandAdapter = new CommandAdapter(getBaseContext(), R.layout.item_model_command, listCommands);
        commandAdapter.sort(new Comparator<Command>() {//Ordenando
            @Override
            public int compare(Command command, Command t1) {
                return t1.getDate().compareTo(command.getDate());
            }
        });
        //arrayAdapterCommand = new ArrayAdapter<Command>(view_ListCommands.this, android.R.layout.simple_list_item_1, listCommands);
        listV_pesquisa_command.setAdapter(commandAdapter);
        //Tornando Clícável
        quickClick();//Clique unico e rápido
        longClick();// Clique lonfo
    }

    private void longClick() {
        listV_pesquisa_command.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Command choseCommand = listCommands.get(i);
                new AlertDialog.Builder(view_ListCommands.this)
                        .setTitle("O que deseja fazer?")
                        .setMessage(choseCommand.getName())
                        .setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent openEdit_command = new Intent(view_ListCommands.this, view_createCommands.class);
                                openEdit_command.putExtra("command_uid", choseCommand.getUid());
                                startActivity(openEdit_command);
                            }
                        })
                        .setNegativeButton("EXCLUIR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new AlertDialog.Builder(view_ListCommands.this).setTitle("Realmente deseja EXCLUIR?")
                                        .setMessage(choseCommand.getName())
                                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                deletingCommand(choseCommand);//Deletando comanda - Criado com (Control + Alt + m)
                                            }
                                        }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                            }
                        }).show();


                return true;
            }
        });
    }

    private void quickClick() {
        listV_pesquisa_command.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Command chosen_Command = listCommands.get(i);
                //Intent sendCommand_view_Command_ListProduct = new Intent(view_ListCommands.this, view_Command_ListProduct.class);
                //sendCommand_view_Command_ListProduct.putExtra("COMMAND", chosen_Command);
                //startActivity(sendCommand_view_Command_ListProduct);
                startActivity(new Intent(view_ListCommands.this, view_Command_ListProduct.class).putExtra("COMMAND", chosen_Command));
            }
        });
    }

    private void deletingCommand(Command c) {//Deletando
        msDialog("Apagando comanda de " + c.getName());
        checkNotification.insert(c.getName(), "Comanda excluída em: ", c.getUid());
        new ConnectDB().insertDB(view_ListCommands.this, "CheckNotification", checkNotification.getUid()).setValue(checkNotification);
        new ConnectDB().ConnectDB(view_ListCommands.this).child("Command").child(c.getUid()).removeValue();
        onResume();
    }
    public void msDialog(String ms) {//Mensagens Toast
        Toast.makeText(view_ListCommands.this, ms.toString(), Toast.LENGTH_SHORT).show();
    }

    //Retornando tela anterior
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent cancelReturn = new Intent(this, MainActivity.class);// Abrindo tela Inicial
        startActivity(cancelReturn);
    }


}