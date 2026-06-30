package spp.businesslogic.compliance.document;

import spp.businesslogic.compliance.document.statemachine.*;
import spp.businesslogic.enums.DocumentationPhase;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

public class DocumentationWorkflowManager {
    private DocumentationState currentState;

    public DocumentationWorkflowManager(int internId) {
        DocumentationPhase phase = null;
        try {
            InternDAO internDAO = new InternDAO();
            phase = internDAO.findCurrentDocumentationPhaseById(internId);
        } catch (DAOException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
        }
        this.currentState = initializeStateFromPhase(phase);
    }

    private DocumentationState initializeStateFromPhase(DocumentationPhase phase) {
        DocumentationState stateToReturn = new ClosurePhaseState();
        if (phase == DocumentationPhase.INITIAL) {
            stateToReturn = new InitialPhaseState();
        }

        else if (phase == DocumentationPhase.PRACTICE) {
            stateToReturn = new PracticePhaseState();
        }

        return stateToReturn;
    }

    public boolean isUploadAllowed(DocumentType type) {
        return currentState.canUpload(type);
    }

    public DocumentationPhase getCurrentPhase() {
        return currentState.getDocumentationPhase();
    }

}