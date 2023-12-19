package portaldapecuaria.com.mariozinho.viewComand;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.model.Command;
import portaldapecuaria.com.mariozinho.viewProduct.view_Add_Product_in_Command;

public class view_createCommands extends AppCompatActivity {
    //Campos
    EditText editName, editCommandCel;
    TextView tv_newCommand;
    Command c;
    String command_uid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_create_new);
        startCommponents();
        gerIntent();
    }

    private void startCommponents() {
        editName = (EditText) findViewById(R.id.edit_Command_name);
        editCommandCel = (EditText) findViewById(R.id.edit_Command_cell);
        tv_newCommand = (TextView) findViewById(R.id.tv_newCommand);
    }

    private void gerIntent() {
        Intent data_command = getIntent();
        if (data_command.hasExtra("command_uid")) {
            command_uid = data_command.getStringExtra("command_uid");
            Toast.makeText(view_createCommands.this, "Chegou aqui", Toast.LENGTH_LONG).show();
            new ConnectDB().ordeByID(view_createCommands.this, "Command").equalTo(command_uid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                                c = objSnapshot.getValue(Command.class);
                                setTitle("Editando");
                                tv_newCommand.setText(c.getUid());
                                editName.setText(c.getName());
                                editCommandCel.setText(c.getCel());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    //Retornando tela anterior
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent open_Active_main = new Intent(view_createCommands.this, view_ListCommands.class);
        startActivity(open_Active_main);

    }

    //Criando Comanda
    public void saveCommand(View v) {
        if (command_uid != "") {
            new AlertDialog.Builder(view_createCommands.this)
                    .setTitle("Salvar Edição")
                    .setMessage("Deseja salvar as alterações em " + c.getName())
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            c.updateDB(editName.getText().toString(), editCommandCel.getText().toString());
                            new ConnectDB().updateItem(view_createCommands.this, "Command", c.getUid().toString()).setValue(c);
                            open_ListProducts_in_Command(c);
                            Toast.makeText(view_createCommands.this, "Salvando...", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        } else {
            saveNewCommand();
        }
    }

    private void saveNewCommand() {
        Command c = new Command();
        //c.insertCommand(editName.getText().toString(), editCommandCel.getText().toString());
        new AlertDialog.Builder(view_createCommands.this)
                .setTitle("Salvar")
                .setMessage("Deseja salvar: " + c.getName())
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //new ConnectDB().updateItem(view_createCommands.this, "Command", c.getUid().toString()).setValue(c);
                        Toast.makeText(view_createCommands.this, "A comanda de " + c.getName() + " foi salva com sucesso", Toast.LENGTH_SHORT).show();
                        open_ListProducts_in_Command(c);
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

    private void open_ListProducts_in_Command(Command c) {
        Intent open_ListProducts_in_Command = new Intent(getBaseContext(), view_Add_Product_in_Command.class);
        open_ListProducts_in_Command.putExtra("COMMAND", c);
        finish(); //Fechando Tela Atual
        startActivity(open_ListProducts_in_Command);
    }

}