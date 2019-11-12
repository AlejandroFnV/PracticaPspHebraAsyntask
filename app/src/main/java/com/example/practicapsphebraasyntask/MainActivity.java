package com.example.practicapsphebraasyntask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btHebraAT, btHebra;
    private TextView textView;

    private HebraAT hebraAT;
    private Hebra hebra;
    private boolean started = false;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initEvents();



    }

    private void initComponents() {
        progressBar = findViewById(R.id.progressBar);
        btHebra = findViewById(R.id.btHebra);
        btHebraAT = findViewById(R.id.btHebraAT);
        textView = findViewById(R.id.textView);
        hebraAT = new HebraAT(0);
        hebra = new Hebra();
    }

    private void initEvents() {
        btHebraAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!started){
                    hebraAT.execute();
                    started = true;
                }
                if(hebraAT.getStatus() == AsyncTask.Status.FINISHED){
                    hebraAT = new HebraAT();
                    hebraAT.execute();
                }
            }
        });

        btHebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hebra hebra = new Hebra();
                hebra.start();
            }
        });
    }

    private void lanzarActividad() {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    public void setTextView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            textView.setText("ESPERE 20 SEGUNDOS!");

                    }
                });
            }
        }).start();
    }

    //-----------------ASYNTASK------------------

    class HebraAT extends AsyncTask<String, Integer, Boolean>{
        private int valorInicial;

        HebraAT(int vi){
            valorInicial = vi;
        }

        HebraAT(){
            this(0);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(valorInicial);
        }

        @Override
        public Boolean doInBackground(String... strings) {
            //setTextView();

            for(int i = valorInicial; i < 20; i++){

                Log.v("ASYNTASK", Thread.currentThread().getName() + " " + i);
                publishProgress(i);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    return false;
                }


            }
            lanzarActividad();
            return true;

        }

        @Override
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /*int value = 100;
            value = value / 10;
            if(value > valorInicial){
                progressBar.setProgress(value);
            }*/
            textView.setText(valorInicial + "");
            valorInicial++;



        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                progressBar.setProgress(100);
            }else{
                progressBar.setProgress(0);
            }
        }
    }


    //---------------------THREAD-----------------------

    class Hebra extends Thread{

        //private int valorInicial = 0;

        @Override
        public void run() {
            //progressBar.setProgress(0);
            for(int i = 0; i < 10; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        contador++;
                        textView.setText(contador + "");
                    }
                });
                contador = -1;
            }

            lanzarActividad();

            super.run();
            /*for(int i = valorInicial * 10; i < 1000; i++){
                Log.v("THREAD", Thread.currentThread().getName() + " " + i);
                if(i % 10 == 0){
                    progressBar.setProgress(i);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }*/
        }

    }




}
