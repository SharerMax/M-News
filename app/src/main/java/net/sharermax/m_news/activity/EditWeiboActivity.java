package net.sharermax.m_news.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import net.sharermax.m_news.R;
import net.sharermax.m_news.support.AccessTokenKeeper;
import net.sharermax.m_news.support.SharerToHelper;
import net.sharermax.m_news.support.Utility;

/**
 * Author: SharerMax
 * Time  : 2015/4/11
 * E-Mail: mdcw1103@gmail.com
 */
public class EditWeiboActivity extends AbsActivity
        implements LocationListener, View.OnClickListener{
    public static final String CLASS_NAME = "EditWeiboActivity";
    private MaterialEditText mWeiboEditText;
    private TextView mLocationTextView;
    private LocationManager mLocationManager;
    private Button mSendButton;
    private Location mLocation;
    public static final int WEIBO_MAX_COUNT = 140;
    public static final String EXTRA_FLAG = "weibo_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_weibo_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWeiboEditText = (MaterialEditText)findViewById(R.id.weibo_edit_editTextView);
        mLocationTextView = (TextView)findViewById(R.id.location_textview);
        mSendButton = (Button)findViewById(R.id.weibo_send);
        mSendButton.setOnClickListener(this);
        mWeiboEditText.addValidator(new CountValidator(getString(R.string.weibo_edit_error)));

        Intent intent = getIntent();
        String weiboStatus = intent.getStringExtra(EXTRA_FLAG);
        if (null == weiboStatus) {
            mWeiboEditText.setText("");
        } else {
            mWeiboEditText.setText(weiboStatus);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationManager = Utility.updateLocation(getApplicationContext(), this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (null != location) {
            mLocation = location;
            Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
            SharerToHelper.geoToAddress(getApplicationContext(), token.getToken(), location,
                    new SharerToHelper.GeoToAddressListener() {
                        @Override
                        public void onResponse(String address) {
                            mLocationTextView.setText(address);
                        }
                    });
            if (null != mLocationManager) {
                mLocationManager.removeUpdates(this);
            }

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weibo_send:
//                Log.v(CLASS_NAME, mWeiboEditText.getText().toString());
                if (!(mWeiboEditText.getText().toString().length() > WEIBO_MAX_COUNT)) {
                    SharerToHelper.sendToWeibo(getApplicationContext(),
                            mWeiboEditText.getText().toString(),
                            getString(R.string.send_success),
                            getString(R.string.send_fail),
                            mLocation);
                    this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.weibo_edit_error), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    class CountValidator extends METValidator {

        public CountValidator(@NonNull String errorMessage) {
            super(errorMessage);
        }

        @Override
        public boolean isValid(@NonNull CharSequence charSequence, boolean b) {
            return !(charSequence.length() > WEIBO_MAX_COUNT);
        }
    }
}
