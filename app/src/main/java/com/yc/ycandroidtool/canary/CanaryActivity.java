
package com.yc.ycandroidtool.canary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.ycandroidtool.R;

public class CanaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canary);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, DemoFragment.newInstance())
                .commit();

        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTipDialog();
            }
        });
    }

    private void showTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tip");
        builder.setMessage(getResources().getString(R.string.hello_world));
        builder.setNegativeButton("ok", null);
        builder.show();
    }

}
