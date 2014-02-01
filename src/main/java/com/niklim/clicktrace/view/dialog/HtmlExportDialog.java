package com.niklim.clicktrace.view.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.exception.HtmlExportException;
import com.niklim.clicktrace.service.export.html.HtmlExportService;

public class HtmlExportDialog extends AbstractDialog {
	private static final Logger log = LoggerFactory.getLogger(HtmlExportDialog.class);
	
	@Inject
	private HtmlExportService htmlExportService;
	
	@Inject
	private ActiveSession activeSession;
	
	@Inject
	private UserProperties props;
	
	private JTextField outputDirPath;
	private JFileChooser outputDirFileChooser;
	private JFormattedTextField initImageWidth;
	
	public HtmlExportDialog() {
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]rel[]"));
		dialog.setResizable(false);
		
		createConfigPanel();
		createInitImageWidthPanel();
		dialog.add(createControlPanel("Export"), "align r, span 3");
		
		postInit();
	}
	
	private void createConfigPanel() {
		outputDirPath = new JTextField();
		outputDirFileChooser = new JFileChooser();
		outputDirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton setPathButton = new JButton("set path");
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
	
	private void createInitImageWidthPanel() {
		initImageWidth = new JFormattedTextField();
		NumberFormat longFormat = NumberFormat.getIntegerInstance();
		longFormat.setGroupingUsed(false);
		
		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setAllowsInvalid(false);
		numberFormatter.setMinimum(0);
		numberFormatter.setMaximum(9999);
		
		initImageWidth = new JFormattedTextField(numberFormatter);
		dialog.add(new JLabel("Image initial width [px]"));
		dialog.add(initImageWidth, "wrap");
	}
	
	@Override
	public void okAction() {
		try {
			showWaitingCursor();
			Integer initImageWidthValue = Integer.valueOf(initImageWidth.getText());
			htmlExportService.export(activeSession.getSession(), outputDirPath.getText(),
					initImageWidthValue);
			
			JOptionPane.showMessageDialog(dialog, InfoMsgs.HTML_EXPORT_SUCCESS);
			close();
			saveLastPath();
		} catch (HtmlExportException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} catch (IOException e) {
			log.error("", e);
			JOptionPane.showMessageDialog(dialog, ErrorMsgs.HTML_EXPORT_IO_ERROR);
		} catch (Exception e) {
			log.error("", e);
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} finally {
			hideWaitingCursor();
		}
	}
	
	public void open() {
		loadModel();
		
		center();
		dialog.setVisible(true);
	}
	
	private void saveLastPath() {
		props.setHtmlExportLastPath(outputDirPath.getText());
		props.save();
	}
	
	private void loadModel() {
		loadLastPath();
		loadInitImageWidth();
	}
	
	private void loadInitImageWidth() {
		initImageWidth.setText(String.valueOf(props.getHtmlExportImageWidth()));
	}
	
	private void loadLastPath() {
		String lastPath = props.getHtmlExportLastPath();
		if (lastPath != null) {
			outputDirPath.setText(lastPath);
			outputDirFileChooser.setSelectedFile(new File(lastPath));
		}
	}
}
