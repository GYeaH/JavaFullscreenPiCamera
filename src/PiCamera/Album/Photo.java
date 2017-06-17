package PiCamera.Album;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Photo {

    private String name;
    private String info;

    private BufferedImage photoImage;

    private int width;
    private int height;

    //default directory
    private static String path = "/home/pi/Pictures/";

    //constructor
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
        this.photoImage = loadImage(name);

        return photoImage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage loadImage(String name){
        System.out.println("in the loadImage method");
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
