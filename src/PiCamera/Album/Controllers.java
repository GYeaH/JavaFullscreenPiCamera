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
    private Vector<String> photoList = new Vector<>();
    private BufferedImage image = null;
    private ImageIcon loadPhoto;
    private boolean infoDisplaySwitcher = false;

    private static String format = "png";

    //default directory
    private static String path = "/home/pi/Pictures/";

    private Vector<String> loadPhotoList(){
        //find format defined files under directory
        File dir = new File(path);

        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + format);
            }
        });

        //add file name to list
        for(int i=0;i<files.length;i++)
            photoList.add(files[i].getName());

        return photoList;
    }

    private BufferedImage loadPhotoImage(int photoIndex){

        String loadPhotoName = photoList.elementAt(photoIndex);

        //image = null;

        try{
            this.image = ImageIO.read(new File(path + loadPhotoName));

            this.loadPhoto = new ImageIcon(image);

        }catch(IOException e){
            //handle exception
        }
        return this.image;
    }

    /**
     * @param photoIndex image index
     * @param label display label, expect JPanel as parent container of passed JLabel     *
     * */
    private void loadThumbnail(int photoIndex, JLabel label){
        //load image
        BufferedImage img = loadPhotoImage(photoIndex);
        double ratio = (double)img.getWidth()/img.getHeight();

        //thumbnail definition
        try{
            //use label's parent panel to define the height of display
            JPanel labelPanel = (JPanel)label.getParent();
            int displayH = labelPanel.getHeight();
            //int displayH = label.getHeight();
            int displayW = (int)(displayH * ratio);
            //update display area according to content
            label.setPreferredSize(new Dimension(displayW,displayH));

            //load thumnail instead of original photo
            Image thumbnail = img.getScaledInstance(displayW,displayH, Image.SCALE_FAST);

            BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),thumbnail.getHeight(null),BufferedImage.TYPE_INT_RGB);
            bufferedThumbnail.getGraphics().drawImage(thumbnail,label.getX(),label.getY(),null);

            this.loadPhoto = new ImageIcon(bufferedThumbnail);

        }catch(ClassCastException e){
            //handle exception
        }
    }

    public void switchToNext(JLabel displayLabel,JLabel infoLabel) {
		/*below are fill in codes*/

        //check photo index value valid
        if(photoIndex == (photoList.size()-1))
            photoIndex = 0;
        else  photoIndex ++;

        //loadPhotoImage(photoIndex);
        loadThumbnail(photoIndex,displayLabel);

        displayLabel.setIcon(loadPhoto);
        displayLabel.repaint();

        //hide infomation label
        turnOffInfo(infoLabel);
    }

    public void switchToPrevious(JLabel displayLabel,JLabel infoLabel) {
		/*below are fill in codes*/

        //check photo index value valid
        if(photoIndex == 0)
            photoIndex = photoList.size()-1;
        else photoIndex --;

        loadThumbnail(photoIndex,displayLabel);

        displayLabel.setIcon(loadPhoto);
        displayLabel.repaint();

        //hide infomation label
        turnOffInfo(infoLabel);
    }

    public void showCurrentIndexedPhoto(JLabel displayLabel){
        //load photo list only when album mode start
        loadPhotoList();

        //load first photo
        loadThumbnail(photoIndex,displayLabel);

        displayLabel.setIcon(loadPhoto);
        displayLabel.repaint();
    }

    /**
     * @param displayLabel label for image display
     * @param infoLabel label for information display which will be visible when delete operation complete
     * */
    public void deleteImage(JLabel displayLabel,JLabel infoLabel){

        String name = photoList.elementAt(photoIndex);

        File file = new File(path + name);

        try{
            displayLabel.setIcon(null);
            file.delete();
            //shows file deletion information after delete operation complete
            String info = "Current Image deleted from directory: " + file.getAbsolutePath();

            turnOnInfo(infoLabel,info);

            //reload photo list
            loadPhotoList();

        }catch (Exception e){
            System.out.println("Delete operation failed");
            System.out.println(e);
        }


    }

    public void saveModifiedImage(BufferedImage modifiedImage, int photoIndex,JLabel infoLabel){

        String name = photoList.elementAt(photoIndex);
        File file = new File(path + name);

        String info;

        try {
            ImageIO.write(modifiedImage,format,file);

            info = "modification saved";

        } catch (IOException e) {
            e.printStackTrace();
            info = "saving of modification not success";
        }

        turnOnInfo(infoLabel,info);

    }

    private BufferedImage rotateImage(BufferedImage image, double angle){
        AffineTransform transform = new AffineTransform();

        transform.rotate(angle, (image.getWidth()/2), (image.getHeight()/2));

        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        return transformOp.filter(image,null);
    }

    public void photoRotationOperation(JLabel displayLabel,JLabel infoLabel){
        BufferedImage photo = loadPhotoImage(photoIndex);

        double angle = Math.toRadians(90);

        photo = rotateImage(photo,angle);

        saveModifiedImage(photo,photoIndex,infoLabel);

        loadThumbnail(photoIndex,displayLabel);

        displayLabel.setIcon(loadPhoto);
        displayLabel.repaint();


    }


    private String photoInfo(int photoIndex){
        String info = null;
        String tempStr = photoList.elementAt(photoIndex);

        try{
            /**
             * use date and resolution as name, like:160728074523_2592_1944.png
             * (yyMMddHHmm s s_WIDTHxHEIGHT.format)
             * */
            String[] nameContent = tempStr.split("[_.]");

            String photoDate = nameContent[0];
            String resolution= nameContent[1] +"x"+ nameContent[2];

            String year = "20" + photoDate.substring(0,2);
            String month = photoDate.substring(2,4);
            String day = photoDate.substring(4,6);
            String hour = photoDate.substring(6,8);
            String min = photoDate.substring(8,10);
            String sec = photoDate.substring(10,12);


            String date=day +"/"+month+"/" + year;
            String time=hour+":"+min+":"+sec;
            info = resolution + "    " + date + "    " + time;

        }catch(Exception e){
            System.out.println("Unable to find image information");
        }
        return info;
    }

    public void displayInfo(JLabel infoLabel){

        String info = photoInfo(photoIndex);

        infoLabel.setBackground(Color.BLACK);

        //toggle display
        if(infoDisplaySwitcher)
            turnOffInfo(infoLabel);
        else
            turnOnInfo(infoLabel,info);
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