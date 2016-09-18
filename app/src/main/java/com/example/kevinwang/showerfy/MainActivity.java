package com.example.kevinwang.showerfy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import static com.example.kevinwang.showerfy.R.string.pointsTotal;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    ImageButton bigButton, smallButton, histoButton;
    TextView bigText, pointsText;
    private int state = 0, points = 0;
    private static SharedPreferences prefs;
    static Calendar timerStart;
    private long songDuration = 0, songTimerStart;
    private RotateAnimation r;

    //SPOTIFY FIELDS
    private static String chosenSong = "spotify:track:7GhIk7Il098yCjg4BQjzvb";
    private static String songTitle = "Take Me to Church";
    private static final String CLIENT_ID = "f023aedf8bf34ea5a638ae933f41e944";
    private static final String REDIRECT_URI = "showerfybigredhacks://callback";

    private static final int REQUEST_CODE = 1337;
    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spotify login intent
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        prefs = getPreferences(0);
        points = prefs.getInt("points", 0);
        pointsText = (TextView) findViewById(R.id.text_points);
        pointsText.setText(String.format(getString(pointsTotal), points));

        bigButton = (ImageButton) findViewById(R.id.imageButton);
        bigText = (TextView) findViewById(R.id.text1);
        bigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleClick();
            }
        });

        smallButton = (ImageButton) findViewById(R.id.imageButton2);
        smallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleMusicClick();
            }
        });

        histoButton = (ImageButton) findViewById(R.id.imageButton3);
        histoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleHistoClick();
            }
        });
    }

    private void handleMusicClick() {
        Intent songSelect = new Intent(this, SongSelectActivity.class);
        startActivityForResult(songSelect, 1);
    }

    private void handleHistoClick() {
        Intent histoView = new Intent(this, RecordsActivity.class);
        startActivity(histoView);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void handleClick() {
        switch (state) {
            case 0:
                ImageButton bigBut = (ImageButton) findViewById(R.id.imageButton);

                AnimationSet animationSet = new AnimationSet(true);

                r = new RotateAnimation(0f, 355f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                r.setDuration(1200);
                r.setRepeatCount(Animation.INFINITE);
                r.setFillAfter(true);
                animationSet.addAnimation(r);
                animationSet.setInterpolator(new LinearInterpolator());
                animationSet.setFillAfter(true);
                bigBut.startAnimation(animationSet);

                mPlayer.play(chosenSong);
                songTimerStart = System.currentTimeMillis();
                bigButton.setImageResource(R.drawable.icons2);
                bigText.setText(R.string.showeringNow);
                state = 1;
                break;
            case 1:
                mPlayer.pause();
                bigButton.setImageResource(R.drawable.icons);
                r.setRepeatCount(0);
                bigText.setText(R.string.readyShower);
                state = 0;
                long timeDiff = Calendar.getInstance().getTimeInMillis() - timerStart.getTimeInMillis();
                Toast t = Toast.makeText(getApplicationContext(), "Shower time: " + String.format("%d min, %d sec",
                        MILLISECONDS.toMinutes(timeDiff),
                        MILLISECONDS.toSeconds(timeDiff) -
                                MINUTES.toSeconds(MILLISECONDS.toMinutes(timeDiff))
                ), Toast.LENGTH_LONG);
                t.show();

                long mins = MILLISECONDS.toMinutes(timeDiff);
                points += Math.min(20, 40.0f / mins);
                pointsText.setText(getString(pointsTotal, points));
                prefs.edit().putInt("points", points).apply();

                long dt = timeDiff - songDuration;

                if (songDuration == 0)
                    badEnding(dt);
                else
                    goodEnding();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //SPOTIFY CALLBACKS
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer = player;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        } else if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                chosenSong = intent.getStringArrayExtra("song")[0];
                songTitle = intent.getStringArrayExtra("song")[1];
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            case END_OF_CONTEXT:
                songDuration = System.currentTimeMillis() - songTimerStart;
                break;
            default:
                break;
        }
    }

    private void badEnding(long dt) {
        Intent penalty = new Intent(this, PenaltyActivity.class);
        String[] info = {songTitle, String.valueOf((double) dt)};
        penalty.putExtra("info", info);
        startActivity(penalty);
    }

    private void goodEnding() {
        Intent success = new Intent(this, PenaltyActivity.class);
        String[] info = {songTitle, String.valueOf(points)};
        success.putExtra("info", info);
        startActivity(success);
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}