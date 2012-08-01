package br.com.logusinfo.geradorm.wizards.utils;

import java.util.logging.Logger;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Util {
	
	public static IProject getSelectedProject(){
		  IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		    if (window != null)
		    {
		        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
		        Object firstElement = selection.getFirstElement();
		        if (firstElement instanceof IAdaptable)
		        {
		            IProject project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
		            return project;
		        }
		    }
		return null;
	}
	
	public static IJavaProject getSelectedJavaProject(){
		IProject project = getSelectedProject();
		if (project != null){
			try {
				return JavaCore.create(project);
			} catch (Exception e){
				Logger.getLogger(Util.class.getName()).severe(e.getMessage());
				return null;
			}
		}
		return null;
	}
			

}
