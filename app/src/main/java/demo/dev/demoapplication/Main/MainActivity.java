package demo.dev.demoapplication.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.dev.demoapplication.R;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
