import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.util.Scanner;

public class DeviceLoader {
    static Mixer.Info [] mixersInfo = AudioSystem.getMixerInfo();
    Mixer mixer;
    TargetDataLine audioLine;
    AudioFormat finalFormat = new AudioFormat(44100, 16, 2, true, true);
    public DeviceLoader() {

    }
    public void setMixer(int i) throws LineUnavailableException {
        mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[i]);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,finalFormat);
        audioLine = (TargetDataLine) mixer.getLine(info);
        audioLine.open(finalFormat, 512);
        audioLine.start();
    }

    public TargetDataLine getAudioLine(){
        return audioLine;
    }

   /* public TargetDataLine getMixerOutput(){
        return AudioSystem.getTargetDataLine(mixer.getSourceLineInfo());
    }*/

    public static void displayMixerInfo()
    {

        int i = 0;
        for (Mixer.Info mixerInfo : mixersInfo)
        {
            System.out.println("Mixer "+i+": " + mixerInfo.getName());
            i++;
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Line.Info [] sourceLineInfo = mixer.getSourceLineInfo();
            for (Line.Info info : sourceLineInfo)
                showLineInfo(info);

            Line.Info [] targetLineInfo = mixer.getTargetLineInfo();
            for (Line.Info info : targetLineInfo)
                showLineInfo(info);
        }
    }


    private static void showLineInfo(final Line.Info lineInfo)
    {
        System.out.println("  " + lineInfo.toString());

        if (lineInfo instanceof DataLine.Info)
        {
            DataLine.Info dataLineInfo = (DataLine.Info)lineInfo;

            AudioFormat [] formats = dataLineInfo.getFormats();
            for (final AudioFormat format : formats)
                System.out.println("    " + format.toString());
        }
    }
}
