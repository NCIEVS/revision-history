package org.protege.editor.owl.client.diff.ui;

import javax.swing.JPanel;

import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.client.diff.model.LogDiffEvent;
import org.protege.editor.owl.client.diff.model.LogDiffListener;
import org.protege.editor.owl.client.diff.model.LogDiffManager;
import org.protege.editor.owl.model.OWLModelManager;

public class TreePanel extends JPanel implements Disposable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogDiffManager diffManager;
	/**
     * Constructor
     *
     * @param modelManager  OWL model manager
     * @param editorKit OWL editor kit
     */
    public TreePanel(OWLModelManager modelManager, OWLEditorKit editorKit) {
    
    	diffManager = LogDiffManager.get(modelManager, editorKit);
        diffManager.addListener(diffListener);
    }
    
    private LogDiffListener diffListener = new LogDiffListener() {
        @Override
        public void statusChanged(LogDiffEvent event) {
            if(event.equals(LogDiffEvent.COMMIT_SELECTION_CHANGED)) {
                diffManager.clearSelectedChanges();
                
            } else {
            	
            }
        }
    };
	@Override
	public void dispose() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
