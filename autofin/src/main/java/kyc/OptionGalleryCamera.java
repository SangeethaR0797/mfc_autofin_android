package kyc;

public class OptionGalleryCamera {
    private static OptionGalleryCamera mInstance = null;
    private String whichImage;

    private OptionGalleryCamera(){
        whichImage = "";

    }

    public static OptionGalleryCamera getInstance(){
        if(mInstance == null)
        {
            mInstance = new OptionGalleryCamera();
        }
        return mInstance;
    }

    public String getWhichImage() {
        return whichImage;
    }

    public void setWhichImage(String whichImage) {
        this.whichImage = whichImage;
    }
}
