package dal;

import com.tagtraum.jipes.math.*;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MediaMetaData {
    int volume;
    List<Float> freqArr = new ArrayList<Float>();
    //float[] freqArr;
    int input = 42;
    TargetDataLine targetDataLine;
    private static final float NORMALIZATION_FACTOR_2_BYTES = Short.MAX_VALUE + 1.0f;


    public MediaMetaData(){

    }
    public List<Float> getFreqArr(){
        updateFreqArr();
        return freqArr;
    }
    private void updateFreqArr(){
        freqArr.clear();
        float[] tempArr = getSamples();
        for(int i = 0; i < tempArr.length;i++){
            freqArr.add(tempArr[i]);
        }
    }

    public int getVolume() {
        updateVolume();
        return volume;
    }

    public void updateVolume() {
        this.volume = calculateRMSLevel(getSoundData());
    }

    /**
     * Captures the sound from chosen input
     */
    private byte[] getSoundData() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        byte[] buffer = new byte[4096];
        Mixer currentMixer = AudioSystem.getMixer(mixerInfo[input]);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        Line.Info targetDLInfo = new Line.Info(TargetDataLine.class);
        targetDataLine = null;
//        final int numberOfSamples = buffer.length / getAudioFormat().getFrameSize();
//        final FFTFactory.JavaFFT fft = new FFTFactory.JavaFFT(numberOfSamples);
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
            buffer = new byte[4096];
            targetDataLine.read(buffer, 0, buffer.length);
//            final float[] samples = decode(buffer, getAudioFormat());
//            final float[][] transformed = fft.transform(samples);
//            final float[] realPart = transformed[0];
//            final float[] imaginaryPart = transformed[1];
//            final double[] magnitudes = toMagnitudes(realPart, imaginaryPart);
//            System.out.println(samples[52]);

            targetDataLine.stop();
            targetDataLine.close();


        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
        return buffer;

    }

    private float[] getSamples(){
        final float[] samples = decode(getSoundData(), getAudioFormat());
        return samples;
    }

    private static float[] decode(final byte[] buf, final AudioFormat format) {
        final float[] fbuf = new float[buf.length / format.getFrameSize()];
        for (int pos = 0; pos < buf.length; pos += format.getFrameSize()) {
            final int sample = format.isBigEndian()
                    ? byteToIntBigEndian(buf, pos, format.getFrameSize())
                    : byteToIntLittleEndian(buf, pos, format.getFrameSize());
            // normalize to [0,1] (not strictly necessary, but makes things easier)
            fbuf[pos / format.getFrameSize()] = sample / NORMALIZATION_FACTOR_2_BYTES;
        }
        return fbuf;
    }

    private static double[] toMagnitudes(final float[] realPart, final float[] imaginaryPart) {
        final double[] powers = new double[realPart.length / 2];
        for (int i = 0; i < powers.length; i++) {
            powers[i] = Math.sqrt(realPart[i] * realPart[i] + imaginaryPart[i] * imaginaryPart[i]);
        }
        return powers;
    }

    private static int byteToIntLittleEndian(final byte[] buf, final int offset, final int bytesPerSample) {
        int sample = 0;
        for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
            final int aByte = buf[offset + byteIndex] & 0xff;
            sample += aByte << 8 * (byteIndex);
        }
        return sample;
    }

    private static int byteToIntBigEndian(final byte[] buf, final int offset, final int bytesPerSample) {
        int sample = 0;
        for (int byteIndex = 0; byteIndex < bytesPerSample; byteIndex++) {
            final int aByte = buf[offset + byteIndex] & 0xff;
            sample += aByte << (8 * (bytesPerSample - byteIndex - 1));
        }
        return sample;
    }

    /**
     * Defines an audio format
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 44000;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
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