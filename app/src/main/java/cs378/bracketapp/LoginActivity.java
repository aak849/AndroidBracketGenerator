package cs378.bracketapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendNewUserInfo(View view) {
        Firebase ref = new Firebase("https://androidbracket.firebaseio.com");
        EditText emailEditText = (EditText) findViewById(R.id.email_address);
        EditText pwEditText = (EditText) findViewById(R.id.password);
        final String userEmail = emailEditText.getText().toString();
        final String userPassword = pwEditText.getText().toString();
        ref.createUser(userEmail, userPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String uid = "" + result.get("uid");
                Log.d(TAG, "Successfully created user account with uid: " + uid);
                addUserToDB(userEmail, uid );
                loginAfterSignUp(userEmail, userPassword);
                //retrievePreviouslyCreatedBrackets(uid);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Log.d(TAG, "New user creation Firebase error");
            }
        });
    }



    public void loginAfterSignUp(String username, String password) {
        Firebase ref = new Firebase("https://androidbracket.firebaseio.com");
        ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d(TAG,"User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                Bundle loginBundle = new Bundle();
                loginBundle.putString("uid", authData.getUid());
                i.putExtra("loginBundle",loginBundle);
                //i.putExtra("uid", authData.getUid());
                startActivity(i);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Log.d(TAG, "Login attempt failed");
            }
        });
    }

    public void onSendLoginInfo(View view) {
        Firebase ref = new Firebase("https://androidbracket.firebaseio.com");
        EditText emailEditText = (EditText) findViewById(R.id.email_address);
        EditText pwEditText = (EditText) findViewById(R.id.password);
        String userEmail = emailEditText.getText().toString();
        String userPassword = pwEditText.getText().toString();
        ref.authWithPassword(userEmail, userPassword, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d(TAG,"User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                Bundle loginBundle = new Bundle();
                loginBundle.putString("uid", authData.getUid());
                i.putExtra("loginBundle",loginBundle);
                //i.putExtra("uid", authData.getUid());
                retrievePreviouslyCreatedBrackets(authData.getUid());

                startActivity(i);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Log.d(TAG, "Login attempt failed");
            }
        });
    }

    public void retrievePreviouslyCreatedBrackets(String uid) {
        Firebase userRef = new Firebase("https://androidbracket.firebaseio.com/users/" + uid + "/brackets");
        final GlobalState globalState = (GlobalState) getApplicationContext();
        globalState.setUid(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> usersBrackets = (Map<String, Object>) snapshot.getValue();
                globalState.setUserBrackets(usersBrackets);
                for(String key : usersBrackets.keySet())
                {
                    //BracketObject bracket = (BracketObject)o;
                    Map<String, Object> eachBracket = (Map<String, Object>) usersBrackets.get(key);
                    Log.d(TAG, "This thing works yuhhhh " + eachBracket.get("numPlayers"));

                }
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void onForgotPassword(View view) {
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
    }

    public void addUserToDB(String email, String uid) {

        Firebase rootRef = new Firebase("https://androidbracket.firebaseio.com/");
        Firebase usersRef = rootRef.child("users");
        usersRef.child(uid).child("email").setValue(email);

//        UserObject user = new UserObject(email);
//        Map<String, UserObject> users = new HashMap<String, UserObject>();
//        users.put(uid, user);
//        usersRef.push().setValue(users);
//        Log.d(TAG, "User added to db");


    }

    public void onLoginAsGuest(View view) {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        Bundle loginBundle = new Bundle();
        loginBundle.putString("uid", "nouid");
        i.putExtra("loginBundle",loginBundle);
        startActivity(i);
    }
}
