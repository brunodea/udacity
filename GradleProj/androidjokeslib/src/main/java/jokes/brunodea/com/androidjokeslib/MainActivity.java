package jokes.brunodea.com.androidjokeslib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_JOKE = "extra_joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidjokeslib_activity_main);
        Intent i = getIntent();
        if (i != null && i.hasExtra(EXTRA_JOKE)) {
            TextView tv = findViewById(R.id.androidjokeslib_tv_joke);
            tv.setText(i.getStringExtra(EXTRA_JOKE));
        }
    }
}
