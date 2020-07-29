import dal.MediaMetaData;

public class Main {
    public static void main(String[] args){
        DeviceLoader deviceLoader = new DeviceLoader();
        deviceLoader.displayMixerInfo();
        final MediaMetaData mediaMetaData = new MediaMetaData();
        while(true){
//            System.out.println(mediaMetaData.getVolume());
            System.out.println(mediaMetaData.getFreqArrDiscrete());
//            for(int i = 0; i < mediaMetaData.getFreqArrTransformed().size(); i ++){
//                System.out.println(mediaMetaData.getFreqArrTransformed().get(i));
//                i++;
//            }
//            System.out.println(mediaMetaData.getFreqArrTransformed().size());
        }
    }
}
