package br.com.logusinfo.geradorm.wizards;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import br.com.logusinfo.geradorm.StringGenerator;
import br.com.logusinfo.geradorm.model.DAOIfModel;
import br.com.logusinfo.geradorm.model.DAOModel;
import br.com.logusinfo.geradorm.model.FieldModel;
import br.com.logusinfo.geradorm.model.VOModel;
import br.com.logusinfo.geradorm.wizards.dialogs.FieldData;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "java". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */
public class PersistenceWizard extends Wizard implements INewWizard {
	private PersistenceWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for PersistenceWizard.
	 */
	public PersistenceWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new PersistenceWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {		
		final String entityName = page.getEntityName();
		final String description = page.getDescription();
		final String tableName = page.getTableName();
		final IPackageFragmentRoot sourceFolder = page.getSourceFolder();
		final IPackageFragment voPackage = page.getVoPackage();
		final String  voPackageName = page.getVoPackageName();
		final IPackageFragment daoPackage = page.getDaoPackage();
		final String  daoPackageName = page.getDaoPackageName();
		final IPackageFragment daoIFPackage = page.getDaoIFPackage();
		final String  daoIFPackageName = page.getDaoIFPackageName();
		final List<FieldData> fields = page.getFieldData();
		
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(entityName, description, tableName, sourceFolder, voPackage,
							voPackageName, daoPackage, daoPackageName,
							daoIFPackage, daoIFPackageName, fields, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(String entityName,
			String description,
			String tableName,
			IPackageFragmentRoot sourceFolder,
			IPackageFragment voPackage,
			String  voPackageName,
			IPackageFragment daoPackage,
			String  daoPackageName,
			IPackageFragment daoIFPackage,
			String  daoIFPackageName,
			List<FieldData> fields, IProgressMonitor monitor)
	
		throws CoreException {
		
		monitor.beginTask("Creating VO", 4);
		
		if (!sourceFolder.exists()) {
			throwCoreException("Container \"" + sourceFolder + "\" does not exist.");
		}
		
		if (voPackage == null && voPackageName != null && !voPackageName.isEmpty()){
			voPackage = sourceFolder.createPackageFragment(voPackageName, true, monitor);	
		}
	
		VOModel model = new VOModel();
		model.setEntityName(entityName);
		model.setDescription(description);
		model.setPackageName(voPackage.getElementName());				
		for(FieldData fd : fields){
			model.getFields().add(new FieldModel(fd.name, fd.description, fd.type.getElementName(), fd.colName, fd.type.getFullyQualifiedName()));
			if (!fd.type.getFullyQualifiedName().startsWith("java.lang")){			
				model.getImports().add(fd.type.getFullyQualifiedName());
			}
		}
				
		String vo = new StringGenerator(model).generate();
		final ICompilationUnit cu = voPackage.createCompilationUnit(model.getEntityName()+"VO.java", vo, false, monitor);		
		
		monitor.worked(1);
		
		monitor.setTaskName("Creating DAO Interface ");
		
		if (daoIFPackage == null && daoIFPackageName != null && !daoIFPackageName.isEmpty()){
			daoIFPackage = sourceFolder.createPackageFragment(daoIFPackageName, true, monitor);	
		}
	
		
		DAOIfModel daoifmodel = new DAOIfModel();
		daoifmodel.setTableName(tableName);		
		daoifmodel.setEntityName(entityName);
		daoifmodel.setDescription(description);
		daoifmodel.setPackageName(daoIFPackage.getElementName());
		daoifmodel.setVoPackageName(voPackage.getElementName());	
		for(FieldData fd : fields){
			daoifmodel.getFields().add(new FieldModel(fd.name, fd.description, fd.type.getElementName(), fd.colName, fd.type.getFullyQualifiedName()));
			if (!fd.type.getFullyQualifiedName().startsWith("java.lang")){
				daoifmodel.getImports().add(fd.type.getFullyQualifiedName());
			}
		}
		
		String daoIF = new StringGenerator(daoifmodel).generate();
		final ICompilationUnit cuIF = daoIFPackage.createCompilationUnit(daoifmodel.getEntityName()+"DAOInterface.java", daoIF, false, monitor);		
		
		
		monitor.worked(1);
		
		monitor.setTaskName("Creating DAO ");
		
		if (daoPackage == null && daoPackageName != null && !daoPackageName.isEmpty()){
			daoPackage = sourceFolder.createPackageFragment(daoPackageName, true, monitor);	
		}
		
		DAOModel daomodel = new DAOModel();					
		daomodel.setEntityName(entityName);
		daomodel.setDescription(description);
		daomodel.setPackageName(daoPackage.getElementName());
		daomodel.setVoPackageName(voPackage.getElementName());
		daomodel.setDaoInterfacePackageName(daoIFPackage.getElementName());
		for(FieldData fd : fields){
			daomodel.getFields().add(new FieldModel(fd.name, fd.description, fd.type.getElementName(), fd.colName, fd.type.getFullyQualifiedName()));
			if (!fd.type.getFullyQualifiedName().startsWith("java.lang")){
				daomodel.getImports().add(fd.type.getFullyQualifiedName());
			}
		}
		
		String dao = new StringGenerator(daomodel).generate();
		final ICompilationUnit cuDAO = daoPackage.createCompilationUnit(daomodel.getEntityName()+"DAO.java", dao, false, monitor);		
				
		monitor.worked(1);
				
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					if (cu.exists()) {		
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(cu.getPath());
					    IDE.openEditor(page, file, true);
					}
					if (cuIF.exists()) {		
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(cuIF.getPath());
					    IDE.openEditor(page, file, true);
					}
					if (cuDAO.exists()) {		
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(cuDAO.getPath());
					    IDE.openEditor(page, file, true);
					}
				} catch (PartInitException e) {
					
				} 			}
		});
		monitor.worked(1);
	}
	

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "geradorm", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}