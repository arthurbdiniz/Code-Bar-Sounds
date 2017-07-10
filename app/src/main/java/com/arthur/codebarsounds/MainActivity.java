package com.arthur.codebarsounds;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final int duration = 1; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    //private double freqOfTone; // hz
    private int indexNote = 1;









    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



            genTone(getNote(indexNote));
            indexNote++;
            // Use a new tread as this can take a while
            final Thread thread = new Thread(new Runnable() {
                public void run() {



                    handler.post(new Runnable() {

                        public void run() {
                            playSound();

                        }
                    });


                }
            });
            thread.start();








        Button button = (Button)findViewById(R.id.play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


/*
        genTone(C);


            // Use a new tread as this can take a while
            final Thread thread = new Thread(new Runnable() {
                public void run() {



                    handler.post(new Runnable() {

                        public void run() {
                            playSound();
                        }
                    });


                }
            });
            thread.start();

*/

    }

    double getNote(int index){
        double C = 261.626;
        double D = 293.665;
        double E = 329.628;
        double F = 349.228;
        double G = 391.995;
        double A = 440.000;
        double B = 493.883;


        switch (index){

            case 1:
                return C;

            case 2:
                return D;

            case 3:
                return E;

            case 4:
                return F;

            case 5:
                return G;

            case 6:
                return A;

            case 7:
                return B;


        }
        return C;
    }

    void genTone(double freqOfTone){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
}
