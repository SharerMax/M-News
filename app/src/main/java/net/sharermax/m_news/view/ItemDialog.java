package net.sharermax.m_news.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.sharermax.m_news.R;

/**
 * Author: SharerMax
 * Time  : 2015/4/27
 * E-Mail: mdcw1103@gmail.com
 */
public class ItemDialog {
    private Context mContext;
    private  AlertDialog mAlertDialog;
    private static ItemDialog sItemDialog;
    public static ItemDialog getInstance(Context context) {
        if (null == sItemDialog) {
            sItemDialog = new ItemDialog(context);
        }
        return sItemDialog;
    }
    private ItemDialog(Context context) {
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        String [] items = new String[2];
        items[0] = "Star";
        items[1] = "Share to Weiobo";
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "click" + which, Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != sItemDialog) {
                    sItemDialog.dismiss();
                }
            }
        });
        builder.setTitle(R.string.share_to);
        mAlertDialog = builder.create();
    }

    public void show() {
        if (null != mAlertDialog && !mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    public void dismiss() {
        if (null != mAlertDialog && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
