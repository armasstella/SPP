package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;

public class ClosurePhaseState implements DocumentationState {
    @Override
    public boolean canUpload(DocumentType type) {
        return type == DocumentType.FINAL_REPORT ||
                type == DocumentType.SELF_EVALUATION ||
                type == DocumentType.EVALUATION_LINKED_ORGANIZATION ||
                type == DocumentType.PSP;
    }

    @Override
    public DocumentationState nextPhase() {
        return this;
    }
}