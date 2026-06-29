package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;

public class InitialPhaseState implements DocumentationState {

    @Override
    public boolean canUpload(DocumentType type) {
        return type == DocumentType.CLASS_SCHEDULE;
    }

    @Override
    public DocumentationState nextPhase() {
        return new PracticePhaseState();
    }
}