package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.DocumentationPhase;

public class ClosurePhaseState implements DocumentationState {

    private final DocumentationPhase currentPhase = DocumentationPhase.CLOSURE;

    @Override
    public boolean canUpload(DocumentType type) {
        return type == DocumentType.FINAL_REPORT ||
                type == DocumentType.SELF_EVALUATION ||
                type == DocumentType.EVALUATION_LINKED_ORGANIZATION ||
                type == DocumentType.PSP ||
                type == DocumentType.RELEASE_LETTER;
    }

    @Override
    public DocumentationState nextPhase() {
        return this;
    }

    @Override
    public DocumentationPhase getDocumentationPhase() {
        return currentPhase;
    }


}