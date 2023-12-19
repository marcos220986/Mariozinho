package portaldapecuaria.com.mariozinho.database;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ConnectDB {
    //Conexão com Banco de Dados
    boolean firebaseOffiline = false;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public DatabaseReference ConnectDB(Context view) {
        //Realizando Consulta em Banco de Dados
        //enablePersistense();
        FirebaseApp.initializeApp(view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        return databaseReference;
    }
    public Query ordeByID(Context view, String classTable){//Consulta por ID
        Query query = ConnectDB(view).child(classTable).orderByChild("uid");
        return query;
    }
    public Query ordeByName(Context view, String classTable){//Consulta por nome
       Query query = ConnectDB(view).child(classTable).orderByChild("name");
       return query;
    }
    public Query ordeByMail(Context view, String classTable){//Consulta por e-mail
        Query query = ConnectDB(view).child(classTable).orderByChild("email");
        return query;
    }
    public Query ordeByCheckLogin(Context view, String classTable){//Checker_login
        Query query = ConnectDB(view).child(classTable).orderByChild("checker_login");
        return query;
    }
    public Query ordeByDate(Context view, String classTable){//Consulta por data
        Query query = ConnectDB(view).child(classTable).orderByChild("date");
        return query;
    }
    public Query ordeByBarCode(Context view, String classTable){//Consulta por data
        Query query = ConnectDB(view).child(classTable).orderByChild("barCode");
        return query;
    }

    public void removeItem(Context view, String classTable, String uid){
        ConnectDB(view).child(classTable).child(uid).removeValue();
    }
    public DatabaseReference updateItem(Context view, String classTable, String uid){
        return ConnectDB(view).child(classTable).child(uid);
    }
    public DatabaseReference insertDB(Context view, String classTable, String uid){
        return ConnectDB(view).child(classTable).child(uid);
    }
    public void enablePersistense(){
        try {
            if(!firebaseOffiline){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                firebaseOffiline = true;
            }else {
                //Firebase oline
            }
        }catch (Exception e){
            //erro
        }
    }
}
