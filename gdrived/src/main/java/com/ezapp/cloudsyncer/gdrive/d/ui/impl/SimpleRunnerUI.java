package com.ezapp.cloudsyncer.gdrive.d.ui.impl;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI;

/**
 * Simple interface definition
 * 
 * @author grangarajan
 * 
 *         Date: Mat 22, 2015
 *
 */
class SimpleRunnerUI implements RunnerUI {

	/**
	 * Add account frame
	 */
	private AddAccountFrame addAccountFrame;
	/**
	 * Main frame
	 */
	private MainFrame mainFrame;

	/**
	 * Initializes simple runner UI
	 */
	SimpleRunnerUI() {
		mainFrame = new MainFrame();
		addAccountFrame = new AddAccountFrame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#start()
	 */
	public void start() {
		invokeLater(mainFrame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#shutdown(int)
	 */
	public int shutdown(int statusCode) {
		if (statusCode != 0) {
			addAccountFrame.dispose();
		}
		return statusCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setOAuthURL(java.lang.String)
	 */
	public void setOAuthURL(String url) {
		if (null != addAccountFrame) {
			addAccountFrame.setOAuthURL(url);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#setImageIco(java.net.URL)
	 */
	public void setImageIco(URL url) {
		ImageIcon imageIcon = new ImageIcon(url);
		Image image = imageIcon.getImage();
		mainFrame.setIconImage(image);
		addAccountFrame.setIconImage(image);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#openAddAccountWindow()
	 */
	public void openAddAccountWindow() {
		invokeLater(addAccountFrame);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ezapp.cloudsyncer.gdrive.d.ui.RunnerUI#showHideMainUI()
	 */
	public void toggleShowHideMainUI() {
		if(mainFrame.isVisible()) {
			mainFrame.setVisible(false);
		} else {
			mainFrame.setVisible(true);
		}
	}

}
