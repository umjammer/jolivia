package org.dyndns.jkiddo;

import gmusic.api.impl.InvalidCredentialsException;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TrayIcon;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.desk.GoogleStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Window
{

	private JFrame frmJolivia;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JTextField textField;
	private JButton btnNewButton;
	private JFileChooser fc;
	private JButton btnNewButton_1;
	protected File path = new File(System.getProperty("user.home"));
	protected ExecutorService executor;
	private JCheckBox chckbxUseGoogleMusic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
					{
						if("Nimbus".equals(info.getName()))
						{
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					Window window = new Window();
					window.frmJolivia.setVisible(true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window()
	{
		executor = Executors.newSingleThreadExecutor();
		fc = new JFileChooser(System.getProperty("user.home"));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmJolivia = new JFrame();
		frmJolivia.setTitle("Jolivia");
		frmJolivia.setResizable(false);
		frmJolivia.setBounds(100, 100, 450, 258);
		frmJolivia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJolivia.getContentPane().setLayout(null);

		chckbxUseGoogleMusic = new JCheckBox("Use Google Music as backend");
		chckbxUseGoogleMusic.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.DESELECTED)
				{
					txtUsername.setEnabled(false);
					pwdPassword.setEnabled(false);
					btnNewButton.setEnabled(true);
					textField.setEnabled(true);
				}
				else if(e.getStateChange() == ItemEvent.SELECTED)
				{
					txtUsername.setEnabled(true);
					pwdPassword.setEnabled(true);
					btnNewButton.setEnabled(false);
					textField.setEnabled(false);
				}
			}
		});
		chckbxUseGoogleMusic.setBounds(21, 22, 200, 18);
		frmJolivia.getContentPane().add(chckbxUseGoogleMusic);

		txtUsername = new JTextField();
		txtUsername.setEnabled(false);
		txtUsername.setToolTipText("Username");
		txtUsername.setBounds(87, 52, 122, 28);
		frmJolivia.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);

		pwdPassword = new JPasswordField();
		pwdPassword.setEnabled(false);
		pwdPassword.setToolTipText("Password");
		pwdPassword.setBounds(87, 80, 122, 28);
		frmJolivia.getContentPane().add(pwdPassword);

		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(6, 58, 69, 16);
		frmJolivia.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(6, 86, 69, 16);
		frmJolivia.getContentPane().add(lblNewLabel_1);

		btnNewButton = new JButton("Select path");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = fc.showDialog(frmJolivia, "Here");
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					path = fc.getSelectedFile();
					textField.setText(path.getAbsolutePath());
				}
			}
		});
		btnNewButton.setBounds(87, 120, 122, 28);
		frmJolivia.getContentPane().add(btnNewButton);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(87, 150, 315, 28);
		frmJolivia.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(path.getAbsolutePath());

		JLabel lblPath = new JLabel("Path:");
		lblPath.setBounds(47, 156, 28, 16);
		frmJolivia.getContentPane().add(lblPath);

		btnNewButton_1 = new JButton("AWESOMENESS!");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				btnNewButton_1.setText("Now go to iTunes!");
				btnNewButton_1.setEnabled(false);
				txtUsername.setEnabled(false);
				pwdPassword.setEnabled(false);
				btnNewButton.setEnabled(false);
				textField.setEnabled(false);
				chckbxUseGoogleMusic.setEnabled(false);
				executor.execute(new Runnable() {

					@Override
					public void run()
					{
						try
						{
							IMusicStoreReader reader = null;
							if(chckbxUseGoogleMusic.isSelected())
							{
								reader = new GoogleStoreReader(txtUsername.getText(), new String(pwdPassword.getPassword()));
								new GReporter(txtUsername.getText());
							}
							else
							{
								reader = new DeskMusicStoreReader(path);
								new GReporter("local version");
							}
							new Jolivia.JoliviaBuilder().port(4000).pairingCode(1337).musicStoreReader(reader).build();
						}
						catch(InvalidCredentialsException ice)
						{
							handleInvalidCredentials();
						}
						catch(Exception ee)
						{
							ee.printStackTrace();
							onShutdown();
						}
					}
				});
			}
		});
		btnNewButton_1.setBounds(87, 184, 315, 34);
		frmJolivia.getContentPane().add(btnNewButton_1);
		setupGui();
	}

	private void handleInvalidCredentials()
	{
		JOptionPane.showMessageDialog(frmJolivia, "Don't! - Do it more right ...");
		btnNewButton_1.setText("AWESOMENESS!");
		btnNewButton_1.setEnabled(true);
		txtUsername.setEnabled(true);
		pwdPassword.setEnabled(true);
		chckbxUseGoogleMusic.setEnabled(true);
		btnNewButton.setEnabled(false);
		textField.setEnabled(false);
	}

	private void setupGui()
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				onShutdown();
			}

		}));

		try
		{
			/* Create about dialog */
			final Dialog aboutDialog = new Dialog((Dialog) null);
			final GridBagLayout aboutLayout = new GridBagLayout();
			aboutDialog.setLayout(aboutLayout);
			aboutDialog.setVisible(false);
			aboutDialog.setTitle("About Jolivia");
			aboutDialog.setResizable(false);
			{
				/* Message */
				final TextArea title = new TextArea(AboutMessage.split("\n").length + 1, 64);
				title.setText(AboutMessage);
				title.setEditable(false);
				final GridBagConstraints titleConstraints = new GridBagConstraints();
				titleConstraints.gridx = 1;
				titleConstraints.gridy = 1;
				titleConstraints.fill = GridBagConstraints.HORIZONTAL;
				titleConstraints.insets = new Insets(0, 0, 0, 0);
				aboutLayout.setConstraints(title, titleConstraints);
				aboutDialog.add(title);
			}
			{
				/* Done button */
				final Button aboutDoneButton = new Button("Done");
				aboutDoneButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent evt)
					{
						aboutDialog.setVisible(false);
					}
				});
				final GridBagConstraints aboutDoneConstraints = new GridBagConstraints();
				aboutDoneConstraints.gridx = 1;
				aboutDoneConstraints.gridy = 2;
				aboutDoneConstraints.anchor = GridBagConstraints.PAGE_END;
				aboutDoneConstraints.fill = GridBagConstraints.NONE;
				aboutDoneConstraints.insets = new Insets(0, 0, 0, 0);
				aboutLayout.setConstraints(aboutDoneButton, aboutDoneConstraints);
				aboutDialog.add(aboutDoneButton);
			}
			aboutDialog.setVisible(false);
			aboutDialog.setLocationByPlatform(true);
			aboutDialog.pack();

			/* Create tray icon */
			final URL trayIconUrl = Jolivia.class.getClassLoader().getResource("icon_32.png");
			if(trayIconUrl == null)
			{
				throw new Exception("No image found");
			}
			TrayIcon trayIcon = new TrayIcon((new ImageIcon(trayIconUrl, "Jolivia").getImage()));
			trayIcon.setToolTip("Jolivia");
			trayIcon.setImageAutoSize(true);
			final PopupMenu popupMenu = new PopupMenu();
			final MenuItem aboutMenuItem = new MenuItem("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt)
				{
					aboutDialog.setLocationByPlatform(true);
					aboutDialog.setVisible(true);
				}
			});
			popupMenu.add(aboutMenuItem);
			final MenuItem exitMenuItem = new MenuItem("Quit");
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent evt)
				{
					onShutdown();
				}
			});
			popupMenu.add(exitMenuItem);
			trayIcon.setPopupMenu(popupMenu);
			SystemTray.getSystemTray().add(trayIcon);

			// logger.info("Running with GUI, created system tray icon and menu");
		}
		catch(Exception e)
		{
			// logger.info("Running headless", e);
		}
	}

	protected void onShutdown()
	{
		System.exit(0);
	}

	private final String AboutMessage = "   * Jolivia *\n" + "\n" + "Copyright (c) 2013 Jens Kristian Villadsen\n" + "\n" + "Jolivia is free software: you can redistribute it and/or modify\n" + "it under the terms of the GNU General Public License as published by\n" + "the Free Software Foundation, either version 3 of the License, or\n" + "(at your option) any later version.\n" + "\n" + "didms is distributed in the hope that it will be useful,\n" + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n" + "GNU General Public License for more details.\n" + "\n" + "You should have received a copy of the GNU General Public License\n" + "along with didms.  If not, see <http://www.gnu.org/licenses/>." + "\n\n";
}
