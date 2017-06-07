package PiCamera.panels;

import PiCamera.Album.Controllers;
import PiCamera.Camera.Preview;
import PiCamera.Camera.Shutter;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by YeaH on 1/23/2017.
 */
public class SwingGUI {
    private JPanel cardsPanel;
    private JPanel menuPanel;
    private JPanel photoPanel;
    private JPanel albumPanel;
    private JPanel exitButtonPanel;
    private JPanel photoButtonPanel;
    private JButton menu_photoButton;
    private JButton exitButton;
    private JPanel albumButtonPanel;
    private JButton menu_albumButton;
    private JPanel effectsPanel;
    private JPanel previewPanel;
    private JPanel photoPanelControllPanel;
    private JLabel effectsLabel;
    private JComboBox effectsComboBox;
    private JButton photo_menuButton;
    private JPanel albumPanelControllPanel;
    private JButton album_menuButton;
    private JPanel previousButtonPanel;
    private JPanel nextButtonPanel;
    private JButton previousButton;
    private JButton nextButton;
    private JPanel viewPanel;
    /****************************************/
    private JPanel northenControlPanel;
    private JPanel photoNorthernPanel;
    private JLabel previewLabel;
    private JPanel albumNorthenPanel;
    private JButton rotationButton;
    private JButton deleteButton;
    private JLabel infoArea;
    private JPanel photoPanelExitPanel;
    private JButton photoPanelExitButton;
    private JPanel albumPanelExitPanel;
    private JButton albumPanelExitButton;
    private JPanel photoDisplayPanel;
    private JLabel photoDisplayLabel;


    /*global variables later need to be moved*/
    public static String[] effectList = {"NONE","NEGATIVE","SKETCH","EMBOSS","OILPAINT","CARTOON"};
    static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    static Controllers controllers = new Controllers();


    public static void main(String[] args) {
        JFrame frame = new JFrame("Pi Camera");
        frame.setContentPane(new SwingGUI().cardsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setVisible(true);

        //disable resizability
        frame.setResizable(false);

        //update configurations
        SwingUtilities.updateComponentTreeUI(frame);

    }

    public SwingGUI() {

        //load cardsPanel layout
        final CardLayout c = (CardLayout) cardsPanel.getLayout();
        //load layout configurations
        layoutConfig();
        //load UI configurations
        uiManagerConfig();

        //service registrations
        final Shutter shutter = new Shutter();
        final Preview preview = new Preview(previewLabel);
        //put preview to another thread
        final Thread previewThread = new Thread(preview);

        //buttons actions
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.exit(0);
            }
        });

        menu_photoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                c.show(cardsPanel,"photoPanelCard");

                previewLabel.setText("<html><p>preview will be displayed at here<br>tap HERE to take a photo</p></html>");
                previewLabel.setForeground(Color.white);
            }
        });
        menu_albumButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                c.show(cardsPanel,"albumPanelCard");
                //load photo when album panel shows
                controllers.showCurrentIndexedPhoto(photoDisplayLabel);
            }
        });
        photo_menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                c.show(cardsPanel,"menuPanelCard");
                //disable preview when photo panel switched
                preview.disablePreview();
                //previewThread.interrupt();
            }
        });
        album_menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                c.show(cardsPanel,"menuPanelCard");
            }
        });

        /******************** PHOTO PANEL ************************/
        effectsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEffect = (String)effectsComboBox.getSelectedItem();
                shutter.setImageEffect(selectedEffect);
            }
        });

        //shoot still photo when tap preview
        previewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                previewLabel.setText("preview taped");
                shutter.shootStill();
            }
        });

        /******************** ALBUM PANEL ************************/
        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                //photoDisplayArea.setText("> button pressed");
                controllers.switchToNext(photoDisplayLabel,infoArea);
            }
        });
        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                //photoDisplayArea.setText("< button pressed");
                controllers.switchToPrevious(photoDisplayLabel,infoArea);
            }
        });
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                controllers.deletePhoto(photoDisplayLabel,infoArea);
            }
        });

        photoDisplayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                //display info
                controllers.displayInfo(infoArea);
            }
        });
        albumPanelExitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.exit(0);
            }
        });
        photoPanelExitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.exit(0);
            }
        });
        rotationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                controllers.photoRotationOperation(photoDisplayLabel,infoArea);
            }
        });
    }

    private void layoutConfig(){

        Rectangle displayBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        int displayW = displayBounds.width;
        int displayH = displayBounds.height;

        cardsPanel.setPreferredSize(new Dimension(displayW, displayH));

        /******************** MENU PANEL ************************/
        exitButtonPanel.setPreferredSize(new Dimension(displayW,50));
        int exitPanelH = 80;

        System.out.println(exitPanelH);

        int northernPanelH = displayH - exitPanelH;
        System.out.println(northernPanelH);

        northenControlPanel.setPreferredSize(new Dimension(displayW,northernPanelH));
        photoButtonPanel.setPreferredSize(new Dimension((displayW/2),northernPanelH));
        albumButtonPanel.setPreferredSize(new Dimension((displayW/2),northernPanelH));

        //components' colors
        exitButton.setBackground(Color.darkGray);
        exitButton.setForeground(Color.white);
        menu_photoButton.setBackground(Color.white);
        menu_photoButton.setForeground(Color.BLACK);
        menu_albumButton.setBackground(Color.white);
        menu_albumButton.setForeground(Color.BLACK);

        /******************** PHOTO PANEL ************************/
        photoNorthernPanel.setPreferredSize(new Dimension(displayW,northernPanelH));

        effectsPanel.setPreferredSize(new Dimension(displayW,50));

        //set effects model to combobox

        DefaultComboBoxModel effectsModel = new DefaultComboBoxModel(effectList);
        effectsComboBox.setModel(effectsModel);

        int previewH = photoNorthernPanel.getHeight() - effectsPanel.getHeight();
        previewPanel.setPreferredSize(new Dimension(displayW,previewH));
        System.out.println(previewH);

        photoPanelControllPanel.setPreferredSize(new Dimension(displayW,exitPanelH));

        photoPanelExitPanel.setPreferredSize(new Dimension(exitPanelH,exitPanelH));
        photoPanelExitButton.setPreferredSize(photoPanelExitPanel.getPreferredSize());

        //components' colors
        photo_menuButton.setBackground(Color.blue);
        photo_menuButton.setForeground(Color.white);
        photoPanelExitButton.setBackground(Color.darkGray);
        photoPanelExitButton.setForeground(Color.white);
        effectsComboBox.setBackground(Color.white);
        effectsComboBox.setForeground(Color.black);
        effectsPanel.setBackground(Color.white);
        previewPanel.setBackground(Color.black);
        previewLabel.setBackground(Color.black);

        /******************** ALBUM PANEL ************************/
        albumNorthenPanel.setPreferredSize(new Dimension(displayW,exitPanelH));
        rotationButton.setPreferredSize(new Dimension((displayW/2),exitPanelH));
        deleteButton.setPreferredSize(new Dimension((displayW/2),exitPanelH));

        int albumCentreH = northernPanelH - exitPanelH - exitPanelH;

        int buttonW = 60;
        previousButtonPanel.setPreferredSize(new Dimension(buttonW, albumCentreH));
        nextButtonPanel.setPreferredSize(new Dimension(buttonW, albumCentreH));

        int viewW = displayW - buttonW - buttonW;

        viewPanel.setPreferredSize(new Dimension(viewW, albumCentreH));

        albumPanelControllPanel.setPreferredSize(new Dimension(displayW,exitPanelH));

        albumPanelExitPanel.setPreferredSize(new Dimension(exitPanelH,albumPanelControllPanel.getHeight()));
        albumPanelExitButton.setPreferredSize(albumPanelExitPanel.getPreferredSize());

        //components' colors
        rotationButton.setBackground(Color.white);
        rotationButton.setForeground(Color.BLACK);
        deleteButton.setBackground(Color.white);
        deleteButton.setForeground(Color.BLACK);
        nextButton.setBackground(Color.black);
        nextButton.setForeground(Color.white);
        previousButton.setBackground(Color.black);
        previousButton.setForeground(Color.white);
        album_menuButton.setBackground(Color.blue);
        album_menuButton.setForeground(Color.white);
        albumPanelExitButton.setBackground(Color.darkGray);
        albumPanelExitButton.setForeground(Color.white);

    }

    private void uiManagerConfig(){
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        }catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }catch (ClassNotFoundException e) {
            // handle exception
        }catch (InstantiationException e) {
            // handle exception
        }catch (IllegalAccessException e) {
            // handle exception
        }

        UIManager.put("Button.font", new FontUIResource("Dialog",Font.BOLD,16));
        UIManager.put("Label.font", new FontUIResource("Dialog",Font.BOLD,16));

    }


}
