package cs378.bracketapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class ChangePasswordActivity extends ActionBarActivity {
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

    public void onSubmitNewPassword(View view) {
        Firebase ref = new Firebase("https://androidbracket.firebaseio.com");
        TextView email = (TextView) findViewById(R.id.current_email_address);
        TextView oldPw = (TextView) findViewById(R.id.oldPassword);
        TextView newPw = (TextView) findViewById(R.id.newPassword);
        String oldPassword = oldPw.getText().toString();
        String newPassword = newPw.getText().toString();
        String emailAddress = email.getText().toString();



        ref.changePassword(emailAddress, oldPassword, newPassword, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
                Log.d(TAG, "Password successfully changed");
                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                Log.d(TAG, "Password change failed");
                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
