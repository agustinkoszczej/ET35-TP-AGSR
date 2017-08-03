import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;



public class Cliente{

	JMenuBar mb=null;
	JMenu menu1=null;
	JMenuItem mi1,mi2,mi3,mi4,mi5;
	JFrame ventana_chat=null;
	static JButton btn_enviar = null;
	static JTextField txt_mensaje = null;
	static JTextArea area_chat = null;
	JPanel contenedor_areachat = null;
	JPanel contenedor_btntxt = null;
	JScrollPane scroll = null;
	static Socket socket=null;

	ImageIcon imagen;
	static ObjectOutputStream ClientesOutput;
	static ObjectInputStream ClientesInput;	
	static String mensajerecibido;
	Pantalla pantalla = null;
	
	private static String ip;
	static String nombre_cliente;
	private static String SonidoMensaje = "MensajeRecibido";
	private static final AudioClip MensajeRecibido = Applet.newAudioClip(Cliente.class.getResource("MensajeRecibido.wav"));
	private static final AudioClip Tweet = Applet.newAudioClip(Cliente.class.getResource("tweet.wav"));
	private static final AudioClip Facebook = Applet.newAudioClip(Cliente.class.getResource("facebook.wav"));
	private static final AudioClip Ondas = Applet.newAudioClip(Cliente.class.getResource("Ondas.wav"));
	private static final AudioClip Whatsapp = Applet.newAudioClip(Cliente.class.getResource("Whatsapp.wav"));
	
	
	public Cliente()
	{
		hacerInterfaz();
	}
	
	public static void main(String[] args) {
		
			nombre_cliente = JOptionPane.showInputDialog("Ingrese Nombre del Cliente: ");

		do{
				ip = JOptionPane.showInputDialog("Ingrese IP del Servidor: ");

			}while (ip == " ");
		
		new Cliente();
	}
	
	
	public void hacerInterfaz()
	{
		ventana_chat = new JFrame("Cliente");
		mb = new JMenuBar();
		ventana_chat.setJMenuBar(mb);
		menu1 = new JMenu("Sonidos Mensaje");
		mb.add(menu1);
		mi1 = new JMenuItem("Messenger");
		menu1.add(mi1);
		mi2 = new JMenuItem("Tweet");
		menu1.add(mi2);
		mi3 = new JMenuItem("Facebook");
		menu1.add(mi3);
		mi4 = new JMenuItem("Ondas");
		menu1.add(mi4);
		mi5 = new JMenuItem("Whatsapp");
		menu1.add(mi5);
		btn_enviar = new JButton("Enviar");
		txt_mensaje=new JTextField(4);
		area_chat = new JTextArea(12,30);
		area_chat.setForeground(Color.BLACK);
		area_chat.setEditable(false);
		scroll=new JScrollPane(area_chat);
		ventana_chat.add(scroll);
		contenedor_areachat=new JPanel();
		contenedor_areachat.setBounds(35,35,350,200);
		contenedor_areachat.add(scroll);
		ventana_chat.add(contenedor_areachat);
		//contenedor_btntxt = new JPanel();
		/*contenedor_btntxt.setLayout(new GridLayout(1,2));*/
		txt_mensaje.setBounds(30,520,200,25);
		txt_mensaje.setBorder(BorderFactory.createLineBorder(Color.white));
		ventana_chat.add(txt_mensaje);
		btn_enviar.setBounds(290,520,100,25);
		ventana_chat.add(btn_enviar);
		/*contenedor_btntxt.add(txt_mensaje);
		contenedor_btntxt.add(btn_enviar);*/
		/*ventana_chat.setLayout(new BorderLayout());
		ventana_chat.add(contenedor_areachat,BorderLayout.NORTH);
		ventana_chat.add(contenedor_btntxt,BorderLayout.SOUTH);*/
		pantalla = new Pantalla("image.jpg");
		ventana_chat.add(pantalla,BorderLayout.CENTER);
		ventana_chat.setSize(400,640);
		ventana_chat.setVisible(true);
		ventana_chat.setResizable(false);
		ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread principal = new Thread(new Runnable(){
			public void run()
			{
				try{
				socket=new Socket(ip,9000);
				
				ConectarAServidor s = new ConectarAServidor();
				
				mi1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(e.getSource() == mi1)
					{
						SonidoMensaje = "MensajeRecibido";
					}
				}
				});
				mi2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						if(e.getSource() == mi2)
						{
							SonidoMensaje = "Tweet";
						}
					}
					});
				mi3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						if(e.getSource() == mi3)
						{
							SonidoMensaje = "Facebook";
						}
					}
					});
				mi4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						if(e.getSource() == mi4)
						{
							SonidoMensaje = "Ondas";
						}
					}
					});
				mi5.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						if(e.getSource() == mi5)
						{
							SonidoMensaje = "Whatsapp";
						}
					}
					});
				
			

				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		principal.start();
	}
	
	class ConectarAServidor implements Runnable
	{
		ConectarAServidor s;
		Thread t;

		ConectarAServidor()
		{
			t = new Thread(this, "Servidor");
			t.start();
		}
		
		public void run()
		{
			try
			{
				try
				{
					ClientesOutput = new ObjectOutputStream(socket.getOutputStream());
					ClientesOutput.flush();
					ClientesInput = new ObjectInputStream(socket.getInputStream());
					
					mensajerecibido = (String) ClientesInput.readObject();
			
				    area_chat.append(mensajerecibido + "\n"); 
				    area_chat.setCaretPosition(area_chat.getText().length());
					EnviarMensaje(nombre_cliente);
				} 
				catch(Exception e) 
				{ }
			
				Leer leer = new Leer();
				escribir();
			
				
			}
			catch (Exception ex) 
			{ }
		}
	}	
	
	
	public void escribir()
	{
		
		try{
		 txt_mensaje.addKeyListener(new KeyListener()
		    {

			
				public void keyPressed(KeyEvent e) {
					
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						if (txt_mensaje.getText().equals("") == false)
						{
						Calendar calendario = Calendar.getInstance();
						int hora, minutos;
						String horario;
						hora = calendario.get(Calendar.HOUR_OF_DAY);
						minutos = calendario.get(Calendar.MINUTE);
						if (hora >= 12){
							horario = "p.m.";
						}
						else horario = "a.m.";
						String enviar_mensaje;
						if ((hora <= 9)&&(minutos <= 9)){
						enviar_mensaje="0"+hora+":0"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
						}
						else if ((hora <= 9)&&(minutos>9)){
							enviar_mensaje="0"+hora+":"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
						}
						else if ((hora > 9)&&(minutos<=9)){
							enviar_mensaje=hora+":0"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
						}
						else{
							enviar_mensaje=hora+":"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
						}
					
						area_chat.setLineWrap(true);
						area_chat.setWrapStyleWord(true);
						area_chat.append(""+ enviar_mensaje + "\n");
						area_chat.setCaretPosition(area_chat.getDocument().getLength());
						EnviarMensaje(enviar_mensaje);
						txt_mensaje.setText("");
						}
					}
					
				}

			
				public void keyReleased(KeyEvent e) {
				
					
				}

			
				public void keyTyped(KeyEvent e) {
					
					
				}
		    	
		    });
			btn_enviar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					if (txt_mensaje.getText().equals("") == false)
					{
					Calendar calendario = Calendar.getInstance();
					int hora, minutos;
					String horario;
					hora = calendario.get(Calendar.HOUR_OF_DAY);
					minutos = calendario.get(Calendar.MINUTE);
					if (hora >= 12){
						horario = "p.m.";
					}
					else horario = "a.m.";
					String enviar_mensaje;
					if ((hora <= 9)&&(minutos <= 9)){
					enviar_mensaje="0"+hora+":0"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
					}
					else if ((hora <= 9)&&(minutos>9)){
						enviar_mensaje="0"+hora+":"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
					}
					else if ((hora > 9)&&(minutos<=9)){
						enviar_mensaje=hora+":0"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
					}
					else{
						enviar_mensaje=hora+":"+minutos+" "+horario+" - "+nombre_cliente+" dice: "+txt_mensaje.getText();
					}

					area_chat.setLineWrap(true);
					area_chat.setWrapStyleWord(true);
					area_chat.append(""+ enviar_mensaje + "\n");
					area_chat.setCaretPosition(area_chat.getDocument().getLength());
					EnviarMensaje(enviar_mensaje);
					txt_mensaje.setText("");
					}
				}
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
	
	class Leer implements Runnable
	{
		Leer leer;
		Thread t;

		Leer()
		{
			t = new Thread(this, "Leer");
			t.start();
		}
		
		public void run()
		{
			try
			{
				do
				{	
					try
					{	
						mensajerecibido = (String)ClientesInput.readObject(); //mensaje recibido
						area_chat.append(mensajerecibido + "\n");
						area_chat.setCaretPosition(area_chat.getText().length());
						if (SonidoMensaje == "MensajeRecibido")
						{
						MensajeRecibido.play();
						}
						if (SonidoMensaje == "Tweet")
						{
						Tweet.play();
						}
						if (SonidoMensaje == "Facebook")
						{
						Facebook.play();
						}
						if (SonidoMensaje == "Ondas")
						{
						Ondas.play();
						}
						if (SonidoMensaje == "Whatsapp")
						{
						Whatsapp.play();
						}
					
						
					} 
					catch(Exception ex) 
					{ 
						
						area_chat.append("El Servidor ha sido desconectado !!!\n");
						area_chat.setCaretPosition(area_chat.getText().length());
						mensajerecibido = "cliente desconectado"; 
						try
						{
							ClientesInput.close();
							ClientesOutput.close();
							socket.close();
						} catch(Exception ex2) { }
						
					
					}
				} while(!mensajerecibido.equalsIgnoreCase("cliente desconectado"));
			} catch (Exception ex) { }
		}
	}
	
	void EnviarMensaje(String mensaje)
	{
		try 
		{ 
	
			ClientesOutput.writeObject(mensaje);
			ClientesOutput.flush(); 
		} 
		catch(Exception e) 
		{ }
	}
	


}
