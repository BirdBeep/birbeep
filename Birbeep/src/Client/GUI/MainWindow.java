package Client.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Client.SSLConexion;
import Client.Security;
import Client.Sender;
import Client.Modelo.Conversaciones;
import Client.Modelo.Mensajes;
import Client.Modelo.Peticion;
import Client.Modelo.PeticionCertificado;
import Client.Modelo.PeticionLogin;
import Client.Modelo.PeticionMensaje;
import Client.Modelo.PeticionUPDATEConv;
import Client.Modelo.PeticionUPDATEMsg;
import Client.Modelo.PeticionUpdateUsuarios;
import Client.Modelo.Usuarios;

public class MainWindow extends JFrame implements Runnable {
	public static final long serialVersionUID = 1L;
	public static Color mainColor;

	private static SSLConexion ssl = new SSLConexion();
	private static Usuarios user;
	private static Thread interfaz;
	private static Peticion p;
	private static Sender sender;

	public static List<Conversaciones> convers = new ArrayList<>();
	public static List<Usuarios> usuarios = new ArrayList<>();
	public static List<Mensajes> mensajes = new ArrayList<>();
	
	public static Dimension screenSize;
	public static Rectangle barra;

	public static Image icon_16;
	public static ImageIcon img_cargando;
	public static ImageIcon icon_64;
	public static ImageIcon fondo_login;
	public static ImageIcon img_usuario;
	public static ImageIcon img_conver;
	public static ImageIcon fondo_conver;

	public static JPanel contentPane_login;
	public static JPanel contentPane_conver;
	public static JLabel label_error;
	public static JLabel label_cargar;
	public static JPanel panel_credenciales;
	public static JLabel label_logo;
	public static JTextField email_login;
	public static JPasswordField password_login;
	public static TextPrompt placeholder;
	public static JButton btn_login;
	public static JPanel panel_fondo_login;
	public static JLabel label_fondo_login;

	public static JPanel panel_datos_usuario;
	public static JLabel label_img_usuario;
	public static JLabel label_nombre_usuario;
	public static JLabel label_email_usuario;
	public static JPanel panel_lista;
	public static JScrollPane scroll_lista;
	public static JButton btn_conver;
	public static JPanel panel_conver;
	public static JPanel panel_datos_conver;
	public static JLabel label_img_conver;
	public static JLabel label_nombre_conver;
	public static JPanel panel_chat;
	public static JScrollPane scroll_chat;
	public static JPanel panel_input;
	public static JTextArea text_area_input;
	public static JScrollPane scroll_input;
	public static JButton btn_enviar;
	public static JLabel label_fondo_conver;
	public static JButton btn_nueva;
	public static JButton btn_ver_conver;

	public static void main(String[] args) {
		interfaz = new Thread(new MainWindow());
		interfaz.start();
	}

	public void run() {
		try {
			ssl.start();
			MainWindow frame = new MainWindow();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MainWindow() {
		// Ventana Principal
		mainColor = new Color(51, 255, 119);
		icon_16 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon_16.png"));
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		barra = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		setIconImage(icon_16);
		setTitle(" Birbeep - Servicio de Mensajer\u00eda Segura");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, screenSize.width, barra.height - 20);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				if (JOptionPane.showConfirmDialog(rootPane, "¿Est\u00e1 seguro de que desea salir?", "Confirmaci\u00f3n",
						JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});

		// Contenedor principal
		contentPane_login = new JPanel();
		contentPane_login.setLayout(null);
		setContentPane(contentPane_login);

		// Mensaje de error
		label_error = new JLabel("");
		label_error.setForeground(Color.WHITE);
		label_error.setOpaque(true);
		label_error.setBackground(new Color(255, 0, 0, 150));
		label_error.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
		label_error.setBorder(new LineBorder(Color.WHITE));
		label_error.setVisible(false);
		label_error.setBounds((getSize().width - 400) / 2, 50, 400, 40);
		contentPane_login.add(label_error);

		// AnimaciÃ³n de carga
		label_cargar = new JLabel("");
		img_cargando = new ImageIcon(this.getClass().getResource("cargando.gif"));
		label_cargar.setIcon(img_cargando);
		label_cargar.setBounds((getSize().width - 100) / 2, (getSize().height - 50) / 3, 70, 70);
		label_cargar.setVisible(false);
		contentPane_login.add(label_cargar);

		// Cuadro de login
		panel_credenciales = new JPanel();
		panel_credenciales.setBounds((getSize().width-400)/2, getSize().height / 4, 400,300);
		panel_credenciales.setOpaque(false);
		panel_credenciales.setLayout(null);

		label_logo = new JLabel(" Birbeep");
		label_logo.setForeground(mainColor);
		label_logo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 45));
		icon_64 = new ImageIcon(this.getClass().getResource("icon_64.png"));
		label_logo.setIcon(icon_64);
		label_logo.setBounds(50, 0, 300, 64);
		panel_credenciales.add(label_logo);

		email_login = new JTextField(30);
		email_login.setBackground(Color.WHITE);
		email_login.setForeground(Color.BLACK);
		email_login.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));
		email_login.setBounds(0, 70, 400, 40);
		placeholder = new TextPrompt("Introduzca su e-mail", email_login);
		placeholder.changeAlpha(0.5f);
		placeholder.changeStyle(Font.ITALIC);
		panel_credenciales.add(email_login);

		password_login = new JPasswordField(15);
		password_login.setBackground(Color.WHITE);
		password_login.setForeground(Color.BLACK);
		password_login.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));
		password_login.setBounds(0, 125, 400, 40);
		placeholder = new TextPrompt("Introduzca su contrase\u00f1a", password_login);
		placeholder.changeAlpha(0.5f);
		placeholder.changeStyle(Font.ITALIC);
		panel_credenciales.add(password_login);

		btn_login = new JButton();
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validar()) {
					panel_credenciales.setVisible(false);
					label_cargar.setVisible(true);
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							MainWindow.setInterfaz(Thread.currentThread());
							try {
								user = new Usuarios(email_login.getText(),
										String.valueOf(password_login.getPassword()));
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							p = new PeticionLogin(user);
							sender = SSLConexion.getSender();
							sender.setPeticion(p);
							sender.interrupt();
							try {
								TimeUnit.SECONDS.sleep(5);
								label_cargar.setVisible(false);
								panel_credenciales.setVisible(true);
								label_error.setText(" Error al conectar al servidor.");
								label_error.setVisible(true);
								new Timer().schedule(new TimerTask() {
									@Override
									public void run() {
										label_error.setVisible(false);
									}
								}, 2000);
							} catch (InterruptedException e) {
								switch (user.getNombre()) {
								case "nouser":
									placeholder = new TextPrompt("Introduzca su e-mail", email_login);
									label_cargar.setVisible(false);
									email_login.setBorder(new LineBorder(Color.RED));
									panel_credenciales.setVisible(true);
									label_error.setText(" El e-mail no existe");
									label_error.setVisible(true);
									new Timer().schedule(new TimerTask() {
										@Override
										public void run() {
											label_error.setVisible(false);
										}
									}, 2000);
									break;
								case "nopass":
									placeholder = new TextPrompt("Introduzca su contrase\u00f1a", password_login);
									label_cargar.setVisible(false);
									password_login.setBorder(new LineBorder(Color.RED));
									panel_credenciales.setVisible(true);
									label_error.setText(" La contrase\u00f1a no existe");
									label_error.setVisible(true);
									new Timer().schedule(new TimerTask() {
										@Override
										public void run() {
											label_error.setVisible(false);
										}
									}, 2000);
									break;
								default:
									contentPane_login.setVisible(false);
									setContentPane(contentPane_conver);
									contentPane_conver.setVisible(true);
									contentPane_conver.add(label_error);
									contentPane_conver.add(label_cargar);
									label_nombre_usuario.setText(user.getNombre() + " " + user.getApellidos());
									label_email_usuario.setText(user.getEmail());
									label_cargar.setVisible(true);
									new Timer().schedule(new TimerTask() {
										@Override
										public void run() {
											MainWindow.setInterfaz(Thread.currentThread());
											p = new PeticionUpdateUsuarios(user);
											sender.setPeticion(p);
											sender.interrupt();
											try {
												TimeUnit.SECONDS.sleep(10);
											} catch (InterruptedException e1) {
												p = new PeticionUPDATEConv(user);
												sender.setPeticion(p);
												sender.interrupt();
												try {
													TimeUnit.SECONDS.sleep(10);
												} catch (InterruptedException e2) {
													cargarConvers();												
													/*Mensajes*/
													p = new PeticionUPDATEMsg(user);
													sender.setPeticion(p);
													sender.interrupt();
													try {
														TimeUnit.SECONDS.sleep(10);
													} catch (InterruptedException e3) {
														System.out.println(usuarios.size()+" "+convers.size()+" "+mensajes.size());
													}
												}
											}
										}
									},1000);
									
									break;
								}
							}
						}
					}, 1000);
				}
			}
		});
		btn_login.setText("Iniciar Sesi\u00f3n");
		btn_login.setForeground(mainColor);
		btn_login.setBackground(Color.BLACK);
		btn_login.setOpaque(false);
		btn_login.setBorder(new LineBorder(mainColor));
		btn_login.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 26));
		btn_login.setBounds(100, 190, 200, 45);
		panel_credenciales.add(btn_login);

		contentPane_login.add(panel_credenciales);

		panel_fondo_login = new JPanel();
		panel_fondo_login.setBounds(0, 0, getSize().width, getSize().height);
		panel_fondo_login.setBackground(new Color(0, 0, 0, 100));
		contentPane_login.add(panel_fondo_login);

		label_fondo_login = new JLabel();
		label_fondo_login.setBounds(0, 0, getSize().width, getSize().height);
		fondo_login = new ImageIcon(this.getClass().getResource("fondo_login.jpg"));
		label_fondo_login.setIcon(fondo_login);
		contentPane_login.add(label_fondo_login);

		/******************** CONTENT PANEL 2 ****************************/

		contentPane_conver = new JPanel();
		contentPane_conver.setVisible(false);
		contentPane_conver.setLayout(null);
		
//		contentPane_login.setVisible(false);//Eliminar
//		setContentPane(contentPane_conver);//Eliminar
//		contentPane_conver.setVisible(true);//Eliminar
		
		panel_datos_usuario = new JPanel();
		panel_datos_usuario.setBounds(0, 0, 400, 138);
		panel_datos_usuario.setBackground(new Color(240, 240, 240));
		panel_datos_usuario.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_datos_usuario.setLayout(null);
		contentPane_conver.add(panel_datos_usuario);

		label_img_usuario = new JLabel("");
		img_usuario = new ImageIcon(this.getClass().getResource("img_usuario.png"));
		label_img_usuario.setIcon(img_usuario);
		label_img_usuario.setBounds(5, 5, 128, 128);
		panel_datos_usuario.add(label_img_usuario);

		label_nombre_usuario = new JLabel("");
		label_nombre_usuario.setForeground(Color.BLACK);
		label_nombre_usuario.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		label_nombre_usuario.setBounds(145, 30, 300, 35);
		panel_datos_usuario.add(label_nombre_usuario);

		label_email_usuario = new JLabel("");
		label_email_usuario.setForeground(Color.BLACK);
		label_email_usuario.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		label_email_usuario.setBounds(145, 70, 300, 35);
		panel_datos_usuario.add(label_email_usuario);

		panel_lista = new JPanel();
		panel_lista.setLayout(new GridLayout(11, 1));
		scroll_lista = new JScrollPane(panel_lista, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_lista.setBounds(0, 138, 400, getSize().height - 180);
		scroll_lista.setBackground(new Color(240, 240, 240));
		scroll_lista.setBorder(new LineBorder(Color.LIGHT_GRAY));
		contentPane_conver.add(scroll_lista);

		JPanel panel_btn_lista = new JPanel();
		panel_btn_lista.setLayout(new GridLayout(0, 2));
		panel_btn_lista.setBounds(0, getSize().height - 42, 400, 42);
		panel_btn_lista.setBackground(new Color(240, 240, 240));
		panel_btn_lista.setBorder(new LineBorder(Color.LIGHT_GRAY));
		contentPane_conver.add(panel_btn_lista);

		btn_nueva = new JButton();
		btn_nueva.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarUsuarios();
			}
		});
		btn_nueva.setText(" Nueva Conversaci\u00f3n ");
		btn_nueva.setForeground(Color.BLACK);
		btn_nueva.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
		btn_nueva.setBackground(Color.BLACK);
		btn_nueva.setOpaque(false);
		panel_btn_lista.add(btn_nueva);

		btn_ver_conver = new JButton();
		btn_ver_conver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					cargarConvers();
			}
		
			});
		btn_ver_conver.setText(" Conversaciones ");
		btn_ver_conver.setForeground(Color.BLACK);
		btn_ver_conver.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
		btn_ver_conver.setBackground(Color.BLACK);
		btn_ver_conver.setOpaque(false);
		panel_btn_lista.add(btn_ver_conver);

		panel_conver = new JPanel();
		panel_conver.setBounds(400, 0, getSize().width -400, getSize().height);
		panel_conver.setOpaque(false);
		panel_conver.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_conver.setLayout(null);
		panel_conver.setVisible(false);
		contentPane_conver.add(panel_conver);

		panel_datos_conver = new JPanel();
		panel_datos_conver.setBounds(0, 0, panel_conver.getSize().width, 70);
		panel_datos_conver.setBackground(new Color(240, 240, 240));
		panel_datos_conver.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_datos_conver.setLayout(null);
		panel_conver.add(panel_datos_conver);

		label_img_conver = new JLabel("");
		img_conver = new ImageIcon(this.getClass().getResource("img_conver.png"));
		label_img_conver.setIcon(img_conver);
		label_img_conver.setBounds(5, 5, 64, 64);
		panel_datos_conver.add(label_img_conver);

		label_nombre_conver = new JLabel("");
		label_nombre_conver.setForeground(Color.BLACK);
		label_nombre_conver.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		label_nombre_conver.setBounds(90, 5, 150, 64);
		panel_datos_conver.add(label_nombre_conver);

		panel_chat = new JPanel();
		//panel_chat.setBorder(new EmptyBorder(20,20,20,20));
		panel_chat.setLayout(null);
		//panel_chat.setLayout(new BorderLayout());
		// panel_chat.setLayout(new BoxLayout(panel_chat, BoxLayout.PAGE_AXIS));
		panel_chat.setOpaque(false);
		scroll_chat = new JScrollPane(panel_chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_chat.setLayout(new ScrollPaneLayout());
		scroll_chat.setBounds(0, 70, panel_conver.getSize().width,
		panel_conver.getSize().height - 130);
		scroll_chat.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scroll_chat.setOpaque(false);
		panel_conver.add(scroll_chat);

//		JLabel label_fondo_chat = new JLabel();
//		label_fondo_chat.setBounds(0, 0, scroll_chat.getSize().width, scroll_chat.getSize().height);
//		Icon fondo_chat = new ImageIcon(new ImageIcon(this.getClass().getResource("fondo_conver.jpg")).getImage()
//				.getScaledInstance(label_fondo_chat.getWidth(), label_fondo_chat.getHeight(), Image.SCALE_DEFAULT));
//		label_fondo_chat.setIcon(fondo_chat);
//		panel_chat.add(label_fondo_chat);

		panel_input = new JPanel();
		panel_input.setBounds(0, panel_conver.getSize().height - 60, panel_conver.getSize().width, 60);
		panel_input.setBackground(new Color(240, 240, 240));
		panel_input.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_input.setLayout(null);
		panel_conver.add(panel_input);

		text_area_input = new JTextArea();
		text_area_input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (text_area_input.getLineCount() < 5) {
					scroll_chat.setBounds(0, 70, panel_conver.getSize().width,
							panel_conver.getSize().height -70 - 60 * (text_area_input.getLineCount()));
					scroll_chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					panel_input.setBounds(0, panel_conver.getSize().height - 60 * (text_area_input.getLineCount()),
							panel_conver.getSize().width, 60 * (text_area_input.getLineCount()));
					scroll_input.setBounds(20, 10, panel_input.getSize().width * 7 / 8,
							panel_input.getSize().height - 20);
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		text_area_input.setForeground(Color.BLACK);
		text_area_input.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
		text_area_input.setWrapStyleWord(true);
		scroll_input = new JScrollPane(text_area_input, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_input.setBounds(20, 10, panel_input.getSize().width * 7 / 8, panel_input.getSize().height - 20);
		scroll_input.setOpaque(false);
		placeholder = new TextPrompt("Introduzca su mensaje aqu\u00ed", text_area_input);
		placeholder.changeAlpha(0.5f);
		text_area_input.setBorder(new EmptyBorder(5, 10, 5, 10));
		text_area_input.setLineWrap(true);
		panel_input.add(scroll_input);

		btn_enviar = new JButton();
		btn_enviar.setForeground(Color.BLACK);
		btn_enviar.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
		btn_enviar.setText("Enviar");
		btn_enviar.setBackground(mainColor);
		btn_enviar.setBorder(new LineBorder(Color.GRAY));
		btn_enviar.setBounds(panel_input.getSize().width * 7 / 8 + 30, 10, panel_input.getSize().width / 8 - 40,
				panel_input.getSize().height - 20);
		panel_input.add(btn_enviar);
		btn_enviar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				PeticionMensaje p2=null;
				PeticionMensaje p3=null;
				try {
					p2 = new PeticionMensaje(new Mensajes("client3","client1",Security.cifrar(text_area_input.getText(),SSLConexion.getCert()),1));
					p3 = new PeticionMensaje(new Mensajes("client3","client3",Security.cifrar(text_area_input.getText(),Security.propioCert()),1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sender.setPeticion(p2);
				sender.interrupt();//sender.notify()
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sender.setPeticion(p3);
				sender.interrupt();//sender.notify()
			}
			
		});

		label_fondo_conver = new JLabel();
		label_fondo_conver.setBounds(0, 0, getSize().width, getSize().height);
		fondo_conver = new ImageIcon(this.getClass().getResource("fondo_conver.jpg"));
		label_fondo_conver.setIcon(fondo_conver);
		contentPane_conver.add(label_fondo_conver);
		

	}
	private static boolean validar() {
		int errors = 0;
		int email_lenght = email_login.getText().length();
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher mather = pattern.matcher(email_login.getText());
		Boolean match = mather.find();
		if (!match) {
			email_login.setBorder(new LineBorder(Color.RED));
			email_login.removeAll();
			if (email_login.getText().length() == 0) {
				placeholder = new TextPrompt("Este campo es obligatorio", email_login);
			} else {
				placeholder = new TextPrompt("Introduzca su e-mail", email_login);
				label_cargar.setVisible(false);
				email_login.setBorder(new LineBorder(Color.RED));
				panel_credenciales.setVisible(true);
				label_error.setText(" El e-mail es incorrecto");
				label_error.setVisible(true);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						label_error.setVisible(false);
					}
				}, 2000);
			}
			placeholder.setForeground(Color.RED);
			placeholder.changeAlpha(0.5f);
			placeholder.changeStyle(Font.ITALIC);
			errors++;
		} else {
			email_login.setBorder(new EmptyBorder(email_login.getInsets()));
			email_login.removeAll();
			placeholder = new TextPrompt("Introduzca su e-mail", email_login);
			placeholder.changeAlpha(0.5f);
			placeholder.changeStyle(Font.ITALIC);
		}

		if (password_login.getPassword().length < 5) {
			password_login.setBorder(new LineBorder(Color.RED));
			password_login.removeAll();
			if (password_login.getPassword().length == 0) {
				placeholder = new TextPrompt("Este campo es obligatorio", password_login);
			} else {
				placeholder = new TextPrompt("Introduzca su contrase\u00f1a", password_login);
				label_cargar.setVisible(false);
				password_login.setText(null);
				password_login.setBorder(new LineBorder(Color.RED));
				panel_credenciales.setVisible(true);
				if (!match && email_lenght > 0)
					label_error.setText(" El e-mail y la contrase\u00f1a son incorrectos");
				else
					label_error.setText(" La contrase\u00f1a es incorrecta");
				label_error.setVisible(true);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						label_error.setVisible(false);
					}
				}, 2000);
			}
			placeholder.setForeground(Color.RED);
			placeholder.changeAlpha(0.5f);
			placeholder.changeStyle(Font.ITALIC);
			errors++;
		} else {
			password_login.setBorder(new EmptyBorder(password_login.getInsets()));
			password_login.removeAll();
			placeholder = new TextPrompt("Introduzca su contrase\u00f1a", password_login);
			placeholder.changeAlpha(0.5f);
			placeholder.changeStyle(Font.ITALIC);
		}
		if(errors==0) {
			return true;
		}else {
			return false;
		}
	}
	
	private static void cargarConvers() {
		panel_lista.removeAll();
		for (int i = 0; i < convers.size(); i++) {
			btn_conver = new JButton();
			final int ind=i;
			btn_conver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					p = new PeticionCertificado(convers.get(ind).getIdConversacion(),getUser().getId());
					sender.setPeticion(p);
					sender.interrupt();
					try{
						TimeUnit.SECONDS.sleep(1);
					}catch(InterruptedException e2){
					}
						panel_chat.removeAll();
						label_nombre_conver.setText(btn_conver.getText());
						JLabel label_msg_chat = null;
						for (int i = 0; i < mensajes.size(); i++) {
							if(mensajes.get(i).getEmisor().compareTo(user.getId())==0) {
								try {
									label_msg_chat = new JLabel(Security.descrifrar(mensajes.get(i).getTexto(),Security.propioCert()));
									label_msg_chat.setBounds(scroll_chat.getSize().width-250, 100 * i + 50, 200, 50);
									label_msg_chat.setBackground(Color.GREEN);
								} catch (Exception e1) {
									System.out.println(e1.getMessage());
								}
							}else{
								try {
									label_msg_chat = new JLabel(Security.descrifrar(mensajes.get(i).getTexto(),SSLConexion.getCert()));
									label_msg_chat.setBounds(50, 100 * i + 50, 200, 50);
									label_msg_chat.setBackground(Color.YELLOW);
								} catch (Exception e1) {
									System.out.println(e1.getMessage());
								}
							}
							label_msg_chat.setBorder(new LineBorder(Color.LIGHT_GRAY));
							label_msg_chat.setForeground(Color.BLACK);
							label_msg_chat.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 16));
							label_msg_chat.setOpaque(true);
							panel_chat.add(label_msg_chat);
						}
						panel_conver.setVisible(true);
						panel_conver.paintAll(panel_conver.getGraphics());
				}
			});
			img_conver = new ImageIcon(MainWindow.class.getResource("img_conver.png"));
			btn_conver.setIcon(img_conver);
			btn_conver.setText(String.valueOf(convers.get(i).getIdConversacion()));
			btn_conver.setForeground(Color.BLACK);
			btn_conver.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
			btn_conver.setBackground(Color.BLACK);
			btn_conver.setOpaque(false);
			panel_lista.add(btn_conver);
	}
		
		btn_ver_conver.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		btn_ver_conver.setBackground(mainColor);
		btn_ver_conver.setOpaque(true);
		btn_nueva.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
		btn_nueva.setBackground(Color.BLACK);
		btn_nueva.setOpaque(false);
		if (convers.size() < 11)
			panel_lista.setLayout(new GridLayout(11, 1));
		else
			panel_lista.setLayout(new GridLayout(0, 1));
		panel_lista.paintAll(panel_lista.getGraphics());

	}
	
	private static void cargarUsuarios() {
		panel_lista.removeAll();
		for (int i = 0; i < usuarios.size(); i++) {
			btn_conver = new JButton();
			btn_conver.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					panel_chat.removeAll();
					label_nombre_conver.setText(btn_conver.getText());
					panel_conver.setVisible(true);
					panel_conver.paintAll(panel_conver.getGraphics());
				}
			});
			img_conver = new ImageIcon(MainWindow.class.getResource("img_conver.png"));
			btn_conver.setIcon(img_conver);
			btn_conver.setText(usuarios.get(i).getNombre());
			btn_conver.setForeground(Color.BLACK);
			btn_conver.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
			btn_conver.setBackground(Color.BLACK);
			btn_conver.setOpaque(false);
			panel_lista.add(btn_conver);
	}
		
		btn_nueva.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		btn_nueva.setBackground(mainColor);
		btn_nueva.setOpaque(true);
		btn_ver_conver.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
		btn_ver_conver.setBackground(Color.BLACK);
		btn_ver_conver.setOpaque(false);
		if (usuarios.size() < 11)
			panel_lista.setLayout(new GridLayout(11, 1));
		else
			panel_lista.setLayout(new GridLayout(0, 1));
		panel_lista.paintAll(panel_lista.getGraphics());
	
	}
	public static SSLConexion getSsl() {
		return ssl;
	}

	public static void setSsl(SSLConexion ssl) {
		MainWindow.ssl = ssl;
	}

	public static Usuarios getUser() {
		return user;
	}

	public static void setUser(Usuarios user) {
		MainWindow.user = user;
	}

	public static Thread getInterfaz() {
		return interfaz;
	}

	public static void setInterfaz(Thread interfaz) {
		MainWindow.interfaz = interfaz;
	}
}
