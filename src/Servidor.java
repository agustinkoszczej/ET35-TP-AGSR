import javax.swing.*;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.net.*;
import java.util.Calendar;
import java.io.*;
import java.awt.event.*;

public class Servidor {
	

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
	ServerSocket servidor = null;
	static Socket socket=null;
	static BufferedReader lector=null;
	static PrintWriter escritor = null;
	Calendar calendario = Calendar.getInstance();
	int hora, minutos;
	static int Clientes[] = new int[100];
	static ObjectInputStream ClientesInput[] = new ObjectInputStream[Clientes.length + 1];
	static ObjectOutputStream ClientesOutput[] = new ObjectOutputStream[Clientes.length + 1];
	static String NombreCliente[] = new String[Clientes.length + 1];
	

	static String Mensaje;
	private static String nombre_servidor;
	private static String SonidoMensaje = "MensajeRecibido";
	private static final AudioClip MensajeRecibido = Applet.newAudioClip(Cliente.class.getResource("MensajeRecibido.wav"));
    private static final AudioClip Tweet = Applet.newAudioClip(Cliente.class.getResource("tweet.wav"));
    private static final AudioClip Facebook = Applet.newAudioClip(Cliente.class.getResource("facebook.wav"));
	private static final AudioClip Ondas = Applet.newAudioClip(Cliente.class.getResource("Ondas.wav"));
	private static final AudioClip Whatsapp = Applet.newAudioClip(Cliente.class.getResource("Whatsapp.wav"));
	
	public Servidor()
	{
		hacerInterfaz();
	}
	
	public void hacerInterfaz()
	{
		ventana_chat = new JFrame("Servidor");
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
		area_chat.setEditable(true);
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
		Pantalla pantalla = new Pantalla("imagen3.png");
		ventana_chat.add(pantalla,BorderLayout.CENTER);
		ventana_chat.setSize(400,640);
		ventana_chat.setVisible(true);
		ventana_chat.setResizable(false);
		ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Thread principal = new Thread(new Runnable(){
			public void run()
			{
				try{
				servidor=new ServerSocket(9000);
			    AgregarNuevosClientes AceptarClientes = new AgregarNuevosClientes("Servidor");
				while(true)
				{
			
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
				}
				catch(Exception ex)
				{
					area_chat.append("Error en el puerto");
					ex.printStackTrace();
				}
				
			
				
				}
		});
		principal.start();
	}
	
	public static void main(String[] args) {
		nombre_servidor = JOptionPane.showInputDialog("Ingrese Nombre del Servidor: ");
		
		new Servidor();
	}
	
    public class AgregarNuevosClientes implements Runnable
    {
        Thread t;
		
		AgregarNuevosClientes(String Nombre)
		{
			t = new Thread(this, Nombre);
			t.start();
		}
		
		public void run() 
		{  
			while(true)
			{
				try
				{	
					try 
					{
						socket = servidor.accept(); // wait for someone to join on server
					} 
					catch (Exception ex) { break; }
					
					int cont = 0; //esto es para saber el id del cliente que se conecta
					for(int i = 0; i < Clientes.length; i++)
					{
						if(Clientes[i] == 0) 
						{
							Clientes[i] = i + 1;
							cont = i;
							break;
						}
					}
						
					ClientesOutput[Clientes[cont]] = new ObjectOutputStream(socket.getOutputStream()); 
					ClientesOutput[Clientes[cont]].flush();
					ClientesInput[Clientes[cont]] = new ObjectInputStream(socket.getInputStream());
						
					
					EnvioMensajeAClientes(Clientes[cont], "se ha Conectado!"); // muestro que el id del cliente que se conecto en el servidor
					NombreCliente[Clientes[cont]] = (String) ClientesInput[Clientes[cont]].readObject(); // metodo para obtener el nombre del cliente que se conecto
					area_chat.setForeground(Color.RED);
					area_chat.append("Cliente:" + NombreCliente[Clientes[cont]] +  "Conectado!\n"); // aca se imprime el cliente que se conecto al servidor
				    area_chat.setCaretPosition(area_chat.getText().length());
				    MensajeServidor(NombreCliente[Clientes[cont]] + " Conectado !!!"); // aca muestro en la pantalla del cliente que se conecto al servidor
						
					RecibirMsgs ob2 = new RecibirMsgs(Clientes[cont], "recibirmensaje" + Clientes[cont]); // make new thread for every new client 
					
				} 
				catch(Exception e) 
				{ 
					e.printStackTrace(); 
				}
						
					EnvioMensaje();
		
		
			}
		}
    }
	
    //metodo para que el servidor envie un mensaje a todos los clientes que esten conectados
    void MensajeServidor(String mensaje) 
	{
		for(int i = 0; i < Clientes.length; i++) // Enviar mensaje a todos los clientes
		{
			if(Clientes[i] != 0) 
			{
				try
				{
					ClientesOutput[i+1].writeObject(mensaje); 
					ClientesOutput[i+1].flush();
			
				}
				catch(Exception e) 
				{ }
			}
		}
	}
    
    public void EnvioMensajeAClientes(int idcliente, String mensaje) 
	{
		try
		{
			ClientesOutput[idcliente].writeObject(mensaje); 
			ClientesOutput[idcliente].flush();
		
		} 
		catch(Exception e) 
		{ }
	}
    
    
    public void EnvioMensaje()
	{
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
					enviar_mensaje="0"+hora+":0"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
					}
					else if ((hora <= 9)&&(minutos>9)){
						enviar_mensaje="0"+hora+":"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
					}
					else if ((hora > 9)&&(minutos<=9)){
						enviar_mensaje=hora+":0"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
					}
					else{
						enviar_mensaje=hora+":"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
					}
					MensajeServidor(enviar_mensaje);
					area_chat.setLineWrap(true);
					area_chat.setWrapStyleWord(true);
					area_chat.append(""+ enviar_mensaje + "\n");
					area_chat.setCaretPosition(area_chat.getDocument().getLength());
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
				enviar_mensaje="0"+hora+":0"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
				}
				else if ((hora <= 9)&&(minutos>9)){
					enviar_mensaje="0"+hora+":"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
				}
				else if ((hora > 9)&&(minutos<=9)){
					enviar_mensaje=hora+":0"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
				}
				else{
					enviar_mensaje=hora+":"+minutos+" "+horario+" - "+nombre_servidor+" dice: "+txt_mensaje.getText();
				}
			
				MensajeServidor(enviar_mensaje);
				area_chat.setLineWrap(true);
				area_chat.setWrapStyleWord(true);
				area_chat.append("" + enviar_mensaje + "\n");
				area_chat.setCaretPosition(area_chat.getDocument().getLength());
	
				txt_mensaje.setText("");
				}
			}

		});
	}
    
    //esta clase es para que cuando un cliente envia un mensaje al servidor el servidor lo envie a los demas clientes conectados
	public class RecibirMsgs implements Runnable 
	{
		int idcliente;
		RecibirMsgs ob1;
		Thread t;
		
		RecibirMsgs(int idcliente, String Nombre)
		{
			this.idcliente = idcliente;
			t = new Thread(this, Nombre);
			t.start();
		}
		
		public void run() 
		{ 
			do
			{	
				try
				{	
					Mensaje = (String) ClientesInput[idcliente].readObject(); 
					area_chat.append(Mensaje + "\n"); // muestro el mensaje
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
				catch(Exception e) { Mensaje = "cliente desconectado"; }
				
				if(Mensaje.equals("cliente desconectado")) // aca pregunto si el mensaje = cliente desconectado se para el hilo para ese cliente que se desconecto
				{
				    area_chat.append(NombreCliente[idcliente] + " se ha Desconectado!\n"); // imprime que cliente se desconecto del servidor
				    area_chat.setCaretPosition(area_chat.getText().length());
				    MensajeAClientes(idcliente, ""+ NombreCliente[idcliente] + " se ha Desconectado!"); // aca le envio a todos que el cliente se desconecto
					
					Clientes[idcliente - 1] = 0; // aca limpio la posicion del array en el que se encontraba el cliente que se desconecto para que se pueda guardar otro cliente en esa posicion
					NombreCliente[idcliente] = ""; // aca limpio el nombre del cliente que se desconecto del array
					try
					{
						ClientesInput[idcliente].close(); // aca cierro los datos de entrada de ese cliente
						ClientesOutput[idcliente].close(); // aca cierro los datos de salida de ese cliente
					} catch(Exception ex) { } 
				}
				// si esta conectado el cliente
				else
					MensajeAClientes(idcliente, Mensaje);  //envio el mensaje a todos los usuarios
			} while(!Mensaje.equals("cliente desconectado"));
		}
	}
	

	void MensajeAClientes(int cli, String mensaje) 
	{
		for(int i = 0; i < Clientes.length; i++) 
		{
			if(Clientes[i] != 0) 
			{
				if(cli != i+1) 
				{ 
					try
					{
						ClientesOutput[i+1].writeObject(mensaje); 
						ClientesOutput[i+1].flush();
					} 
					catch(Exception e) 
					{ }
				}
			}
		}
	}


}



