package ma.ensa.www.assistdoc.model;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import ma.ensa.www.assistdoc.patient.Medications_Activity;
import ma.ensa.www.assistdoc.R;


public class MedicationReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve data from the intent
        String medicamentName = intent.getStringExtra("medicament_name") != null
                ? intent.getStringExtra("medicament_name")
                : "Médicament";
        String notificationType = intent.getStringExtra("notification_type") != null
                ? intent.getStringExtra("notification_type")
                : "default";

        // Call the method to show the notification
        showNotification(context, medicamentName, notificationType);
    }

    private void showNotification(Context context, String medicamentName, String type) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "medication_channel";

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Rappels de Médicament",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Intent to open the activity when clicking on the notification
        Intent intent = new Intent(context, Medications_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification with variations based on the type
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.soleil)
                .setContentTitle("Notification de Médicament")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Customize based on notification type
        switch (type) {
            case "reminder":
                notificationBuilder.setContentText("Il est temps de prendre votre médicament: " + medicamentName);
                break;
            case "delete":
                notificationBuilder.setContentText("Le médicament " + medicamentName + " a été supprimé.");
                break;
            case "add":
                notificationBuilder.setContentText("Le médicament " + medicamentName + " a été ajouté avec succès.");
                break;
            // You can add other types if needed
            default:
                notificationBuilder.setContentText("Notification inconnue.");
                break;
        }

        // Show the notification
        notificationManager.notify(medicamentName.hashCode(), notificationBuilder.build());
    }
}
