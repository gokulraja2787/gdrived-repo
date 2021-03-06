package com.ezapp.cloudsyncer.gdrive.d.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;

import com.ezapp.cloudsyncer.gdrive.d.Main;
import com.ezapp.cloudsyncer.gdrive.d.log.LogManager;
import com.ezapp.cloudsyncer.gdrive.d.ui.event.listener.ThemeContextListener;

/**
 * Unit responsible for Sys tray
 * 
 * @author grangarajan
 *
 */
public class SysTray {

	/**
	 * LOGGER
	 */
	private static Logger LOGGER = LogManager.getLogger(SysTray.class);

	/**
	 * Application context listener
	 */
	private static GdrivedContextListener applicationContextListener = new GdrivedContextListener();

	/**
	 * Theme context listener
	 */
	private static ThemeContextListener themeContextListner = new ThemeContextListener();
	
	private static boolean sysTrayInitialized = false;

	/**
	 * Tray icon
	 */
	private static TrayIcon trayIcon;

	/**
	 * Sets up System Tray
	 */
	public static void initSysTray() {
		if (!sysTrayInitialized && SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			URL fileUrl = Thread
					.currentThread()
					.getContextClassLoader()
					.getResource(
							"com/ezapp/cloudsyncer/gdrive/d/images/app40X40-ico.png");
			ImageIcon imageIcon = new ImageIcon(fileUrl);
			Image image = imageIcon.getImage();
			PopupMenu popup = new PopupMenu();
			MenuItem addActItem = new MenuItem(
					GdrivedContextListener.ACTION_COMMAND.ADD_ACCOUNT
							.getValue());
			MenuItem exitItem = new MenuItem(
					GdrivedContextListener.ACTION_COMMAND.EXIT.getValue());
			MenuItem toggleShowHideItm = new MenuItem(
					GdrivedContextListener.ACTION_COMMAND.SHOW_HIDE.getValue());
			PopupMenu configMenuItem = new PopupMenu(
					GdrivedContextListener.ACTION_COMMAND.CONFIG.getValue());
			PopupMenu themeItem = new PopupMenu(
					GdrivedContextListener.ACTION_COMMAND.THEME.getValue());
			popup.add(addActItem);
			popup.add(toggleShowHideItm);
			configMenuItem.add(themeItem);
			popup.add(configMenuItem);
			popup.add(exitItem);
			toggleShowHideItm.addActionListener(applicationContextListener);
			toggleShowHideItm
					.setActionCommand(GdrivedContextListener.ACTION_COMMAND.SHOW_HIDE
							.getValue());
			addActItem.addActionListener(applicationContextListener);
			addActItem
					.setActionCommand(GdrivedContextListener.ACTION_COMMAND.ADD_ACCOUNT
							.getValue());
			exitItem.addActionListener(applicationContextListener);
			exitItem.setActionCommand(GdrivedContextListener.ACTION_COMMAND.EXIT
					.getValue());
			String[] vals = RunnerUIFactory.getImpls();
			for (String val : vals) {
				MenuItem item = new MenuItem(val);
				item.addActionListener(themeContextListner);
				item.setActionCommand(val);
				themeItem.add(item);
			}
			popup.addSeparator();
			MenuItem temm = new MenuItem("Delete my gmail account");
			temm.addActionListener(applicationContextListener);
			temm.setActionCommand("DELETE");
			popup.add(temm);
			trayIcon = new TrayIcon(image,
					"gdrive-d: Cloud syncer for Google drive", popup);
			trayIcon.setImageAutoSize(true);
			try {
				tray.add(trayIcon);
				sysTrayInitialized = true;
			} catch (AWTException e) {
				LOGGER.error("Error while setting up tray: " + e.getMessage(),
						e);
			}
		} else {
			LOGGER.warn("System Tray is not supported");
		}
	}

	/**
	 * Shuts down system tray
	 */
	public static void shutDown() {
		if (sysTrayInitialized && null != trayIcon && SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			tray.remove(trayIcon);
			trayIcon = null;
		}

	}

}

/**
 * Application context listener
 * 
 * @author grangarajan
 *
 */
class GdrivedContextListener implements ActionListener {

	/**
	 * Enums for context commands
	 * 
	 * @author grangarajan
	 *
	 */
	static enum ACTION_COMMAND {
		/**
		 * Show Hide main UI
		 */
		SHOW_HIDE("Show/Hide gdrive-d"),
		/**
		 * Add account
		 */
		ADD_ACCOUNT("Add Account"),
		/**
		 * Configure
		 */
		CONFIG("Configure"),
		/**
		 * Theme
		 */
		THEME("Theme"),
		/**
		 * Exit
		 */
		EXIT("Exit");

		/**
		 * Holds context value
		 */
		private String value;

		/**
		 * Constructor
		 * 
		 * @param value
		 */
		ACTION_COMMAND(String value) {
			this.value = value;
		}

		/**
		 * Get ENUM value
		 * 
		 * @return value
		 */
		public String getValue() {
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		if (command.equals(GdrivedContextListener.ACTION_COMMAND.EXIT
				.getValue())) {
			Main.shutDown(1);
		} else if (command
				.equals(GdrivedContextListener.ACTION_COMMAND.ADD_ACCOUNT
						.getValue())) {
			Main.addAccount();
		} else if (command
				.equals(GdrivedContextListener.ACTION_COMMAND.SHOW_HIDE
						.getValue())) {
			Main.toggleShowHideMainUI();
		} else { // TODO remove this block
			Main.deleteAccount("gokulraja2006@gmail.com");
		}
	}

}