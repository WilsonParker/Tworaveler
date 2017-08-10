package com.developer.hare.tworaveler.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

/**
 * Created by Hare on 2017-08-10.
 */

public class KeyManager {
    private static KeyManager keyManager = new KeyManager();

    public static KeyManager getInstance() {
        return keyManager;
    }

    public String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log_HR.log(getClass(), "getKeyHash(Context)", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}
