package Backend;

/**
 * A modified version of TupleResult meant for holding various data about entertainment titles.
 * Author: Tyree Mitchell
 */
public class ProductionResult extends TupleResult {

    private String originalTitle;
    private boolean isAdult;
    private int startYear;
    private int endYear;
    private int runTime;

    ProductionResult(String id, String type, String primaryTitle, String originalTitle, int isAdult, int startYear,
                     int endYear, int runTime) {
        super(primaryTitle, type, id);
        this.originalTitle = originalTitle;
        this.isAdult = (isAdult != 0);
        this.startYear = startYear;
        this.endYear = endYear;
        this.runTime = runTime;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getRunTime() {
        return runTime;
    }
}
