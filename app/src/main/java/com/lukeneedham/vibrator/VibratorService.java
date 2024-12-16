import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;

public class VibratorService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "VibratorServiceChannel";
    private Handler handler;
    private Runnable vibrateTask;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize handler and vibration logic
        handler = new Handler();
        vibrateTask = new Runnable() {
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(1000); // Vibrate for 1 second
                }
                handler.postDelayed(this, 4 * 60 * 1000); // Schedule every 4 minutes
            }
        };
        startForegroundService();
        handler.post(vibrateTask); // Start the vibration task
    }

    private void startForegroundService() {
        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Vibrator Service",
                NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Create the notification
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Vibration Service Running")
            .setContentText("The app will vibrate every 4 minutes.")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Restart the service if it gets killed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(vibrateTask); // Stop the task when the service is destroyed
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }
}
