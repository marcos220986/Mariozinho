package portaldapecuaria.com.mariozinho;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import portaldapecuaria.com.mariozinho.databinding.ViewRegisterLoginUserBinding;

public class view_RegisterLoginUser extends AppCompatActivity {

    //viewBindig para uso no lugar findByID
    private ViewRegisterLoginUserBinding binding;
    private FirebaseAuth mAuth;//Necessario para criação de conta

    String register_user, regiter_mail, register_pw1, register_pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setando Binding utilizado no lugar de findByid
        binding = ViewRegisterLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Validando dados
        checkMail();
        checkPw_log();
        checkPw_log2();
        //Comunicação com Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.btnRegisterCreate.setOnClickListener(view -> {
            mAuth.createUserWithEmailAndPassword(
                    binding.edtRegiterMail.getText().toString(),
                    binding.edtRegisterPw1.getText().toString()
            ).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    Toast.makeText(view_RegisterLoginUser.this, "Conta criada com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(view_RegisterLoginUser.this, MainActivity.class));
                }else {
                    Toast.makeText(view_RegisterLoginUser.this, "Tente mais tarde.", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    //Validando e-mail
    private void checkMail() {
        binding.edtRegiterMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (new ActionMethods().isMailValid(charSequence.toString())) {
                    binding.edtRegisterPw1.setEnabled(true);
                } else {
                    binding.edtRegisterPw1.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Checando senha 01
    private void checkPw_log() {
        binding.edtRegisterPw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int total_string = binding.edtRegisterPw1.getText().toString().length();
                if (total_string > 5 && total_string < 10) {
                    binding.edtRegisterPw2.setEnabled(true);
                } else {
                    binding.edtRegisterPw2.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //Checando senha 02
    private void checkPw_log2() {
        binding.edtRegisterPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.edtRegisterPw1.getText().toString().equals(binding.edtRegisterPw2.getText().toString())) {
                    binding.btnRegisterCreate.setEnabled(true);
                } else {
                    binding.btnRegisterCreate.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}