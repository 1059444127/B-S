package com.czk.diabetes.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.czk.diabetes.MainActivity;
import com.czk.diabetes.R;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by 陈忠凯 on 2017/4/19.
 */

public class MYNotificationManager {
    private static volatile MYNotificationManager instance;

    public static MYNotificationManager getInstance() {
        if (instance == null) {
            synchronized (MYNotificationManager.class) {
                if (instance == null) {
                    instance = new MYNotificationManager();
                }
            }
        }

        return instance;
    }

    public void setNotifition(NotificationItem notificationItem, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", notificationItem.getTitle());
        intent.putExtra("description", notificationItem.getDescription());
        intent.putExtra("type", notificationItem.getType());
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationItem.getTime(), pi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, notificationItem.getTime(), pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, notificationItem.getTime(), pi);
        } else {
            alarmManager.set(AlarmManager.RTC, notificationItem.getTime(), pi);
        }
    }

    public void showNotifition(NotificationItem notificationItem, Context context) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notification = new Notification.Builder(context)
                .setContentTitle(notificationItem.getTitle())
                .setContentText(notificationItem.getDescription())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon)
                .getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }

    public static void cancleAlarm(Intent intent,Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (intent != null) {
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
                alarmManager.cancel(pi);
            }
        } catch (Exception e) {
        }

    }
}
