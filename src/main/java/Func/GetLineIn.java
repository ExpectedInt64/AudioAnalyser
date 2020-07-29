package Func;

import dal.MediaMetaData;

import javax.sound.sampled.*;
import java.io.*;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class GetLineIn {

    public GetLineIn(){

    }

    // record duration, in milliseconds
    static final long RECORD_TIME = 10000;  // 1 minute

    // path of the wav file
    File wavFile = new File("O:/Test/RecordAudio.wav");

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;
    TargetDataLine targetDataLine;
    Mixer mixer;


    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 44000;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    /**
     * Captures the sound and record into a WAV file
     */
    void start() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        Mixer currentMixer = AudioSystem.getMixer(mixerInfo[42]);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);
        targetDataLine = null;
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        targetDataLine.close();

        dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        try {
            targetDataLine = (TargetDataLine) currentMixer.getLine(dataLineInfo);
            targetDataLine.open(getAudioFormat());
            targetDataLine.start();


            byte[] buffer = new byte[20000];
            System.out.println(buffer.length);
            AudioInputStream ais = new AudioInputStream(targetDataLine);


            System.out.println("Start capture...");
            // start recording
            //AudioSystem.write(ais, fileType, wavFile);
            //MediaMetaData mediaMetaData = new MediaMetaData();

            while (true){
                targetDataLine.read(buffer, 0, buffer.length);
                System.out.print(calculateRMSLevel(buffer));
                System.out.println(" - "+buffer[100]);
            }




        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    /*void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    void finish() {
        targetDataLine.stop();
        targetDataLine.close();
        System.out.println("Finished");
    }

    /**
     * Entry to run the program
     */
    public static void main(String[] args) {
        final GetLineIn getLineIn = new GetLineIn();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
//                try {
//                    Thread.sleep(RECORD_TIME);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                getLineIn.finish();
            }
        });

        stopper.start();

        // start recording
        getLineIn.start();
    }


    private int calculateRMSLevel(byte[] audioData) {
        // audioData might be buffered data read from a data line

        long lSum = 0;
        for (int i = 0; i < audioData.length; i++) {
            lSum = lSum + audioData[i];
        }
        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0d;
        for (int j = 0; j < audioData.length; j++) {
            sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);
        }
        double averageMeanSquare = sumMeanSquare / audioData.length;
        return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
    }

}