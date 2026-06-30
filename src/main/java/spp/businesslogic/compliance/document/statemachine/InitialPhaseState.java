package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.DocumentationPhase;

public class InitialPhaseState implements DocumentationState {

    DocumentationPhase currentPhase = DocumentationPhase.INITIAL;

    @Override
    public boolean canUpload(DocumentType type) {
        return type == DocumentType.CLASS_SCHEDULE;
    }

    @Override
    public DocumentationState nextPhase() {
        return new PracticePhaseState();
    }

    @Override
    public DocumentationPhase getDocumentationPhase() {
        return currentPhase;
    }


}