package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.niklim.clicktrace.Messages;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.service.HtmlExportService;
import com.niklim.clicktrace.service.exception.HtmlExportAlreadyExistsException;
import com.niklim.clicktrace.view.MainFrameHolder;

public class HtmlExportDialog {
	@Inject
	private HtmlExportService htmlExportService;

	@Inject
	private ActiveSession activeSession;

	private JDialog dialog;
	private JTextField outputDirPath;
	JFileChooser outputDirFileChooser;

	public HtmlExportDialog() {
		dialog = new JDialog(MainFrameHolder.get(), true);
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]rel[]"));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490, 100);

		createImageEditorPathPanel();
		createControlPanel();

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void createImageEditorPathPanel() {
		outputDirPath = new JTextField();
		outputDirFileChooser = new JFileChooser();
		outputDirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JButton setPathButton = new JButton("select");
		setPathButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = outputDirFileChooser.showOpenDialog(dialog);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = outputDirFileChooser.getSelectedFile();
					outputDirPath.setText(file.getAbsolutePath());
				}
			}
		});

		dialog.add(new JLabel("Output folder path"));
		dialog.add(outputDirPath, "w 400");
		dialog.add(setPathButton, "wrap");
	}

	private void createControlPanel() {
		JButton exportButton = new JButton("Export");
		JButton cancelButton = new JButton("Cancel");
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					htmlExportService.export(activeSession.getSession(), outputDirPath.getText());
					JOptionPane.showMessageDialog(dialog, Messages.HTML_EXPORT_SUCCESS);
					dialog.setVisible(false);
				} catch (HtmlExportAlreadyExistsException e) {
					JOptionPane.showMessageDialog(dialog, e.getMessage());
				} catch (IOException e) {
					JOptionPane.showMessageDialog(dialog, Messages.HTML_EXPORT_IO_ERROR);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		cancelButton.setToolTipText("[Esc]");

		JPanel controlPanel = new JPanel(new MigLayout("align right"));
		controlPanel.add(exportButton, "w 80, tag apply");
		controlPanel.add(cancelButton, "w 80, tag cancel");
		dialog.add(controlPanel, "span 3, grow, h 50");
	}

	public void open() {
		dialog.setVisible(true);
	}
}
