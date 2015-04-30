package net.sharermax.m_news.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.DatabaseAdapter;

/**
 * Author: SharerMax
 * Time  : 2015/4/27
 * E-Mail: mdcw1103@gmail.com
 */
public class ItemDialog {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private DatabaseAdapter mAdapter;
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
        items[0] = mContext.getString(R.string.itemdialog_star_action);
        items[1] = mContext.getString(R.string.itemdialog_share_action);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "click" + which, Toast.LENGTH_LONG).show();
                switch (which) {
                    case 0:
                        Toast.makeText(mContext, "star" + which, Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        if (null != mAdapter) {
                            Intent sendIntent = new Intent();
                            sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, mAdapter.getSendData());
                            sendIntent.setClass(mContext, EditWeiboActivity.class);
                            mContext.startActivity(sendIntent);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.itemdialog_negative_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != sItemDialog) {
                            sItemDialog.dismiss();
                        }
                    }
                });
        builder.setTitle(R.string.itemdialog_title);
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

    public void setAdapter(DatabaseAdapter adapter) {
        mAdapter = adapter;
    }
}
