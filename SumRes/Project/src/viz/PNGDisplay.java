package viz;

import java.awt.*;
import java.io.File;

import javax.swing.*; 

@SuppressWarnings("serial")
public class PNGDisplay extends JFrame
{    

  public void ShowPNG(String arg) 
  { 
    JPanel panel = new JPanel(); 
    panel.setSize(500,640);
    panel.setBackground(Color.CYAN); 
    ImageIcon icon = new ImageIcon(arg); 
    JLabel label = new JLabel(); 
    label.setIcon(icon); 
    panel.add(label);
    this.getContentPane().add(panel); 
    this.setVisible(true);
  }
	
	public static void main(String[] args) {
		PNGDisplay c = new PNGDisplay();
		File f = new File("files/Test.png");
		System.out.println(f.exists() + ": " + f.getAbsolutePath());
		c.ShowPNG(f.getAbsolutePath());
	}

}
