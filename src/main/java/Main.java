import dal.MediaMetaData;

public class Main {
    public static void main(String[] args) {
        DeviceLoader deviceLoader = new DeviceLoader();
        deviceLoader.displayMixerInfo();
        final MediaMetaData mediaMetaData = new MediaMetaData();

//            System.out.println(mediaMetaData.getVolume());
        System.out.println(mediaMetaData.getFreqArrDiscrete());
        System.out.println(mediaMetaData.getFreqArrTransformed());
        System.out.println(mediaMetaData.getFreqArrRealPart());
        System.out.println(mediaMetaData.getFreqArrImagPart());
        System.out.println(mediaMetaData.getFreqArrMagnitudes());
//            for(int i = 0; i < mediaMetaData.getFreqArrTransformed().size(); i ++){
//                System.out.println(mediaMetaData.getFreqArrTransformed().get(i));
//                i++;
//            }
//            System.out.println(mediaMetaData.getFreqArrTransformed().size());

    }
}
