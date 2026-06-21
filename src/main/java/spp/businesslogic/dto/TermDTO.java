package spp.businesslogic.dto;


public class TermDTO {
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
        this.name = name;
    }

    public boolean isActiveTerm() {
        return activeTerm;
    }

    public void setActiveTerm(boolean activeTerm) {
        this.activeTerm = activeTerm;
    }
}
