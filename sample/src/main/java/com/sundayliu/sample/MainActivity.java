package com.sundayliu.sample;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start_hello).setOnClickListener(this);
        findViewById(R.id.btn_start_one).setOnClickListener(this);

        try{
            Object obj = getSystemService(Context.ACTIVITY_SERVICE);
            if (obj != null){
                Log.d("gameparser",obj.getClass().getName());
                /*
                Class<?> clz = obj.getClass();
                Method[] methods = clz.getMethods();
                for (Method m:methods){
                    String methodName = m.getName();
                    Log.d("gameparser","method name:" + methodName);
                }
                */
            }

            Class<?> clz = Class.forName("android.app.IActivityManager$Stub$Proxy");
            //Class<?> clz = Class.forName("java.lang.String");
            Method[] methods = clz.getMethods();
            for (Method m:methods){
                String methodName = m.getName();
                if (methodName.equals("startActivity")){
                    Log.d("gameparser","method name:" + methodName);
                    Log.d("gameparser","method:" + m);
                    Class<?>[] paraTypes = m.getParameterTypes();
                    for(Class<?> p:paraTypes){
                        Log.d("gameparser","parameter name:" + p.getName());
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_start_hello:
            {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.sundayliu.hello");
                if (intent != null){
                    startActivity(intent);
                }
            }
                break;
            case R.id.btn_start_one:
            {
                Intent intent = new Intent(this,OneActivity.class);
                startActivity(intent);
            }
                break;
            default:
                break;
        }

    }
}
