package com.niklim.clicktrace.dialog;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.exception.HtmlExportException;
import com.niklim.clicktrace.service.export.html.HtmlExportService;

@Singleton
public class HtmlExportDialog extends AbstractDialog<HtmlExportView> {
	private static final Logger log = LoggerFactory.getLogger(HtmlExportDialog.class);

	@Inject
	private HtmlExportService htmlExportService;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private UserProperties props;

	public HtmlExportDialog() {
		postInit();
	}

	@Override
	public void okAction() {
		try {
			showWaitingCursor();
			Integer initImageWidthValue = props.getExportImageWidth();
			htmlExportService.export(activeSession.getSession(), view.outputDirPath.getText(), initImageWidthValue);

			JOptionPane.showMessageDialog(view.dialog, InfoMsgs.HTML_EXPORT_SUCCESS);
			close();
			saveLastPath();
		} catch (HtmlExportException e) {
			JOptionPane.showMessageDialog(view.dialog, e.getMessage());
		} catch (IOException e) {
			log.error("", e);
			JOptionPane.showMessageDialog(view.dialog, ErrorMsgs.HTML_EXPORT_IO_ERROR);
		} catch (Exception e) {
			log.error("", e);
			JOptionPane.showMessageDialog(view.dialog, e.getMessage());
		} finally {
			hideWaitingCursor();
		}
	}

	public void open() {
		initModel();

		center();
		view.dialog.setVisible(true);
	}

	private void saveLastPath() {
		props.setHtmlExportLastPath(view.outputDirPath.getText());
		props.save();
	}

	private void initModel() {
		loadLastPath();
	}

	private void loadLastPath() {
		String lastPath = props.getHtmlExportLastPath();
		if (lastPath != null) {
			view.outputDirPath.setText(lastPath);
			view.outputDirFileChooser.setSelectedFile(new File(lastPath));
		}
	}

	@Override
	protected HtmlExportView createView() {
		return new HtmlExportView();
	}
}
