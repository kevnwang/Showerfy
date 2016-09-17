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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "f023aedf8bf34ea5a638ae933f41e944";
    private static final String REDIRECT_URI = "showerfybigredhacks://callback";

    ImageButton bigButton;
    ImageButton smallButton;
    TextView bigText, pointsText;
    private int state = 0;
    private int points = 0;
    private int songNum = 0; //notes whether song playing is 1st or 2nd
    public static final int NUM_OF_SONGS = 2;

    private SharedPreferences prefs;

    private static List<String> activeUris = new ArrayList<String>(); //activeUris.add("spotify:track:7GhIk7Il098yCjg4BQjzvb");
    private String[] chosenSongs;

    private Calendar timerStart;

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        /*Intent songSelect = new Intent(this, SongSelectActivity.class);
        startActivityForResult(songSelect, 1);*/

        prefs = getPreferences(0);
        points = prefs.getInt("points", 0);
        pointsText = (TextView) findViewById(R.id.text_points);
        pointsText.setText(String.format("Points: %d", points));

        addButtonListener();
        addSmallButtonListener();

        Bundle songNames = getIntent().getExtras();

        if(songNames!=null){
            chosenSongs = songNames.getStringArray("songs");
            for(int i=0;i<chosenSongs.length;i++){
                activeUris.add(chosenSongs[i]);
            }

        }
    }

    private void addButtonListener() {
        bigButton = (ImageButton) findViewById(R.id.imageButton);
        bigText = (TextView) findViewById(R.id.text1);
        bigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleClick();
            }
        });

    }

    private void addSmallButtonListener(){
        smallButton = (ImageButton) findViewById(R.id.imageButton2);
        smallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                handleMusicClick();
            }
        });

    }


    private void handleMusicClick(){
        setContentView(R.layout.activity_main);

        Intent songSelect = new Intent(this, SongSelectActivity.class);
        startActivityForResult(songSelect, 0);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void handleClick() {
        switch (state) {
            case 0:
                mPlayer.play(activeUris.get(songNum));
                bigButton.setImageResource(R.drawable.icons2);
                bigText.setText("Shower!");
                state = 1;
                timerStart = Calendar.getInstance();
                break;
            case 1:
                mPlayer.pause();
                bigButton.setImageResource(R.drawable.icons);
                bigText.setText("Press to Shower");
                state = 0;
                long timeDiff = Calendar.getInstance().getTimeInMillis() - timerStart.getTimeInMillis();
                Toast t = Toast.makeText(getApplicationContext(), "Shower time: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(timeDiff),
                        TimeUnit.MILLISECONDS.toSeconds(timeDiff) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeDiff))
                ), Toast.LENGTH_LONG);
                t.show();

                long mins = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
                if (mins <= 2)
                    points += 20;
                else
                    points += 20 / (mins - 2);
                pointsText.setText("Points: " + points);
                prefs.edit().putInt("points", points).apply();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
        else if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                chosenSongs = intent.getStringArrayExtra("song");
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
                    songNum++;
                    if(songNum<NUM_OF_SONGS){
                        state=0;
                        handleClick();
                    }
                break;
            default:
                break;
        }

        if(songNum==NUM_OF_SONGS){
            //Will be replaced by sth else
            Toast t = Toast.makeText(getApplicationContext(),"END of two songs!", Toast.LENGTH_LONG);
            t.show();

            songNum=0;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
               // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);

        super.onDestroy();
    }
}