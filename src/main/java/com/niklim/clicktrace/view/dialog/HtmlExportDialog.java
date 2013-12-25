package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.service.HtmlExportService;
import com.niklim.clicktrace.service.exception.HtmlExportException;

public class HtmlExportDialog extends AbstractDialog {
	private static final Logger log = LoggerFactory.getLogger(HtmlExportDialog.class);

	@Inject
	private HtmlExportService htmlExportService;

	@Inject
	private ActiveSession activeSession;

	private JTextField outputDirPath;
	private JFileChooser outputDirFileChooser;
	private JCheckBox simpleHtml;

	public HtmlExportDialog() {
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]rel[]"));
		dialog.setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490, 150);

		createConfigPanel();
		dialog.add(createControlPanel("Export"), "align r, span 3");
	}

	private void createConfigPanel() {
		outputDirPath = new JTextField();
		outputDirFileChooser = new JFileChooser();
		outputDirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		simpleHtml = new JCheckBox();

		JButton setPathButton = new JButton("Select");
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
		dialog.add(new JLabel("Simple HTML"));
		dialog.add(simpleHtml, "wrap");
	}

	@Override
	public void okAction() {
		try {
			htmlExportService.export(activeSession.getSession(), outputDirPath.getText(), simpleHtml.isSelected());
			JOptionPane.showMessageDialog(dialog, InfoMsgs.HTML_EXPORT_SUCCESS);
			close();
		} catch (HtmlExportException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} catch (IOException e) {
			log.error("", e);
			JOptionPane.showMessageDialog(dialog, ErrorMsgs.HTML_EXPORT_IO_ERROR);
		}
	}

	public void open() {
		dialog.setVisible(true);
	}
}
