package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.DocumentationPhase;

public class PracticePhaseState implements DocumentationState {

    private final DocumentationPhase currentPhase =  DocumentationPhase.PRACTICE;

    @Override
    public boolean canUpload(DocumentType type) {
        return type == DocumentType.MONTHLY_REPORT ||
                type == DocumentType.PARTIAL_REPORT ||
                type == DocumentType.PSP ||
                type == DocumentType.ACTIVITIES_PLAN;
    }

    @Override
    public DocumentationState nextPhase() {
        return new ClosurePhaseState();
    }

    @Override
    public DocumentationPhase getDocumentationPhase() {
        return currentPhase;
    }

}