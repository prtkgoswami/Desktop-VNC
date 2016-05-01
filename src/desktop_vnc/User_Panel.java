package desktop_vnc;
// MINE
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jdk.management.resource.ResourceApprover;


import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

public class User_Panel {
    
    private static ResourceBundle confi;
    public User_Panel(){
        if (System.getProperty("swing.defaultlaf") == null) {
            try {
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
            } 
            catch (Exception e) {}
	}
        
        confi=ResourceBundle.getBundle("desktop_vnc.config",Locale.getDefault());
        
        // FRAME
        JFrame frame = new JFrame("User Panel");
        frame.setLayout(new CardLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
         // COLOR
        int red = 49;
        int green = 49;
        int blue = 49;
        Color back = new Color(red,green,blue);
        red = 200;
        green = 200;
        blue = 200;
        Color white = new Color(red,green,blue);
        red = 12;
        green = 122;
        blue = 207;
        Color button = new Color(red,green,blue);
        
        
         /* 
            ---------------------------------------------------------------------------------
		SENDER PANEL
            ---------------------------------------------------------------------------------	
            */
            frame.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            frame.getContentPane().setBackground(back);


            JLabel l = new JLabel("Please Type your Name in the given Field");
            Font myFont = new Font("Serif",Font.BOLD,18);
            l.setFont(myFont);
            l.setForeground(white);
            c.gridx = 2;
            c.gridy = 0;
            frame.add(l,c);

	    JTextField nameField = new JTextField("",20);
	    myFont = new Font("Serif",Font.BOLD,16);
	    nameField.setFont(myFont);
	    c.gridx = 2;
	    c.gridy = 1;
	    frame.add(nameField,c);

	    JButton sendButton = new JButton("Send");
	    myFont = new Font("Serif",Font.BOLD,16);
	    sendButton.setFont(myFont);
            sendButton.setActionCommand("send");
            sendButton.setBackground(button);
            sendButton.setForeground(white);
            sendButton.setOpaque(true);
            sendButton.setBorderPainted(false);
            sendButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(nameField.getText().length() == 0)
                    {
                        System.out.println("Name Empty");
                        JOptionPane.showMessageDialog(frame, "Please provide your Name", "Info", 1);
                        return;
                    } else
                    {
                        sendButton.setEnabled(false);
                        nameField.setEditable(false);

                        JPanel panelPbar = new JPanel();
                        JProgressBar pBar = new JProgressBar();
                        pBar.setIndeterminate(true);
                        panelPbar.add(pBar);
                        c.gridx = 2;
                        c.gridy = 4;
                        frame.add(panelPbar,c);

                        frame.pack();
                        sendCredentials(nameField.getText());

                    }
                    
                    // CHANGE
                    MultiCastClient mc = new MultiCastClient();
                    mc.receive();
                }
            });
             c.gridx = 2;
	    c.gridy = 3;
            frame.add(sendButton,c);
            
           
	   
    }
    public static void sendCredentials(String name)
    {
        //private ResourceBundle config=ResourceBundle.getBundle("desktop_vnc.config", Locale.getDefault());
       //String serverIP=config.getString(serverIP);
        //String serverIP="192.168.2.13";
        int port =7077;
        String serverIP = confi.getString("serverip");
         
        try
        {
                    Socket client = new Socket(serverIP, port);
                     //BufferedImage img=Util.getScreenshot();
                    
                      String ip=client.getLocalSocketAddress().toString();
                      DataOutputStream out = new DataOutputStream(client.getOutputStream());
                      DataInputStream in=new DataInputStream(client.getInputStream());
                      out.writeUTF(ip);
                      out.writeUTF(name);
                      
                      //BufferedImage img=Util.getScreenshot();
                      for(int i=0;i<=10000;i++)
                      {                     
                      for(int j=0;j<=10000;j++);
                      }
                      Robot robot = new Robot();
		//BufferedImage img = robot.createScreenCapture(new Rectangle(200, 400));
                  BufferedImage img=Util.getScreenshot();    
                      JFrame frame1 = new JFrame("IMG");
                  frame1.getContentPane().add(new JLabel(new ImageIcon(img)));
                  frame1.pack();
                  frame1.setVisible(true);
                      
                                        ImageIO.write(img, "jpg", client.getOutputStream());
                      /*String mesg=in.readUTF();
                      if(mesg.equalsIgnoreCase("1")==true)
                      {
                          System.out.println("Success");
                          break;
                      }*/
                      client.close();
                      
                     

        }
        catch (IOException ex) {
            Logger.getLogger(User_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(User_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   
    public static void main(String[] args){
        // DO NOTHING
       // User_Panel u = new User_Panel();
       // u.build();
    }
}
