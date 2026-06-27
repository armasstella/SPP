package spp.utils.term;


import java.time.LocalDate;


public class TermCalculator {

    private TermCalculator() {
    }

    public static String getCurrentPeriod() {
        String generatedPeriod = "";

        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        int currentShortYear = currentYear % 100;

        if (month >= 2 && month <= 7) {
            generatedPeriod = "FEBRERO - JULIO " + currentShortYear;
        } else if (month == 1) {
            int previousYear = currentYear - 1;
            int previousShortYear = previousYear % 100;
            generatedPeriod = "AGOSTO " + previousShortYear + " - ENERO " + currentShortYear;
        } else {
            int nextYear = currentYear + 1;
            int nextShortYear = nextYear % 100;
            generatedPeriod = "AGOSTO " + currentShortYear + " - ENERO " + nextShortYear;
        }

        return generatedPeriod;
    }
}