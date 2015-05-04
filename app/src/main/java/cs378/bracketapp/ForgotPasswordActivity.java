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


public class ForgotPasswordActivity extends ActionBarActivity {
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    public void onSendResetPassword(View view) {
        Firebase ref = new Firebase("https://androidbracket.firebaseio.com");
        TextView resetEmail = (TextView) findViewById(R.id.reset_email_address);
        String email = resetEmail.getText().toString();
        ref.resetPassword(email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password reset email sent
                Log.d(TAG, "Password reset email sent");
                Intent i = new Intent(ForgotPasswordActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                Log.d(TAG, "Password reset email failed to send");
                Intent i = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(i);

            }
        });
    }
}
