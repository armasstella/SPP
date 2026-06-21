package spp.businesslogic.dto;


import spp.businesslogic.enums.GenderFilter;
import spp.businesslogic.enums.YesNoAllFilter;


public class IndicatorFilterDTO {
    private GenderFilter gender;
    private YesNoAllFilter indigenousLanguage;
    private Integer minAge;
    private Integer maxAge;
    private String period;

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public YesNoAllFilter getIndigenousLanguage() {
        return indigenousLanguage;
    }

    public void setIndigenousLanguage(YesNoAllFilter indigenousLanguage) {
        this.indigenousLanguage = indigenousLanguage;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}