package com.developer.hare.tworaveler.FaceBook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.developer.hare.tworaveler.R;

public class FaceBookActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private Button BT_signIn, BT_signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        init();
    }

    private void init() {
//        String key = getKeyHash(getBaseContext());
//        Log.i(TAG, "##########  key : " + key);

        BT_signIn = (Button) findViewById(R.id.facebook$BT_facebook_signIn);
        BT_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), FaceBookSignInActivity.class));
            }
        });
        BT_signOut = (Button) findViewById(R.id.facebook$BT_facebook_signOut);
        BT_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogout();
            }
        });
    }

    private void onClickLogout() {
    }
}
