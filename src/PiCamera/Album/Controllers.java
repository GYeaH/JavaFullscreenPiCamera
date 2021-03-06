package PiCamera.Album;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

/**
 * photos in directory
 */

public class Controllers{


    private int photoIndex = 0;

    private boolean infoDisplaySwitcher = false;

    //default directory
    public static String lookupPath = "img/".replace("/",File.separator);
    public static String lookupFormat = "png";

    /*** AFTER PHOTO CLASS ADD ***/
    //Photo photoObj = new Photo();

    private Vector<Photo> photoList = new Vector<>();

    private Vector<Photo> loadPhotoList(){
        //find format defined files under directory
        File dir = new File(lookupPath);

        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + lookupFormat);
            }
        });

        //add photo object to list
        for(int i=0;i<files.length;i++){
            String photoName = files[i].getName();
            photoList.add(new Photo(photoName));
        }

        return photoList;
    }

    public void displayInfo(JLabel infoLabel){

        //String info = photoInfo(photoIndex);
        Photo currentPhoto = photoList.elementAt(photoIndex);
        String info = currentPhoto.getInfo();

        infoLabel.setBackground(Color.BLACK);

        //toggle display
        if(infoDisplaySwitcher)
            turnOffInfo(infoLabel);
        else
            turnOnInfo(infoLabel,info);
    }


    /**
     * @param displayLabel label displays photo
     * @param infoLabel label for information display which will be visible when delete operation complete
     * */

    public void deletePhoto(JLabel displayLabel, JLabel infoLabel){
        String info;
        String name = photoList.elementAt(photoIndex).getName();

        File file = new File(lookupPath + name);

        try{
            displayLabel.setIcon(null);

            file.delete();

            //reload photo list
            loadPhotoList();
            //display previous photo
            switchToPrevious(displayLabel,infoLabel);

            info = "Photo deleted from directory: " + file.getAbsolutePath();

        }catch (Exception e){
            info = "Delete operation failed";

            System.out.println(info);
            System.out.println(e);
        }

        //display file deletion information after operation
        turnOnInfo(infoLabel,info);

    }

    public void saveModifiedPhoto(BufferedImage modifiedPhoto, int photoIndex, JLabel infoLabel){

        String name = photoList.elementAt(photoIndex).getName();
        File file = new File(lookupPath + name);

        String info;

        try {
            ImageIO.write(modifiedPhoto,lookupFormat,file);

            info = "modification saved";

        } catch (IOException e) {
            e.printStackTrace();
            info = "saving of modification not success";
        }

        turnOnInfo(infoLabel,info);

    }


    public void photoRotationOperation(JLabel displayLabel,JLabel infoLabel){
        //public void photoRotationOperation(JPanel displayPanel,JLabel infoLabel){

        Photo currentPhotoObj = photoList.elementAt(photoIndex);

        BufferedImage currentPhotoImg = currentPhotoObj.getPhotoImage();

        double angle = Math.toRadians(90);

        currentPhotoImg = rotatePhoto(currentPhotoImg,angle);

        saveModifiedPhoto(currentPhotoImg,photoIndex,infoLabel);


        Photo photoObj = photoList.elementAt(photoIndex);
        photoObj.getPhotoImage();
        Thumbnail thumbnail = new Thumbnail(photoObj);

        thumbnail.loadThumbnailtoLabel(displayLabel);

    }

    public void switchToNext(JLabel displayLabel,JLabel infoLabel) {
		/*below are fill in codes*/

        //check photo index value valid
        if(photoIndex == (photoList.size()-1))
            photoIndex = 0;
        else  photoIndex ++;


        Photo photoObj = photoList.elementAt(photoIndex);
        photoObj.getPhotoImage();
        Thumbnail thumbnail = new Thumbnail(photoObj);

        thumbnail.loadThumbnailtoLabel(displayLabel);

        //hide information
        turnOffInfo(infoLabel);
    }


    public void switchToPrevious(JLabel displayLabel,JLabel infoLabel) {
		/*below are fill in codes*/

        //check photo index value valid
        if(photoIndex == 0)
            photoIndex = photoList.size()-1;
        else photoIndex --;


        Photo photoObj = photoList.elementAt(photoIndex);
        photoObj.getPhotoImage();
        Thumbnail thumbnail = new Thumbnail(photoObj);

        thumbnail.loadThumbnailtoLabel(displayLabel);


        //hide information photoDisplayLabel
        turnOffInfo(infoLabel);
    }

    public void showCurrentIndexedPhoto(JLabel displayLabel){
        //load photo list only when album mode start
        loadPhotoList();

        //load first photo
        Photo photoObj = photoList.elementAt(photoIndex);
        photoObj.getPhotoImage();
        Thumbnail thumbnail = new Thumbnail(photoObj);

        thumbnail.loadThumbnailtoLabel(displayLabel);
    }


    private BufferedImage rotatePhoto(BufferedImage photoImg, double angle){
        AffineTransform transform = new AffineTransform();

        transform.rotate(angle, (photoImg.getWidth()/2), (photoImg.getHeight()/2));

        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        return transformOp.filter(photoImg,null);
    }

    private void turnOffInfo(JLabel infoLabel){
        infoLabel.setText(null);
        infoLabel.setVisible(false);

        infoDisplaySwitcher = false;
    }

    private void turnOnInfo(JLabel infoLabel, String info){
        infoLabel.setText(info);
        infoLabel.setVisible(true);

        infoDisplaySwitcher = true;
    }


}