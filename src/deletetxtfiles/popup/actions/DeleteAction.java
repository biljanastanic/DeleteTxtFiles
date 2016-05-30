package deletetxtfiles.popup.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DeleteAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public DeleteAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		ISelectionService selectionService = window.getSelectionService();

		ISelection selection = selectionService.getSelection();

		IProject project = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection)
					.getFirstElement();
			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
				IPath path = project.getLocation();
				List<File> files = filesList(path.toPortableString());

				for (int i = 0; i < files.size(); i++) {
					if (files.get(i).isFile()) {
						if (files.get(i).toString().endsWith(".txt")) {
							files.get(i).delete();
						}
					}
				}

			}
		}
		
		MessageDialog.openInformation(
			shell,
			"DeleteTxtFiles",
			"Delete files was executed.");
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	public static List<File> filesList(String folderName) {
		File folder = new File(folderName);

		List<File> listFiles = new ArrayList<File>();
		File[] files = folder.listFiles();
		listFiles.addAll(Arrays.asList(files));
		for (File file : files) {
				if (file.isDirectory()) {
				listFiles.addAll(filesList(file.getAbsolutePath()));
			}
		}
		return listFiles;
	}

}
