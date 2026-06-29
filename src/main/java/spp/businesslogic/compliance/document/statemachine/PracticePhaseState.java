package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;

public class PracticePhaseState implements DocumentationState {
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
}