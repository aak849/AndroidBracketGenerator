package cs378.bracketapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Firebase.setAndroidContext(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onGetBracketClick(View view) {
        Intent getBracketScreenIntent = new Intent(this, BracketScreen.class);

        final int result = 1;
        Bundle loginBundle = getIntent().getBundleExtra("loginBundle");
        getBracketScreenIntent.putExtra("loginBundle", loginBundle);

        startActivity(getBracketScreenIntent);

    }

    public void onGetHelpClick(View view) {
        Intent getHelpScreenIntent = new Intent(this, HelpScreen.class);

        startActivity(getHelpScreenIntent);

    }
}
