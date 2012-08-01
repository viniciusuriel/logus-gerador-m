package br.com.logusinfo.geradorm.wizards;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.refactoring.nls.SourceContainerDialog;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

import br.com.logusinfo.geradorm.wizards.dialogs.FieldData;
import br.com.logusinfo.geradorm.wizards.dialogs.FieldDataDialog;
import br.com.logusinfo.geradorm.wizards.utils.Util;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

@SuppressWarnings("restriction")
public class PersistenceWizardPage extends WizardPage {

	private Logger logger = Logger.getLogger(getClass().getName());
	private IPackageFragmentRoot sourceFolder;
	private IPackageFragment voPackage; 
	private Text voPackageName;
	
	private IPackageFragment daoIFPackage; 
	private Text daoIFPackageName;
	
	
	private IPackageFragment daoPackage; 
	private Text daoPackageName;
	
	private Text sourceFolderText;
	private Text entityName;
	private Text description;
	private Text tableName;
	private ISelection selection;	
	private Table table; 
		

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public PersistenceWizardPage(ISelection selection) {
		super("wizardPage");		
		setTitle("Logus VO/DAO Wizard");
		setDescription("This wizard creates files related with persitence modules.");
		this.selection = selection;
	}
	
    

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.NULL);
		FillLayout layoutPanel = new FillLayout();
		layoutPanel.type = SWT.VERTICAL;		
		panel.setLayout(layoutPanel);
				
		
		Composite container = new Composite(panel , SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Source Folder:");

		sourceFolderText = new Text(container, SWT.BORDER | SWT.SINGLE);		
		sourceFolderText.setEditable(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		sourceFolderText.setLayoutData(gd);
		sourceFolderText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectSourceFolder();
			}
		});
		
		
		Label packageNameLabel = new Label(container, SWT.NULL);
		packageNameLabel.setText("&VO Package:");

		voPackageName = new Text(container, SWT.BORDER | SWT.SINGLE);				
		GridData packageNameButtonGd = new GridData(GridData.FILL_HORIZONTAL);
		voPackageName.setLayoutData(packageNameButtonGd);
		voPackageName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button packageNameButton = new Button(container, SWT.PUSH);
		packageNameButton.setText("Browse...");
		packageNameButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectVoPackage();
			}
		});
		
		Label packageNameLabelDao = new Label(container, SWT.NULL);
		packageNameLabelDao.setText("&DAO Package:");

		daoPackageName = new Text(container, SWT.BORDER | SWT.SINGLE);				
		GridData packageNameDaoButtonGd = new GridData(GridData.FILL_HORIZONTAL);
		daoPackageName.setLayoutData(packageNameDaoButtonGd);
		daoPackageName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button packageNameDaoButton = new Button(container, SWT.PUSH);
		packageNameDaoButton.setText("Browse...");
		packageNameDaoButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectDaoPackage();
			}
		});
		
		Label packageNameLabelDaoIF = new Label(container, SWT.NULL);
		packageNameLabelDaoIF.setText("&DAO Interface Package:");

		daoIFPackageName = new Text(container, SWT.BORDER | SWT.SINGLE);				
		GridData packageNameDaoIFButtonGd = new GridData(GridData.FILL_HORIZONTAL);
		daoIFPackageName.setLayoutData(packageNameDaoIFButtonGd);
		daoIFPackageName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button packageNameDaoIFButton = new Button(container, SWT.PUSH);
		packageNameDaoIFButton.setText("Browse...");
		packageNameDaoIFButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectDaoIFPackage();
			}
		});
		
		
		
		Label labelEntityName = new Label(container, SWT.NULL);
		labelEntityName.setText("&Entity Name:");

		entityName = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gdEntityName = new GridData(GridData.FILL_HORIZONTAL);
		gdEntityName.horizontalSpan=2;
		entityName.setLayoutData(gdEntityName);
		entityName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label labelDescription = new Label(container, SWT.NULL);
		labelDescription.setText("&Entity description:");

		description = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gdDescription = new GridData(GridData.FILL_HORIZONTAL);
		gdDescription.horizontalSpan=2;
		description.setLayoutData(gdDescription);
		description.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		
		Label tableNameLabel = new Label(container, SWT.NULL);
		tableNameLabel.setText("&Table Name:");

		tableName = new Text(container,SWT.BORDER | SWT.SINGLE);
		GridData tableNameGd = new GridData(GridData.FILL_HORIZONTAL);
		tableNameGd.horizontalSpan=2;
		tableName.setLayoutData(tableNameGd);
		tableName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		
		Composite colComposite = new Composite(container, SWT.NULL);
		GridData gdColComposite = new GridData(GridData.FILL_BOTH);
		gdColComposite.horizontalSpan = 3;
		colComposite.setLayoutData(gdColComposite);
		GridLayout colCompositelayout = new GridLayout();
		colComposite.setLayout(colCompositelayout);
		colCompositelayout.numColumns = 2;
		colCompositelayout.verticalSpacing = 9;
		
		
		
		table = new Table (colComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);		
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 100;
		table.setLayoutData(data);
		String[] titles = {"Type", "Field Name", "Field Description", "Column Name"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	
				
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	
		
		
		Composite addColumn = new Composite(colComposite, SWT.NULL);
		addColumn.setLayout(new GridLayout(1, true));
		Button bAdd = new Button(addColumn, SWT.PUSH);
		bAdd.setText("Add...");
		bAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FieldDataDialog dialog = new FieldDataDialog(getShell(), getWizard().getContainer());
				FieldData data = new FieldData();
				dialog.setState(data);
				dialog.open();
				if (dialog.getReturnCode() == Dialog.OK){
					toTableItem(data);					
				}
			}
		});
		
		Button bEdit = new Button(addColumn, SWT.PUSH);
		bEdit.setText("Edit...");
		bEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection() != null	&& table.getSelection().length > 0) {
					TableItem oldItem = table.getSelection()[0];
					if (oldItem != null) {
						FieldDataDialog dialog = new FieldDataDialog(getShell(), getWizard().getContainer());
						FieldData data = toFieldData(oldItem);
						dialog.setState(data);
						dialog.open();
						if (dialog.getReturnCode() == Dialog.OK) {
							toTableItem(data, oldItem);
						}
					}
				}
			}
		});

		Button bRemove = new Button(addColumn, SWT.PUSH);
		bRemove.setText("Remove...");
		bRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		
				if (table.getSelectionIndex() >= 0){
					table.remove(table.getSelectionIndex());
				}
			}
		});
		
		initialize();
		dialogChanged();	
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
		}		
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */	
	private void selectSourceFolder() {
		sourceFolder = SourceContainerDialog.getSourceContainer(getShell(), ResourcesPlugin.getWorkspace().getRoot(), Util.getSelectedJavaProject());
		if (sourceFolder != null){
			sourceFolderText.setText(sourceFolder.getPath().toPortableString());
		}
	}
	
	private void selectVoPackage(){
		SelectionDialog dialog;
		try {
			dialog = JavaUI.createPackageDialog(getShell(), Util.getSelectedJavaProject(), 0);
			dialog.open();			
			if(dialog.getResult() != null){
				voPackage = (IPackageFragment) dialog.getResult()[0];
				voPackageName.setText(voPackage.getElementName());
			}
		} catch (JavaModelException e) {
			logger.severe(e.getMessage());
		}			
	}
	
	private void selectDaoPackage(){
		SelectionDialog dialog;
		try {
			dialog = JavaUI.createPackageDialog(getShell(), Util.getSelectedJavaProject(), 0);
			dialog.open();			
			if(dialog.getResult() != null){
				daoPackage = (IPackageFragment) dialog.getResult()[0];
				daoPackageName.setText(daoPackage.getElementName());
			}
		} catch (JavaModelException e) {
			logger.severe(e.getMessage());
		}			
	}
	
	private void selectDaoIFPackage(){
		SelectionDialog dialog;
		try {
			dialog = JavaUI.createPackageDialog(getShell(), Util.getSelectedJavaProject(), 0);
			dialog.open();			
			if(dialog.getResult() != null){
				daoIFPackage = (IPackageFragment) dialog.getResult()[0];
				daoIFPackageName.setText(daoIFPackage.getElementName());
			}
		} catch (JavaModelException e) {
			logger.severe(e.getMessage());
		}			
	}
	
	
	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getSourceFolderText()));
		String fileName = getEntityName();
		String tableName = getTableName();

		if (getSourceFolderText().length() == 0) {
			updateStatus("Source folder must be specified");
			return;
		}		
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("Entity name must be specified");
			return;
		}
		if (getVoPackageName().length() == 0) {
			updateStatus("VO package must be specified");
			return;
		}
		if (getDaoIFPackageName().length() == 0) {
			updateStatus("DAo Interface package must be specified");
			return;
		}
		if (tableName.length() == 0) {
			updateStatus("Table name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("Entity name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("java") == false) {
				updateStatus("File extension must be \"java\"");
				return;
			}
		}
		updateStatus(null);
	}
	
	private TableItem toTableItem(FieldData data){
		TableItem item = new TableItem (table, SWT.NONE);
		toTableItem(data, item);
		return item;
	}
	
	private void toTableItem(FieldData data, TableItem item){		
		if (data.type != null){
			item.setText (0, data.type.getFullyQualifiedName());
			item.setData("value", data.type);
		}
		item.setText (1, data.name);
		item.setText (2, data.description);	
		item.setText (3, data.colName);		
	}
	
	private FieldData toFieldData(TableItem item){
		return new FieldData((IType) item.getData("value"), item.getText(1), item.getText(2),	item.getText(3));
		
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public IPackageFragmentRoot getSourceFolder() {
		return sourceFolder;
	}

	public String getEntityName() {
		return entityName.getText();
	}
	
	public String getTableName(){
		return tableName.getText();
	}

	public IPackageFragment getVoPackage() {
		return voPackage;
	}

	public String getSourceFolderText() {
		return sourceFolderText.getText();
	}

	public String getVoPackageName() {
		return voPackageName.getText();
	}
	
	
	public List<FieldData> getFieldData(){
		List<FieldData> list = new ArrayList<FieldData>();
		for(TableItem ti: table.getItems()){
			list.add(toFieldData(ti));
		}
		return list;
	}

	public IPackageFragment getDaoIFPackage() {
		return daoIFPackage;
	}

	public String getDaoIFPackageName() {
		return daoIFPackageName.getText();
	}

	public IPackageFragment getDaoPackage() {
		return daoPackage;
	}

	public String getDaoPackageName() {
		return daoPackageName.getText();
	}
	
	
	public String getDescription() {
		return description.getText();
	}
				
}