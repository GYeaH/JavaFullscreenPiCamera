package PiCamera.Album;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Photo {

    private String name;
    private String info;

    private BufferedImage photoImage;
    private BufferedImage thumbnail;

    private int width;
    private int height;

    //default directory
    private static String path = "/home/pi/Pictures/";

    private static String format = "png";


    //constructor
    public Photo() {
        this.photoImage = null;
        this.name = "";
    }


    public Photo(String name) {
        //do not load photoImage in the beginning to prevent memory usage
        this.photoImage = null;

        this.name = name;

        this.info = setInfo(name);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private String setInfo(String name) {

        try {
            String[] nameContent = name.split("_");

            String photoDate = nameContent[0];

            String resolution = width + "x" + height;

            String year = "20" + photoDate.substring(0, 2);
            String month = photoDate.substring(2, 4);
            String day = photoDate.substring(4, 6);
            String hour = photoDate.substring(6, 8);
            String min = photoDate.substring(8, 10);
            String sec = photoDate.substring(10, 12);


            String date = day + "/" + month + "/" + year;
            String time = hour + ":" + min + ":" + sec;
            info = resolution + "    " + date + "    " + time;

        } catch (Exception e) {
            System.out.println("Unable to find photoImage information");
        }

        return this.info;
    }

    public String getInfo() {
        return info;
    }

    public BufferedImage getPhotoImage() {
        return loadImage(name);
    }


    /**
     *  @param displayLabel display photoDisplayLabel, expect JPanel as parent container of passed JLabel
     */
    public void setThumbnail(JLabel displayLabel) {
        //get uncompressed photoImage first
        this.photoImage = loadImage(name);


        double ratio = (double) width / height;

        //use photoDisplayLabel's parent panel to define the height of display
        JPanel labelPanel = (JPanel)displayLabel.getParent();
        int thumbHeight = labelPanel.getHeight();
        int thumbWidth = (int)(thumbHeight * ratio);

        //update display area according to content
        displayLabel.setPreferredSize(new Dimension(thumbWidth,thumbHeight));


        Image thumbImage = photoImage.getScaledInstance(thumbWidth,thumbHeight,Image.SCALE_FAST);

        this.thumbnail = new BufferedImage(thumbImage.getWidth(null),thumbImage.getHeight(null),BufferedImage.TYPE_INT_RGB);

        this.thumbnail.getGraphics().drawImage(thumbImage, displayLabel.getX(),displayLabel.getY(),null);

    }

    public BufferedImage getThumbnail() {return thumbnail; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static String getPath() {
        return path;
    }

    public static String getFormat() {return format;}



    private BufferedImage loadImage(String name){
        System.out.println("in the loadImage method");
        System.out.println("path: " + path);
        System.out.println("name: " + name);

        BufferedImage loadPhotoImg;

        //load photoImage
        try {
            loadPhotoImg = ImageIO.read(new File(path + name));

            //provide width and height values when photoImage load
            this.width = loadPhotoImg.getWidth();
            this.height = loadPhotoImg.getHeight();


            System.out.println("photoImage resolution: " + width + "x" + height);

        } catch (IOException e) {
            loadPhotoImg = null;
            e.printStackTrace();
            //handle exception
        }

        return loadPhotoImg;
    }


}
