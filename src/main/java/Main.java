import dal.MediaMetaData;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        DeviceLoader deviceLoader = new DeviceLoader();
        deviceLoader.displayMixerInfo();
        final MediaMetaData mediaMetaData = new MediaMetaData();
        while(true){
            System.out.println(mediaMetaData.getVolume());
            System.out.println(mediaMetaData.getFreqArr());
        }
    }
}
