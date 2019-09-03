package com.example.dados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button ouvir;
    TextView textResult;
    TextView text;
    TextToSpeech speech;
    TextView result;
    int valor = 1;
    int dado = 4;
    private final int texto_voz = 200;
    MediaPlayer mediaPlayer;
    String historico[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = (TextView) findViewById(R.id.textResult);
        ouvir = (Button) findViewById(R.id.ouvir);
        text = (TextView) findViewById(R.id.text);
        result = (TextView) findViewById(R.id.result);
        mediaPlayer = MediaPlayer.create(this, R.raw.dados);

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    speech.setLanguage(Locale.getDefault());

                }
            }
        });
        result.setText("D4");
        textResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (valor) {
                    case 1:
                        speech.speak("D6", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D6");
                        dado = 6;
                        valor += 1;
                        break;
                    case 2:
                        speech.speak("D8", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D8");
                        dado = 8;
                        valor += 1;
                        break;
                    case 3:
                        speech.speak("D10", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D10");
                        dado = 10;
                        valor += 1;
                        break;
                    case 4:
                        speech.speak("D12", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D12");
                        dado = 12;
                        valor += 1;
                        break;
                    case 5:
                        speech.speak("D20", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D20");
                        dado = 20;
                        valor += 1;
                        break;
                    case 6:
                        speech.speak("D100", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D100");
                        dado = 100;
                        valor += 1;
                        break;
                    case 7:
                        speech.speak("D4", TextToSpeech.QUEUE_FLUSH, null);
                        result.setText("D4");
                        dado = 4;
                        valor = 1;
                        break;
                }

            }
        });

        ouvir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                speech.speak("Olá! sou Samantha,a sua assistente", TextToSpeech.QUEUE_ADD, null);
//                speech.speak("clique para mudar os dados", TextToSpeech.QUEUE_ADD, null);
//                speech.speak("Pressione para falar a quantidade de dados", TextToSpeech.QUEUE_ADD, null);
//                speech.speak("ê dois clique caso seja apenas um dado", TextToSpeech.QUEUE_ADD, null);
                  speech.speak(Arrays.toString(historico), TextToSpeech.QUEUE_ADD, null);
            }
        });

        textResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent voz = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                voz.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                voz.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                voz.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale algo normalmente");

                try {
                    startActivityForResult(voz, texto_voz);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Seu celular não tem suporte para comando de voz",
                            Toast.LENGTH_SHORT).show();
                    speech.speak("Seu celular não tem suporte para comando de voz", TextToSpeech.QUEUE_ADD, null);

                }
                return false;
            }
        });
    }

    protected void onActivityResult(int id, int resultCodeId, Intent dados){
        super.onActivityResult(id, resultCodeId, dados);

        switch (id){

            case texto_voz:
                if(resultCodeId == RESULT_OK && null != dados){

                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String quantDice = result.get(0);

                    speech.speak("você falou "+quantDice, TextToSpeech.QUEUE_FLUSH, null);

                    String dice[] = Random(quantDice);
                    int total = Soma(dice);
                    text.setText(Arrays.toString(dice));
                    try {
                        new Thread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    try {
                        new Thread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    speech.speak("resultado " + total, TextToSpeech.QUEUE_ADD, null);
                    speech.speak("resultado de cada dado ", TextToSpeech.QUEUE_ADD, null);
                    speech.setSpeechRate((float)0.8);
                    speech.speak(Arrays.toString(dice), TextToSpeech.QUEUE_ADD, null);
                    speech.setSpeechRate((float)1.0);
//                    historico.add(Integer.parseInt(Integer.parseInt(quantDice+"D"+dado) + "resultado"+total)+
//                            "resultado de cada dado"+Arrays.toString(dice) );

                    historico = Historico(quantDice, dado, dice);
                }
                break;
        }
    }
    public String[] Historico(String quantDice, int dado, String[] dice){
        String hist[] = new String[1];
        String dd = String.valueOf(dado);
        hist[0] = quantDice;
        return hist;
    }
    public String[] Random(String quant){
        Random random = new Random();
        int num = Integer.parseInt(quant);
        String quantDice[] = new String[num];
        for(int i = 0; i < num ; i++){
            quantDice[i] = String.valueOf(random.nextInt(dado)+1);
        }
        return quantDice;
    }

    public int Soma(String[] dice){
        int soma = 0;
        for(int i = 0; i < dice.length ; i++){
            soma += Integer.parseInt(dice[i]);
        }
        return soma;
    }
//    public void onPause(){
//        if (speech != null){
//            speech.stop();
//            speech.shutdown();
//        }
//        super.onPause();
//    }

    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }
}

