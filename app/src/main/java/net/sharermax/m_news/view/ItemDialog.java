package net.sharermax.m_news.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import net.sharermax.m_news.R;
import net.sharermax.m_news.activity.EditWeiboActivity;
import net.sharermax.m_news.adapter.DatabaseAdapter;

/**
 * Author: SharerMax
 * Time  : 2015/4/27
 * E-Mail: mdcw1103@gmail.com
 */
public class ItemDialog {
    public static final String CLASS_NAME = "ItemDialog";
    private Context mContext;
    private MaterialDialog mMaterialDialog;
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
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
        builder.cancelable(true);
        builder.items(R.array.itemdialog_actions);
        builder.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                if (null == mAdapter) {
                    return;
                }
                switch (which) {
                    case 0:
                        if (!mAdapter.isOpen()) {
                            mAdapter.open();
                        }
                        if (mAdapter.isExist()) {
                            Toast.makeText(mContext, "has been favorited", Toast.LENGTH_LONG).show();
                        } else {
                            mAdapter.insert();
                        }
                        break;
                    case 1:
                        Intent sendIntent = new Intent();
                        sendIntent.putExtra(EditWeiboActivity.EXTRA_FLAG, mAdapter.getSendData());
                        sendIntent.setClass(mContext, EditWeiboActivity.class);
                        mContext.startActivity(sendIntent);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.negativeText(R.string.itemdialog_negative_button);
        builder.title(R.string.itemdialog_title);
        mMaterialDialog = builder.build();
    }

    public void show() {
        if (null != mMaterialDialog && !mMaterialDialog.isShowing()) {
            mMaterialDialog.show();
        }
    }

    public void dismiss() {
        if (null != mMaterialDialog && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
    }

    public void setAdapter(DatabaseAdapter adapter) {
        mAdapter = adapter;
    }
}
