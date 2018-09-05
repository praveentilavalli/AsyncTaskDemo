package com.example.adityacomputers.asynctaskdemo;

import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

     EditText etData;
     TextView txtcontent;
    Button btnAdd,btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etData=(EditText)findViewById(R.id.etData);
        txtcontent=(TextView)findViewById(R.id.txtcontent);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isExternalStorageReady()) {
                        String data = etData.getText().toString();
                        String root=getExternalFilesDir(null).toString();
                        File dir=new File(root+"/savedfiles");
                        if(!dir.exists())
                        dir.mkdirs();
                        File file=new File(dir,"test.txt");
                        FileOutputStream fos = new FileOutputStream(file,true);
                        PrintStream ps = new PrintStream(fos);
                        ps.println(data);
                        ps.close();
                        Toast.makeText(MainActivity.this, "Data added successfully to external storage", Toast.LENGTH_SHORT).show();
                        ReadFiles readFiles = new ReadFiles();
                        readFiles.execute("test.txt");
                    }
                    else
                        Toast.makeText(MainActivity.this,"External storage is not ready",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExternalStorageReady())
                {
                  File file=new File(getExternalFilesDir(null).toString()+"/savedfiles/test.txt");
                    if(file.exists()) {
                        file.delete();
                        Toast.makeText(MainActivity.this,"File deleted successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this,"Unable to delete the file",Toast.LENGTH_SHORT).show();
                }
                else
                Toast.makeText(MainActivity.this,"External storage is not ready",Toast.LENGTH_SHORT).show();
            }
        });
    }
    class ReadFiles extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String content="";
            try {
                StringBuffer sb = new StringBuffer();
                if(isExternalStorageReady()) {
                    String root=getExternalFilesDir(null).toString();
                    File dir=new File(root+"/savedfiles");
                    File file=new File(dir,"test.txt");
//                    File file=new File(getExternalFilesDir(null).toString()+"/savedfiles/test.txt");
                    FileInputStream fis =new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    content = sb.toString();
                }
            }
            catch(Exception e){}
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            txtcontent.setText(s);
        }
    }
    public boolean isExternalStorageReady()
    {
        boolean flag=false;
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
            flag=true;
        else
            flag=false;
        return flag;
    }
}
