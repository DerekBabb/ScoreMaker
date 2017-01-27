/**
 * @author Derek Babb <derek.babb@bpsne.net>
 * @version 1.0
 * @since 7-17-2013
 *
 * ScoreMaker is an application to test an Optical Character Recognition algorithm
 * specifically for an OCR reading a scoreboard.  OCRs should implement the DigitReader interface.
 * 
 * ScoreMaker has two modes:
 * 1)  Lights Mode: Allows the user to individually turn off and on "Lights" in an image.
 * This mode will allow a programmer to test ideal cases of each digit, also has a feature
 * to modify one or two lights to see what cases begin to fail.
 * 
 * 2)  Image Selector Mode:  Allows the user to open an existing scoreboard image and select
 * an area in which a digit is contained.  That sub-image is fed to the OCR and through this you can
 * program a real-world tester based on either actual images of your scoreboard or other images from
 * the internet and other sources.
 * 
 * To load your OCRs to test, simply define them in the construcor
 *
 */


import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.net.*;
import java.util.*;


public class ScoreMaker extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
  //Adjust this number depending on how many OCRs you want to load.
  /**********************************************************************/
  DigitReader[] reader = new DigitReader[2];  
  /**********************************************************************/
  
  private JButton[] lights = new JButton[54];
  private static final int NUM_ROWS = 9;
  private static final int NUM_COLS = 6;
  private JFrame jf;
  private Point[] point = new Point[2];
  Color lightColor = Color.RED;
  Color backColor = Color.BLACK;
  PictureEdit pic, actualImage;
  int selectedOCR = 0;
  JMenu selectOCR;
  ButtonGroup group;
  
  
  public static void main(String[] args)
  {
    ScoreMaker sm = new ScoreMaker();
    sm.setupLightEditor();
  }
  
  
  public ScoreMaker()
  {
    //setup the OCRs you want to be able to test
    /**********************************************************************/
    reader[0] = new ExampleOCR();
    reader[1] = new TerribleOCR();
    /**********************************************************************/
    
    for(int i = 0; i < lights.length; i++)
    {
      lights[i] = new JButton();
      lights[i].setBackground(Color.BLACK);
    }
    point[0] = new Point(0,0);
    point[1] = new Point(1,1);
    
    selectOCR= new JMenu("Set OCR");
    
    JRadioButtonMenuItem[] ocrs = new JRadioButtonMenuItem[reader.length];
    group = new ButtonGroup();
    for(int i = 0; i < reader.length; i++)
    {
      ocrs[i] = new JRadioButtonMenuItem(reader[i].getName());
      group.add(ocrs[i]);
      selectOCR.add(ocrs[i]);
      ocrs[i].addActionListener(this);
      
    }
    ocrs[selectedOCR].setSelected(true);
    
  }
  
  public int getSelectedOCR()
  {
    int i = 0;
    for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();i++) {
      AbstractButton button = buttons.nextElement();
      if (button.isSelected()) {
        return i;
      }
    }
    return 0; 
  }
  
  public void setupImageSelector()
  {
    jf = new JFrame("Image Selector");
    Container cp = jf.getContentPane();
    
    actualImage = new PictureEdit();
    
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jf.setSize(actualImage.getWidth() + 50 ,actualImage.getHeight() + 100);
    jf.setLocation(100,100);
    jf.setLayout(new GridLayout(1,1));
    
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem saveImage = new JMenuItem("Save Area");
    JMenuItem switchTo = new JMenuItem("Switch to Light Editor");
    JMenuItem exit = new JMenuItem("Exit");
    
    
    JMenu imageMenu = new JMenu("Image");
    JMenuItem testImage = new JMenuItem("Test Area");
    
    
    
    
    imageMenu.add(testImage);
    testImage.addActionListener(this);
    
    fileMenu.add(saveImage);
    fileMenu.add(selectOCR);
    fileMenu.addSeparator();
    fileMenu.add(switchTo);
    fileMenu.addSeparator();
    fileMenu.add(exit);
    
    saveImage.addActionListener(this);
    selectOCR.addActionListener(this);
    switchTo.addActionListener(this);
    exit.addActionListener(this);
                             
    
    menuBar.add(fileMenu);
    menuBar.add(imageMenu);
   
    jf.setJMenuBar(menuBar);
   
    cp.add(this);
    cp.addMouseListener(this);
    cp.addMouseMotionListener(this);
    jf.setVisible(true);
  }
  
  public void setupLightEditor()
  {
    jf = new JFrame("Lights Editor");
    Container cp = jf.getContentPane();
    
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jf.setSize(300,400);
    jf.setLocation(100,100);
    jf.setLayout(new GridLayout(NUM_ROWS, NUM_COLS));
    
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem saveImage = new JMenuItem("Save Image");
    JMenuItem switchTo = new JMenuItem("Switch to Image Select");
    JMenuItem exit = new JMenuItem("Exit");
    
    JMenu imageMenu = new JMenu("Image");
    JMenuItem clearImage = new JMenuItem("Clear Image");
    JMenuItem testImage = new JMenuItem("Test Image");
    
    imageMenu.add(clearImage);
    imageMenu.add(testImage);
    clearImage.addActionListener(this);
    testImage.addActionListener(this);
    
    fileMenu.add(saveImage);
    fileMenu.add(selectOCR);
    fileMenu.addSeparator();
    fileMenu.add(switchTo);
    fileMenu.addSeparator();
    fileMenu.add(exit);
    
    selectOCR.addActionListener(this);
    switchTo.addActionListener(this);
    saveImage.addActionListener(this);
    exit.addActionListener(this);
                             
    
    menuBar.add(fileMenu);
    menuBar.add(imageMenu);
    
    JMenu testMenu = new JMenu("Test");
    JMenuItem oneWrong = new JMenuItem("One Wrong");
    JMenuItem twoWrong = new JMenuItem("Two Wrong");
    
    testMenu.add(oneWrong);
    testMenu.add(twoWrong);
    menuBar.add(testMenu);
    
    oneWrong.addActionListener(this);
    twoWrong.addActionListener(this);
    jf.setJMenuBar(menuBar);
    for(int i = 0; i < lights.length; i++)
    {
      lights[i] = new JButton();
      lights[i].setBackground(Color.BLACK);
      cp.add(lights[i]);
      lights[i].addActionListener(this);
    }
    

    jf.setVisible(true);
  }
  
  public void setOCR()
  {
    JFileChooser chooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("CLASS files", "class");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);
    File classFile = null;
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      try{
        classFile = chooser.getSelectedFile();
        String className = classFile.getName().substring(0,classFile.getName().indexOf("."));
        String fileURL = classFile.toURI().toURL().toString();
        fileURL = fileURL.substring(0, fileURL.lastIndexOf("/")+1);
        
        URL testURL = new URL(fileURL);
        URLClassLoader classLoader = new URLClassLoader(new URL[]{testURL});
        System.out.println();
        Class<?> clazz = classLoader.loadClass(className);
        Class<? extends DigitReader> sub = clazz.asSubclass(DigitReader.class);
        Object instance = clazz.newInstance();
        System.out.println(instance);
        
        
        //DigitReader instance = (DigitReader)clazz.newInstance();
        
        //System.out.println(instance.getDigit());
//        reader = (DigitReader)instance;
       // Method method = clazz.getDeclaredMethod(methodName, String.class);
        //method.setAccessible(true);
        //method.invoke(instance, toSet);
        
      
      }
      catch(Exception e){e.printStackTrace(); }
    }
  }
  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(actualImage.getImage(), 0, 0, actualImage.getWidth(), actualImage.getHeight(), this);
    
    g.setColor(new Color(0, 0, 255, 100));
    int x1 = (int)point[0].getX();
    int y1 = (int)point[0].getY();
    int w = (int)point[1].getX() - x1;
    int h = (int)point[1].getY() - y1;
    
    g.fillRect(x1, y1, w, h);
  }
  
  public void createImage()
  {
    pic = new PictureEdit("images/blank.png");
    for(int y = 0; y < pic.getHeight(); y++)
      for(int x = 0; x < pic.getWidth(); x++)
      pic.setColor(x,y, backColor);
    //lights will be square for this test.
    //gap between the lights will be 1/4 the size of the lights.
    
    //first, compute the size of the bulb given the size of the canvas.
    //don't want to overflow in either direction so take the min of the two options
    
    int bulbWidth = pic.getWidth() / (NUM_COLS);
    int bulbHeight = pic.getHeight() / (NUM_ROWS);
    
    int bulbSize = Math.min(bulbWidth, bulbHeight);
    
    int gap = bulbSize / 4;
    int bulbCount = 0;
    for(int y = 0; y < NUM_ROWS; y++)
    {
      for(int x = 0; x < NUM_COLS; x++)
      {
        if(lights[bulbCount].getBackground().equals(lightColor))
          fillBulb(pic, x * (bulbSize + gap) ,y* (bulbSize + gap),bulbSize);
        
        bulbCount++;
      }
    }
    
    //pic.displayImage();
    
  }
  
  public void fillBulb(PictureEdit pic, int xStart, int yStart, int bulbSize)
  {
    for(int y = 0; y < bulbSize; y++)
    {
      for(int x = 0; x < bulbSize; x++)
      {
        pic.setColor(xStart + x, yStart + y,lightColor);
      }
    }
  }
  
  public int[] switchOne()
  {
    int expected = Integer.parseInt(JOptionPane.showInputDialog("Enter the expected number: "));
    int wrongCount = 0;
    int totalCount = 0;
    for(int i = 0; i < lights.length; i++)
    {
      //Switch one light
      if(lights[i].getBackground().equals(Color.BLACK))
        lights[i].setBackground(Color.RED);
      else
        lights[i].setBackground(Color.BLACK);
      
      createImage();
      //reader = new DigitRead();
      reader[selectedOCR].setImage(pic);
      int numRead = reader[selectedOCR].getDigit();
      if(numRead != expected)
      {
        wrongCount++;
        pic.saveAs("wrongImages\\"+expected +"wrong" + wrongCount +"_" + numRead +".png");
      }
      totalCount++;
      //reader.displayScore();
      
       //Switch the light back to the way it was.
      if(lights[i].getBackground().equals(Color.BLACK))
        lights[i].setBackground(Color.RED);
      else
        lights[i].setBackground(Color.BLACK);
    }
    int[] result = {totalCount, wrongCount};
    return result;
  }
  
  public int[] switchTwo()
  {
    int expected = Integer.parseInt(JOptionPane.showInputDialog("Enter the expected number: "));
    int wrongCount = 0;
    int totalCount = 0;
    for(int i = 0; i < lights.length; i++)
    {
      //Switch one light
      if(lights[i].getBackground().equals(Color.BLACK))
        lights[i].setBackground(Color.RED);
      else
        lights[i].setBackground(Color.BLACK);
      
      for(int j = i+1; j < lights.length; j++)
      {
        //Switch one light
        if(lights[j].getBackground().equals(Color.BLACK))
          lights[j].setBackground(Color.RED);
        else
          lights[j].setBackground(Color.BLACK);
        
        createImage();
        //DigitRead reader = new DigitRead();
        reader[selectedOCR].setImage(pic);
        int numRead = reader[selectedOCR].getDigit();
        if(numRead != expected)
        {
          wrongCount++;
          pic.saveAs("wrongImages\\"+expected +"wrong" + wrongCount +"_" + numRead +".png");
        }
        totalCount++;
        
         //Switch the light back to the way it was.
        if(lights[j].getBackground().equals(Color.BLACK))
          lights[j].setBackground(Color.RED);
        else
          lights[j].setBackground(Color.BLACK);
      }
      //Switch the light back to the way it was.
      if(lights[i].getBackground().equals(Color.BLACK))
        lights[i].setBackground(Color.RED);
      else
        lights[i].setBackground(Color.BLACK);
    }
     int[] result = {totalCount, wrongCount};
    return result;
  }
  
  public void createAreaImage()
  {
    int x1 = (int)point[0].getX();
    int y1 = (int)point[0].getY();
    int w = (int)point[1].getX() - x1;
    int h = (int)point[1].getY() - y1;
    
    pic = new PictureEdit(w,h);
    
    for(int x = 0; x < w; x++)
    {
      for(int y = 0; y < h; y++)
      {
        pic.setColor(x,y,actualImage.getColor(x1+x, y1+y)); 
      }
    }
    pic.displayImage();
  }
  
  public void actionPerformed(ActionEvent e)
  {
    for(int i = 0; i < lights.length; i++)
    {
      if(e.getSource() == lights[i])
      {
        if(lights[i].getBackground().equals(Color.BLACK))
          lights[i].setBackground(Color.RED);
        else
          lights[i].setBackground(Color.BLACK);
        
      }
    }
    
    if(e.getActionCommand().equals("Exit"))
         System.exit(0);
    
    if(e.getActionCommand().equals("Save Image"))
    {
       createImage();
       pic.saveAs();
    }
    if(e.getActionCommand().equals("Clear Image"))
    {
      for(int i = 0; i < lights.length; i++)
          lights[i].setBackground(Color.BLACK);
    }
    
    if(e.getActionCommand().equals("Test Image"))
    {
      createImage();
      //DigitRead reader = new DigitRead();
      reader[selectedOCR].setImage(pic);
      int numRead = reader[selectedOCR].getDigit();
      JOptionPane.showMessageDialog(null, numRead+"", "Result", JOptionPane.INFORMATION_MESSAGE);
    }
    
    if(e.getActionCommand().equals("Test Area"))
    {
      createAreaImage();
      //DigitRead reader = new DigitRead();
      reader[selectedOCR].setImage(pic);
      int numRead = reader[selectedOCR].getDigit();
      JOptionPane.showMessageDialog(null, numRead+"", "Result", JOptionPane.INFORMATION_MESSAGE);

    }
   if(e.getActionCommand().equals("Save Area"))
    {
      createAreaImage();
      pic.saveAs();
    }
    
    if(e.getActionCommand().equals("One Wrong"))
    {
      int[] result = switchOne();
      JOptionPane.showMessageDialog(null, "Correct: " + (result[0] - result[1]) + "\n" + "Wrong: " + result[1], "Results", JOptionPane.INFORMATION_MESSAGE);

    }
    if(e.getActionCommand().equals("Two Wrong"))
    {
      int[] result = switchTwo();
      JOptionPane.showMessageDialog(null, "Correct: " + (result[0] - result[1]) + "\n" + "Wrong: " + result[1], "Results", JOptionPane.INFORMATION_MESSAGE);

    }
    if(e.getActionCommand().equals("Switch to Image Select"))
    {
      jf.dispose();
      setupImageSelector();
    }
    if(e.getActionCommand().equals("Switch to Light Editor"))
    {
      jf.dispose();
      setupLightEditor();
    }
    selectedOCR = getSelectedOCR();
  }
  
  public void mousePressed(MouseEvent e)
  {
    point[0] = e.getPoint();
    repaint();
  }
  
  public void mouseReleased(MouseEvent e)
  {
    point[1] = e.getPoint();
    repaint();
  }
  
  public void mouseClicked(MouseEvent e)
  {
  }
  
  public void mouseEntered(MouseEvent e)
  {
  }
  
  public void mouseExited(MouseEvent e)
  {
  }
  
  public void mouseMoved(MouseEvent e)
  {
  }
  
  public void mouseDragged(MouseEvent e)
  {
    point[1] = e.getPoint();
    repaint();
  }
}
  