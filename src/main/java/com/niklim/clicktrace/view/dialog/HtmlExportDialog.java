package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.niklim.clicktrace.Messages;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.service.HtmlExportService;
import com.niklim.clicktrace.service.exception.HtmlExportException;

public class HtmlExportDialog extends AbstractDialog {
	@Inject
	private HtmlExportService htmlExportService;

	@Inject
	private ActiveSession activeSession;

	private JTextField outputDirPath;
	JFileChooser outputDirFileChooser;

	public HtmlExportDialog() {
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]rel[]"));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490, 120);

		createImageEditorPathPanel();
		createControlPanel();
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
				} catch (HtmlExportException e) {
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
