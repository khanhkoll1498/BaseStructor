package com.masterlibs.basestructure.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {


    private static String oldMsg;

    private static Toast toast = null;

    private static long oneTime = 0;

    private static long twoTime = 0;

    public static void showToast(Context c, String s) {
        try {
            if (null == s) {
                return;
            }
            if (toast == null) {
                toast = Toast.makeText(c, s, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                oneTime = System.currentTimeMillis();
            } else {
                twoTime = System.currentTimeMillis();
                if (s.equals(oldMsg)) {
                    if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                        toast.show();
                    }
                } else {
                    oldMsg = s;
                    toast.setText(s);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
            oneTime = twoTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context c, Integer res) {
        if (null == res) {
            return;
        }
        String s = c.getResources().getString(res);
        if (toast == null) {
            toast = Toast.makeText(c, s, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        oneTime = twoTime;
    }


    public static void showToast(Context context, String s, int duration) {
        try {
            if (TextUtils.isEmpty(s)) {
                return;
            }
            if (toast == null) {
                toast = Toast.makeText(context, s, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                oneTime = System.currentTimeMillis();
            } else {
                twoTime = System.currentTimeMillis();

                if (s.equals(oldMsg)) {
                    if (twoTime - oneTime > duration) {
                        toast.show();
                    }
                } else {
                    oldMsg = s;
                    toast.setText(s);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
            oneTime = twoTime;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
