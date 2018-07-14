package info.loveai.elfviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import info.loveai.jni.Native;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(Native.stringFromJNI());

        findViewById(R.id.start).setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()){
            default:break;
            case R.id.start:
                startTest();
                break;
        }
    }

    private void startTest(){

    }
}
