package com.example.zt.myhotfix_mutidex;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zt.myhotfix_mutidex.FixUtils.FixDexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn);
        Button buttonFix = (Button) findViewById(R.id.btn_fix);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTestClass myTestClass = new MyTestClass();
                myTestClass.testFix(MainActivity.this);
            }
        });

        buttonFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fixBug();
                //Toast.makeText(MainActivity.this,"恭喜你！修复成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fixBug() {
        //目录：/data/data/packageName/odex
        File fileDir = getDir(MyConstants.DEX_DIR, Context.MODE_PRIVATE);
        //往该目录下面放置我们修复好的dex文件。
        String name = "classes2.dex";
        String filePath = fileDir.getAbsolutePath() + File.separator + name;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        //搬家：把下载好的在SD卡里面的修复了的classes2.dex搬到应用目录filePath
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + name);
            os = new FileOutputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            File f = new File(filePath);
            if (f.exists()) {
                Toast.makeText(this, "dex 重写成功", Toast.LENGTH_SHORT).show();
            }
            //热修复
            FixDexUtils.loadFixedDex(this);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
