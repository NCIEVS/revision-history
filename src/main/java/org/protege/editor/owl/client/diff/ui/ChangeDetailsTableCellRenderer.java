package org.protege.editor.owl.client.diff.ui;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.client.diff.model.ChangeMode;
import org.protege.editor.owl.client.diff.model.LogDiff;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * @author Rafael Gonçalves <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class ChangeDetailsTableCellRenderer extends LogDiffCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OWLModelManager modelManager;

    /**
     * Constructor
     *
     * @param editorKit OWL editor kit
     */
    public ChangeDetailsTableCellRenderer(OWLEditorKit editorKit) {
        super(editorKit);
        this.modelManager = editorKit.getOWLModelManager();
    }
    
    private String fix(OWLAxiom ax, String s) {
    	if (ax instanceof OWLAnnotationAssertionAxiom) {
    		OWLAnnotationAssertionAxiom aax = (OWLAnnotationAssertionAxiom) ax;
    		if (aax.isAnnotated()) {
    			for (OWLAnnotation ann : aax.getAnnotations()) {
    				s += "\n\t" + ann.getProperty().getIRI().getFragment() 
    						+ "\t" + ann.getValue().asLiteral().get().getLiteral();
    			}
    		}
    		
    	}
    	return s;
    	
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        ChangeMode mode = LogDiff.getChangeMode((OWLOntologyChange)value);
        if (value instanceof OWLAxiomChange) {
            OWLAxiom ax = ((OWLAxiomChange)value).getAxiom();
            setText(fix(ax, modelManager.getRendering(ax)));
            setToolTipText(modelManager.getRendering(ax));
        }
        else if(value instanceof AnnotationChange) {
            OWLAxiom ax = getAnnotationAssertionAxiom((AnnotationChange) value);
            setText(modelManager.getRendering(ax));
            setToolTipText(modelManager.getRendering(ax));
        }
        else if(value instanceof ImportChange) {
            OWLImportsDeclaration decl = ((ImportChange)value).getImportDeclaration();
            setText(modelManager.getRendering(decl.getIRI()));
            setToolTipText(modelManager.getRendering(decl.getIRI()));
        }
        else if(value instanceof SetOntologyID) {
            IRI newIri = ((SetOntologyID)value).getNewOntologyID().getOntologyIRI().get();
            setText("New IRI: " + newIri);
            setToolTipText(modelManager.getRendering(newIri));
        }
        setBackground(table, mode, this, isSelected);
        setFont(table.getFont());
        return this;
    }

    private OWLAnnotationAssertionAxiom getAnnotationAssertionAxiom(AnnotationChange change) {
        OWLAnnotation ann = change.getAnnotation();
        OWLOntology ont = change.getOntology();
        OWLDataFactory df = ont.getOWLOntologyManager().getOWLDataFactory();
        return df.getOWLAnnotationAssertionAxiom(ont.getOntologyID().getOntologyIRI().get(), ann);
    }

    private void setBackground(JTable table, ChangeMode mode, Component c, boolean isSelected) {
        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        }
        else {
            GuiUtils.setComponentBackground(c, mode);
            c.setForeground(GuiUtils.UNSELECTED_FOREGROUND);
        }
    }
}
