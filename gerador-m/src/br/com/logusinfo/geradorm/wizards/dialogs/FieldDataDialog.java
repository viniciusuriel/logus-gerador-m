package br.com.logusinfo.geradorm.wizards.dialogs;


import java.util.logging.Logger;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import br.com.logusinfo.geradorm.wizards.utils.Util;

public class FieldDataDialog extends Dialog {

	private Logger logger = Logger.getLogger(getClass().getName());

	private Text tTypeName;
	private Text tName;
	private Text tDesc;
	private Text tCol;
	private IWizardContainer wc;
	private FieldData state;

	protected Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Label typeName = new Label(container, SWT.NULL);
		typeName.setText("&Type:");

		tTypeName = new Text(container, SWT.BORDER | SWT.SINGLE);
		tTypeName.setEditable(false);
		GridData gdType = new GridData(GridData.FILL_HORIZONTAL);
		tTypeName.setLayoutData(gdType);

		Button button = new Button(container, SWT.PUSH);
		button.setText("Search..");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectJavaType();
			}
		});

		Label fieldName = new Label(container, SWT.NULL);
		fieldName.setText("&Name:");

		tName = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gdName = new GridData(GridData.FILL_HORIZONTAL);
		gdName.horizontalSpan = 2;
		tName.setLayoutData(gdName);		

		Label description = new Label(container, SWT.NULL);
		description.setText("&Description:");

		tDesc = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gdDesc = new GridData(GridData.FILL_HORIZONTAL);
		gdDesc.horizontalSpan = 2;
		tDesc.setLayoutData(gdDesc);

		Label columnName = new Label(container, SWT.NULL);
		columnName.setText("&Column name:");

		tCol = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gdCol = new GridData(GridData.FILL_HORIZONTAL);
		gdCol.horizontalSpan = 2;
		tCol.setLayoutData(gdCol);
		
		if (state.colName != null){
			this.tCol.setText(state.colName);
		}
		if (state.description != null){
			this.tDesc.setText(state.description);
		}
		if (state.name != null){
			this.tName.setText(state.name);
		}
		if (state.type != null){
			this.tTypeName.setText(state.type.getFullyQualifiedName());			
		}	

		return container;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Field Definition");
		shell.setSize(300, 200);
		Rectangle monitorArea = shell.getDisplay().getPrimaryMonitor()
				.getBounds();
		Rectangle shellArea = shell.getBounds();
		int x = monitorArea.x + (monitorArea.width - shellArea.width) / 2;
		int y = monitorArea.y + (monitorArea.height - shellArea.height) / 2;
		shell.setLocation(x, y);
	}

	public FieldDataDialog(Shell parent, IWizardContainer wc) {
		super(parent);
		this.wc = wc;
	}

	private void selectJavaType() {
		try {
			SelectionDialog dialog = JavaUI
					.createTypeDialog(
							getShell(),
							wc,
							Util.getSelectedProject(),
							IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES,
							false);
			dialog.open();
			if (dialog.getResult() != null){
				state.type = (IType) (dialog.getResult()[0]);
				if (state.type != null){
					this.tTypeName.setText(state.type.getFullyQualifiedName());
				}
			}			
		} catch (JavaModelException e) {
			logger.severe(e.getMessage());
		}
	}
	
	public void setState(FieldData state) {
		this.state = state;		
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == Dialog.OK){
			this.state.colName = tCol.getText();
			this.state.description = tDesc.getText();
			this.state.name = tName.getText();
		}
		super.buttonPressed(buttonId);
	}

}
