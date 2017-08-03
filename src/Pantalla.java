import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Pantalla extends JPanel {
	
	ImageIcon imagen;
	String nombre;
	
	public Pantalla(String nombre)
	{
		this.nombre = nombre;
	}

	public void paint(Graphics g)
	{
	
		imagen = new ImageIcon(getClass().getResource(nombre));
		g.drawImage(imagen.getImage(),0,0,null);
}
}
