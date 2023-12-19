package portaldapecuaria.com.mariozinho;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Patterns;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import portaldapecuaria.com.mariozinho.R;
import portaldapecuaria.com.mariozinho.model.CheckNotification;

public class ActionMethods {



    public void gerNotifications(CheckNotification checkNotification, Context context) {

        Intent intent = new Intent(context, view_CheckNotification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        SimpleDateFormat df = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("dd/MM/YY - HH:mm");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, checkNotification.getChannel())
                .setSmallIcon(R.drawable.allscaixa)
                .setContentTitle(checkNotification.getMsChekTitle())
                .setContentText(checkNotification.getMsChek())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(checkNotification.getMsChek() + df.format(checkNotification.getDate())))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setTimeoutAfter(5000000)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);//Fechando notificação ao toque
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(150, builder.build());
    }

    public String getDeviceIMEI(Activity activity) {

        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    public String gerDate() {
        Date date_c_new = new Date();
        SimpleDateFormat df = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        }
        String dateString = df.format(date_c_new);
        return dateString;
    }

    public boolean isMailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Obtendo MD5 em java
    public String gerMD5(String s){
        final String MD5 = "MD5";
        try {
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for(byte aMessageDigest : messageDigest){
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 0)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }

    public String gerDateFormate(){
        Date date = new Date();
        SimpleDateFormat df = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            df = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        }
        return df.format(date);
    }

}
