package portaldapecuaria.com.mariozinho;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import portaldapecuaria.com.mariozinho.database.ConnectDB;
import portaldapecuaria.com.mariozinho.model.CheckNotification;

public class view_CheckNotification extends AppCompatActivity implements Runnable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_check_notification);

        new ConnectDB().ordeByName(view_CheckNotification.this, "CheckNotification").limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int qtd = 0;
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    CheckNotification checkNotification = objSnapshot.getValue(CheckNotification.class);
                    qtd ++;
                    if(qtd == 1){
                        new ConnectDB().removeItem(view_CheckNotification.this, "CheckNotification", checkNotification.getUid());
                        Toast.makeText(view_CheckNotification.this, "Notificação visualizada...", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Handler handler = new Handler(); //contador de tempo
        handler.postDelayed(this, 5000); //o exemplo 2000 = 2 segundos
    }

    @Override
    public void run(){
        returnMainActivity();
    }

    public void returnMainActivity(){
        finish();;
        Intent returnMain = new Intent(view_CheckNotification.this, MainActivity.class);
        startActivity(returnMain);
    }
}