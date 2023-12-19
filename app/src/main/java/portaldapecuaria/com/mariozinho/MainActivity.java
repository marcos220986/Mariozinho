package portaldapecuaria.com.mariozinho;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.databinding.ActivityMainBinding;
import portaldapecuaria.com.mariozinho.model.CheckNotification;
import portaldapecuaria.com.mariozinho.viewComand.view_ListCommands;
import portaldapecuaria.com.mariozinho.viewLocation.view_Location;
import portaldapecuaria.com.mariozinho.viewProduct.view_ListProducts;

// Ctrl+Alt+O para otimizar importações / Retirar importaçõs não usadas
public class MainActivity extends AppCompatActivity {
    //Para uso no lugar de findById
    private ActivityMainBinding binding;
    //Login Firebase
    private FirebaseAuth mAuth;//Necessario para criação de conta
    FirebaseUser currentUser; //Necessário para obter dados do usuário atual
    //String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    String channel_id;
    Intent intent;
    LinearLayout inputs_login;
    private static final String Ficheiro = "Alls.txt";
    boolean mVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Usado no lugar findByID
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Dessattivando Edit de e-mail
        binding.edtMailLog.setEnabled(true);
        //Ouvindo Edits e Validando dados
        checkMail();//Valida e-mail
        checkPw_log(); //Valida Senha
        //Inicializando FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //Linnear de Entrar Oculto
        checkNotifications();
        //Ouvindo clique em Entrar Logar
        binding.btnLogInto.setOnClickListener(view -> {
            mAuth.signInWithEmailAndPassword(
                    binding.edtMailLog.getText().toString(),
                    binding.edtPasswordLog.getText().toString()
            ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            });
            //log_into();
        });

        //Ouvindo clique em Entrar Logar
        binding.tvRecoverAccount.setOnClickListener(view -> {
            binding.btnLogInto.setVisibility(View.INVISIBLE);
            binding.edtPasswordLog.setVisibility(View.INVISIBLE);
            binding.tvDescribe.setVisibility(View.VISIBLE);
            binding.btnRecoverAcoount.setVisibility(View.VISIBLE);
            binding.tvRecoverAccount.setEnabled(false);
        });
        binding.tvCreateAccount.setOnClickListener(view -> {
            //startActivity(new Intent(this, view_RegisterLoginUser.class));
            mMessage("Entre em contato com o número: +5599991633616");
        });
        //Enviando recuperação de conta
        binding.tvRecoverAccount.setOnClickListener(view -> {
            mAuth.sendPasswordResetEmail(binding.edtMailLog.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Linde recuperação enviado com sucesso. Já pode verificar seu e-mail", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "E-mail não encontrado. Tente mais tarde.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    //Chegando notificações
    private void checkNotifications() {
        new ConnectDB().ordeByName(MainActivity.this, "CheckNotification").limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int qtd = 0;
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    CheckNotification checkNotification = objSnapshot.getValue(CheckNotification.class);
                    qtd++;
                    if (qtd == 1) {
                        createNotificationChannel(checkNotification.getChannel());
                        new ActionMethods().gerNotifications(checkNotification, MainActivity.this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Chegando validade de e-mail
    private void checkMail() {
        binding.edtMailLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (new ActionMethods().isMailValid(charSequence.toString())) {
                    binding.edtPasswordLog.setEnabled(true);
                    binding.btnRecoverAcoount.setEnabled(true);
                } else {
                    binding.edtPasswordLog.setEnabled(false);
                    binding.btnRecoverAcoount.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Chegando validade de nome
    private void checkPw_log() {
        binding.edtPasswordLog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int total_string = binding.edtPasswordLog.getText().toString().length();
                if (total_string > 5 && total_string < 10) {
                    binding.btnLogInto.setEnabled(true);
                } else {
                    binding.btnLogInto.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        ((MenuItem) menu.findItem(R.id.itemCommand)).setVisible(mVisible);
        ((MenuItem) menu.findItem(R.id.itemProduct)).setVisible(mVisible);
        ((MenuItem) menu.findItem(R.id.itemExit)).setVisible(mVisible);
        //((MenuItem) menu.findItem(R.id.itemLocalization)).setVisible(true);

        if(mVisible){binding.linearShowLogIn.setVisibility(View.INVISIBLE);}

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCommand:
                startActivity(new Intent(this, view_ListCommands.class));
                break;
            case R.id.itemProduct:
                startActivity(new Intent(this, view_ListProducts.class));
                break;
            //case R.id.itemLocalization:
                //startActivity(new Intent(this, view_Location.class));
               // break;
            case R.id.itemExit:
                mAuth.signOut();//Desconectando usuário
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        java.lang.System.exit(0);
    }

    public void createNotificationChannel(String channel_id) {//Criando canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            //String channel_id = getString(R.string.channel_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void mMessage(String m) {
        Toast.makeText(MainActivity.this, m, Toast.LENGTH_LONG).show();
    }

    //Verificando se usuário está logado
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //mMessage("Logado como: " + currentUser.getEmail());
            mVisible = true;
        } else {
            binding.edtMailLog.setEnabled(true);
            binding.linearShowLogIn.setVisibility(View.VISIBLE);
            mVisible = false;
        }
    }
}