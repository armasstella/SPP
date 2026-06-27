package spp.businesslogic.dto;


import spp.utils.validation.PatternDomainValidator;

public class TermDTO extends BaseDTO {
    private int termId;
    private String name;
    private boolean activeTerm;

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        PatternDomainValidator validator = new PatternDomainValidator();

        if (validator.validateTerm(name)) {
            this.name = name;
        } else {
            addErrors(validator.getPatternsErrors());
        }
    }

    public boolean isActiveTerm() {
        return activeTerm;
    }

    public void setActiveTerm(boolean activeTerm) {
        this.activeTerm = activeTerm;
    }
}
