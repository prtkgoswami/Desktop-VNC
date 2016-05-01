package desktop_vnc;

import java.awt.Toolkit;
import java.util.*;
import java.awt.image.BufferedImage;

import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Admin_Panel
 implements ActionListener, WindowListener{
    
    //public String[][] arr;
    public static ArrayList<ArrayList<String>> Users;
    private ServerGet serverGet = null;
    JFrame frame;
    
    public Admin_Panel(){
        if (System.getProperty("swing.defaultlaf") == null) {
            try {
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
            } 
            catch (Exception e) {}
	}
        
        Users=new ArrayList<ArrayList<String>>();
        
        // FRAME
        frame = new JFrame("Admin Panel");
        //frame.setLayout(new CardLayout());
        //Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //frame.setSize(dim);
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        frame.setSize(xSize,ySize);
        //frame.setResizable(false);
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
        
        ArrayList arr = new ArrayList();
        ArrayList temp = new ArrayList();
        for(int i=1;i<=150;i++){
            temp=new ArrayList();
            temp.add("n"+i);
            temp.add("1.2.3."+i);
            arr.add(temp);
        }
        //System.out.println(arr);
        
        frame.getContentPane().setBackground(back);
        /* 
        ---------------------------------------------------------------------------------
            RECEIVER PANEL
        ---------------------------------------------------------------------------------	
        */
        frame.setLayout(new BorderLayout());
            JPanel grid = new JPanel(new GridLayout(0,2));
            grid.setBackground(back);
            JScrollPane screenPane = new JScrollPane(grid);
            screenPane.setSize(860,1800);
            screenPane.setVisible(true);
            frame.add(screenPane,BorderLayout.CENTER);
            
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(white);
           // buttonPane.setSize(860,800);
            //buttonPane.setVisible(true);
            frame.add(buttonPane,BorderLayout.SOUTH); 
            
            JPanel refreshPanel = new JPanel();
            JButton refreshButton = new JButton("Refresh");
            refreshButton.setFont(new Font("Verdana",1,22));
            refreshButton.setBackground(button);
            refreshButton.setForeground(white);
            refreshButton.setOpaque(true);
            refreshButton.setBorderPainted(false);
            refreshButton.setActionCommand("refresh");
            refreshButton.addActionListener(new ActionListener()
            {
                Font myFont = new Font("Serif",1,15);
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Refresh");
                    ArrayList al;
                    for( int i = 0 ; i < 8 ; i++ ){
                        JPanel screen = new JPanel();
                        LayoutManager overlay = new OverlayLayout(screen);
                        screen.setLayout(overlay);
                        screen.setSize(new Dimension(200, 200));
                        screen.setBorder(BorderFactory.createLineBorder(white));
                        screen.setBackground(Color.black);
                                               
                        // SETTING IMAGE TO CARD
                        try{
                            BufferedImage img = Util.getScreenshot();
                            img = Util.shrink(img, 35 / 100D);
                            JLabel screenshotLabel = new JLabel();
                            screenshotLabel.setIcon(new ImageIcon(img));
                            
                            screen.add(screenshotLabel);
                        }catch(Exception ex){}
                        
                        grid.add(screen);
                    }
                    frame.pack();
                }
            });
            refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            refreshPanel.add(refreshButton);
            refreshPanel.setBorder(BorderFactory.createEmptyBorder(10, 80, 50, 30));
            refreshPanel.setBackground(white);
            buttonPane.add(refreshPanel);
            
            JPanel castPanel = new JPanel();
            JButton castButton = new JButton("Cast Screen");
            castButton.setFont(new Font("Verdana",1,22));
            castButton.setForeground(white);
            castButton.setBackground(button);
            castButton.setOpaque(true);
            castButton.setBorderPainted(false);
            castButton.setActionCommand("cast");
            castButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    MultiCastServer ms=new MultiCastServer();
                    ms.send();
                }
            });
            castButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            castPanel.add(castButton);
            castPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 50, 70));
            castPanel.setBackground(white);
            buttonPane.add(castPanel);
		
            // STARTS THREAD TO RECIEVE INFO FROM CLIENTS
            receive();
	}
    public void test(){
        try {
            BufferedImage img=Util.getScreenshot();
            JFrame frame1 = new JFrame();
            frame1.getContentPane().add(new JLabel(new ImageIcon(img)));
            frame1.pack();
            frame1.setVisible(true); 
        } catch (AWTException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void receive() {
        try {
            serverGet = new ServerGet(this);
            serverGet.start();
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
    
    public static void main(String[] args){
    // Do Nothing
        //Admin_Panel a = new Admin_Panel();
       // a.build();
    }
        
     public void createConn(String IP, String name){
        ArrayList<String> temp=new ArrayList<String>();
        temp.add(IP);
        temp.add(name);
        Users.add(temp);
        
        
    }
    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent arg0)
    {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }   
    public void actionPerformed(ActionEvent action)
    {
    }
        
}
class ServerGet extends Thread
{
       Admin_Panel parent;
       private ServerSocket serverSocket;
       Socket server;
       private JFrame frame;

	private JWindow fullscreenWindow = null;

	private JLabel labelImage;

	private JLabel windowImage;

	private BufferedImage image;

       public ServerGet(Admin_Panel parent) throws IOException, SQLException, ClassNotFoundException, Exception
       {
          this.parent = parent;
          serverSocket = new ServerSocket(7077);
          serverSocket.setSoTimeout(180000);
       }

       public void run()
       {
           while(true)
          {
               try
               {
                  server = serverSocket.accept();
                  DataInputStream din=new DataInputStream(server.getInputStream());
                  DataOutputStream dout=new DataOutputStream(server.getOutputStream());
                  String IP = din.readUTF();
                  String name = din.readUTF();
                  
                  BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                  JFrame frame1 = new JFrame();
                  frame1.getContentPane().add(new JLabel(new ImageIcon(img)));
                  frame1.pack();
                  frame1.setVisible(true);  
//                  BufferedImage bimg = Util.getScreenshot();
//            JFrame frame1 = new JFrame();
//            frame1.getContentPane().add(new JLabel(new ImageIcon(bimg)));
//            frame1.pack();
//            frame1.setVisible(true);
//         ImageIO.write(bimg,"jpg",server.getOutputStream());
                  dout.writeUTF("1");
                  
                  parent.createConn(IP,name);                  
                  
                  //server.close();
                  //BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(socket.getInputStream()));
                  //lblimg.setIcon(img);
              }
             catch(SocketTimeoutException st)
             {
                   System.out.println("Socket timed out!");
                  break;
             }
             catch(IOException e)
             {
                  e.printStackTrace();
                  break;
             }
             catch(Exception ex)
            {
                  System.out.println(ex);
            }
          }
       }
}