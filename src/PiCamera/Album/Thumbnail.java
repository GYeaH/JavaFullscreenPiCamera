package PiCamera.Album;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YeaH on 6/7/2017.
 */
public class Thumbnail {

    private BufferedImage photoImage;
    private BufferedImage thumbnailImage;

    private String name;
    private Photo photoObj;

    private int imageHeight = 0;
    private int imageWidth = 0;

    private int thumbHeight = 0;
    private int thumbWidth = 0;

    private ImageIcon loadPhoto;

    /**
     *  @param photoObj the object of photo, which thumbnail targeted on
     */
    public Thumbnail(Photo photoObj){

        this.photoObj = photoObj;
        this.name = photoObj.getName();

        imageWidth = photoObj.getWidth();
        imageHeight = photoObj.getHeight();
    }


    /**
     *  @param displayLabel display photoDisplayLabel, expect JPanel as parent container of passed JLabel
     */
    public void setThumbnail(JLabel displayLabel) {
        //load uncompressed photoImage first
        this.photoImage = photoObj.loadImage(name);


        double ratio = (double) imageWidth / imageHeight;

        //use photoDisplayLabel's parent panel to define the height of display
        JPanel labelPanel = (JPanel)displayLabel.getParent();
        this.thumbHeight = labelPanel.getHeight();
        this.thumbWidth = (int)(thumbHeight * ratio);

        //update display area according to content
        displayLabel.setPreferredSize(new Dimension(thumbWidth,thumbHeight));


        Image thumbImage = photoImage.getScaledInstance(thumbWidth,thumbHeight,Image.SCALE_FAST);

        this.thumbnailImage = new BufferedImage(thumbImage.getWidth(null),thumbImage.getHeight(null),BufferedImage.TYPE_INT_RGB);

        thumbnailImage.getGraphics().drawImage(thumbImage, displayLabel.getX(),displayLabel.getY(),null);

    }

    public BufferedImage getThumbnail() {return thumbnailImage; }

    /**
     *  @param photoDisplayLabel display photoDisplayLabel, expect JPanel as parent container of passed JLabel
     */
    public void loadThumbnailtoLabel(JLabel photoDisplayLabel){

        Thumbnail thumbnail = new Thumbnail(photoObj);

        //set thumbnail first
        thumbnail.setThumbnail(photoDisplayLabel);
        //get thumbnail after set
        BufferedImage bufferedThumbnail = thumbnail.getThumbnail();

        this.loadPhoto = new ImageIcon(bufferedThumbnail);

        photoDisplayLabel.setIcon(loadPhoto);
        photoDisplayLabel.repaint();

    }



}
