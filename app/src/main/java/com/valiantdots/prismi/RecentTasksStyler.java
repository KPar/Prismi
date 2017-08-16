package com.valiantdots.prismi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;


public class RecentTasksStyler {
    private static Bitmap sIcon = null;

    private RecentTasksStyler() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void styleRecentTasksEntry(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int colorPrimary;
        Resources resources = activity.getResources();
        String label = resources.getString(activity.getApplicationInfo().labelRes);
        colorPrimary=activity.getApplicationContext().getColor(R.color.white);
        activity.setTaskDescription(new ActivityManager.TaskDescription(label, sIcon, colorPrimary));

        if (sIcon == null) {
            // Cache to avoid decoding the same bitmap on every Activity change
            sIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
        }
    }
}
