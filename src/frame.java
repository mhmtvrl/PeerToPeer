import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class frame extends JFrame {
	public boolean flag = false;

	private JTextField textField;
	

	DefaultListModel left = new DefaultListModel();
	DefaultListModel right = new DefaultListModel();
	static Client c;
	String ipst;
	int index;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame frame = new frame();
					frame.setSize(490,360);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frame() {
		c = new Client();
		try {
			Scanner sc = new Scanner(new File("listOfFiles" + InetAddress.getLocalHost().getHostAddress()+".txt"));
			String line = sc.nextLine();
			String str[] = line.split(",");
			for(int i = 1; i < str.length; i++)
			{
				left.addElement(str[i]);
			}
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		getContentPane().setLayout(null);
		
		final JButton disconnect_button = new JButton("Disconnect");
		final JButton connect_button = new JButton("Connect");
		final JLabel lblPleaseEnterIp = new JLabel("IP Address:");
		final JButton transfer_button = new JButton("<=");
		textField = new JTextField();
		final JLabel ip = new JLabel();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		lblPleaseEnterIp.setBounds(0, 60, 71, 14);
		getContentPane().add(lblPleaseEnterIp);

		////////////////////////////////////////////////////////////////////////////////////////////////////	
		//Connect tusu
		connect_button.addMouseListener(new MouseAdapter() {
			@Override				
			public void mouseClicked(MouseEvent e) {
				
				ipst=textField.getText();
				
				c.connectToNetwork(ipst);
				System.out.println(ipst);
				c.getFile("listOfFiles"+ipst+".txt");
				
				if (c.connectToNetwork(ipst)==true){
					
					try {
						Scanner sc = new Scanner(new File("listOfFiles"+ipst+".txt"));
						String line = sc.nextLine();
						String str[] = line.split(",");
						ip.setText("Connected to " + str[0]);
						for(int i = 1; i < str.length; i++)
						{
							right.addElement(str[i]);
						}
						
						connect_button.setEnabled(false);
						disconnect_button.setEnabled(true);
						transfer_button.setEnabled(true);
						
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					System.out.println("false");
					JOptionPane.showMessageDialog(getContentPane(), "Connection Failed. Please try again", "Connection Failed",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		connect_button.setBounds(265, 55, 209, 24);
		getContentPane().add(connect_button);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		textField.setText("192.168.1.4");
		textField.addKeyListener(new KeyAdapter() {
			@Override				
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
			    if(e.getSource()==textField)
			    {
			        if(key==KeyEvent.VK_ENTER)
			        { ipst=textField.getText();
					
					c.connectToNetwork(ipst);
					System.out.println(ipst);
					c.getFile("listOfFiles"+ipst+".txt");
					
					if (c.connectToNetwork(ipst)==true){
						
						try {
							Scanner sc = new Scanner(new File("listOfFiles"+ipst+".txt"));
							String line = sc.nextLine();
							String str[] = line.split(",");
							ip.setText("Connected to " + str[0]);
							for(int i = 1; i < str.length; i++)
							{
								right.addElement(str[i]);
							}
							
							connect_button.setEnabled(false);
							disconnect_button.setEnabled(true);
							transfer_button.setEnabled(true);
							
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else{
						System.out.println("false");
						JOptionPane.showMessageDialog(getContentPane(), "Connection Failed. Please try again", "Connection Failed",JOptionPane.ERROR_MESSAGE);
					}
			        }
			    }
				
				
				
			}
		});
		textField.setBounds(68, 57, 129, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 96, 197, 180);
		getContentPane().add(scrollPane_1);
		
				
				JList left_list = new JList(left);
				scrollPane_1.setViewportView(left_list);
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(265, 113, 209, 163);
		getContentPane().add(scrollPane_2);
		
		
		final JList right_list = new JList(right);
		scrollPane_2.setViewportView(right_list);
		right_list.addMouseListener(new MouseAdapter() {					/////  SOL LÝSTE FONKSÝYON
			public void mouseClicked(MouseEvent evnt) {
				if (evnt.getClickCount() == 2) {
					//System.out.println(right.getElementAt(right_list.getSelectedIndex()));
					if(left.contains(right.getElementAt(right_list.getSelectedIndex()))){
						JOptionPane.showMessageDialog(getContentPane(), "File Overwritten", "Overwrite",JOptionPane.ERROR_MESSAGE);	
						}
					else{
						left.addElement(right.getElementAt(right_list.getSelectedIndex()));
					}
						
					
					c.connect(ipst);
					c.getFile("C:\\inetpub\\wwwroot\\" + right.getElementAt(right_list.getSelectedIndex()));
					c.getDirectory();
					
				}
			}
		});
		
			
		////////////////////////////////////////////////////////////////////////////////////////////////////

		
		
		transfer_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evnt) {			//// file transger
				if (evnt.getClickCount() == 1) {
					if(left.contains(right.getElementAt(right_list.getSelectedIndex()))){
						JOptionPane.showMessageDialog(getContentPane(), "File Overwritten", "Overwrite",JOptionPane.ERROR_MESSAGE);	
						}
					else{
						left.addElement(right.getElementAt(right_list.getSelectedIndex()));
					}
						
					
					c.connect(ipst);
					c.getFile("C:\\inetpub\\wwwroot\\" + right.getElementAt(right_list.getSelectedIndex()));
					c.getDirectory();
					
				}
					
			}
		});
		transfer_button.setBounds(198, 152, 65, 23);
		getContentPane().add(transfer_button);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		disconnect_button.setBounds(0, 287, 474, 24);
		disconnect_button.setEnabled(false);
		transfer_button.setEnabled(false);
		getContentPane().add(disconnect_button);
		
		JButton btnNewButton = new JButton("Start Server");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				c.getDirectory();
				 new Server();	
				 JOptionPane.showMessageDialog(getContentPane(), "Successfully Connected to Server", "Server",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setBounds(0, 11, 474, 23);
		getContentPane().add(btnNewButton);
		
		
		ip.setBounds(265, 96, 209, 14);
		getContentPane().add(ip);
		disconnect_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				c.disconnect();
				right.removeAllElements();
				connect_button.setEnabled(true);
				disconnect_button.setEnabled(false);
				transfer_button.setEnabled(false);
				ip.setText("");
				
}
		});
		
		 
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
	        
		
	        
	        getContentPane().setVisible(true);

}
}