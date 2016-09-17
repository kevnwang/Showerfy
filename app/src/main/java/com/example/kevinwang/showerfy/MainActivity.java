package com.example.kevinwang.showerfy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.*;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
//import com.spotify.sdk.android.player.PlayerNotificationCallback;
//import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.authentication.AuthenticationRequest;

public class MainActivity extends Activity {
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "f023aedf8bf34ea5a638ae933f41e944";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "showerfybigredhacks://callback";

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
}