package com.ezapp.cloudsyncer.gdrive.d.ui.nativeimpl;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Add account UI
 * 
 * Date: June 15, 2015
 * 
 * @author gokul
 *
 */
class AddAccountFrame {

	/**
	 * Holds shell
	 */
	private Shell shell;

	/**
	 * Holds display
	 */
	private Display display;

	/**
	 * Self refernce
	 */
	private AddAccountFrame self;

	/**
	 * Field to hold generated oauth URL
	 */
	private String oauthURLField = new String(
			"Please wait while URL is generated.");
	private Text oauthField;

	/**
	 * Construct Add Account frame
	 */
	AddAccountFrame(Display display) {
		this.display = display;
		self = this;
		initUI(display);
	}

	private void initUI(Display display) {
		shell = new Shell(display);
		shell.setText("Add account");
		shell.setSize(400, 399);
		shell.setLayout(null);

		Label lblAddAccount = new Label(shell, SWT.NONE);
		lblAddAccount.setForeground(SWTResourceManager.getColor(0, 0, 0));
		lblAddAccount.setFont(SWTResourceManager
				.getFont("Sans", 20, SWT.NORMAL));
		lblAddAccount.setAlignment(SWT.CENTER);
		lblAddAccount.setBounds(61, 12, 279, 32);
		lblAddAccount.setText("Add Google Account");

		Label lblOpenTheGenerated = new Label(shell, SWT.WRAP);
		lblOpenTheGenerated.setFont(SWTResourceManager.getFont("Sans", 14,
				SWT.NORMAL));
		lblOpenTheGenerated.setForeground(SWTResourceManager
				.getColor(SWT.COLOR_DARK_GRAY));
		lblOpenTheGenerated.setBounds(10, 62, 380, 155);
		lblOpenTheGenerated
				.setText("In order to start using gdrive cloud syncer you need to login to google account first. Click on the below button to authenticate using your google credential, then copy the code and paste it below.");

		Button btnLogin = new Button(shell, SWT.NONE);
		btnLogin.setBounds(10, 223, 380, 29);
		btnLogin.setText("Authentical using a Google credential");

		Label lblPaste = new Label(shell, SWT.NONE);
		lblPaste.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblPaste.setBounds(10, 280, 147, 17);
		lblPaste.setText("Paste the code here:");

		oauthField = new Text(shell, SWT.BORDER);
		oauthField.setBounds(144, 272, 246, 25);

		Button btnAddAccount = new Button(shell, SWT.NONE);
		btnAddAccount.setBounds(91, 329, 105, 29);
		btnAddAccount.setText("Add Account");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				self.close();
			}
		});
		btnCancel.setBounds(202, 329, 88, 29);
		btnCancel.setText("Cancel");
	}

	/**
	 * Opens the shell
	 */
	public void openAndStart() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (shell.isDisposed()) {
					initUI(display);
				}
				shell.open();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
			}

		});
	}

	/**
	 * Closes display & shell
	 */
	public void close() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!shell.isDisposed()) {
					shell.close();
				}
			}
		});
	}

	/**
	 * Set oAuth URL
	 * 
	 * @param url
	 */
	public void setOAuthURL(String url) {
		if (null != oauthURLField) {
			oauthURLField = url;
			/*
			 * if (null != authBrowser && !authBrowser.isClosed()) {
			 * authBrowser.openUrl(oauthURLField); }
			 */
		}
	}

	/**
	 * Set app image icon
	 * 
	 * @param url
	 */
	public void setAppImageIcon(URL url) {
		String urlloc = url.toString();
		try {
			urlloc = urlloc.substring(urlloc.indexOf("com/ezapp") - 1);
		} catch (Exception e) {
			urlloc = "/com/ezapp/cloudsyncer/gdrive/d/images/app-ico.png";
		}

		shell.setImage(SWTResourceManager.getImage(MainFrame.class, urlloc));
	}
}
