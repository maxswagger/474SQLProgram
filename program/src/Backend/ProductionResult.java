package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    private SQLServer server;

    private String genre;

    private ArrayList<PersonResult> castCrew;

    ProductionResult(String id, String type, String primaryTitle, String originalTitle, int isAdult, int startYear,
                     int endYear, int runTime, SQLServer server) {
        super(primaryTitle, type, id);
        this.originalTitle = originalTitle;
        this.isAdult = (isAdult != 0);
        this.startYear = startYear;
        this.endYear = endYear;
        this.runTime = runTime;
        this.server = server;
        castCrew = new ArrayList<>();
    }

    public void addGenre() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT genreID FROM productiongenre WHERE prodID = " + "'" + getId() + "'" + ";");
            if(result.next())
                genre = result.getString("genreID");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add various cast and crew members/characters to this production
     */
    public void addCastCrew() {
        ResultSet result = null;
        try {
            result = server.executeStatement("SELECT * FROM castandcrew INNER JOIN Person ON castandcrew.personID = person.personID WHERE castandcrew.prodID = " + "'" + getId() + "'" + ";");
            while(result.next()) {
                String name = result.getString("primaryName");
                String id = result.getString("personID");
                String jobID = result.getString("jobID");
                String characterPlayed = result.getString("characterPlayed");
                String jobTitle = result.getString("jobTitle");
                PersonResult newResult = new PersonResult(id, "Person", name, jobTitle, characterPlayed, jobID, server);
                castCrew.add(newResult);
            }
        } catch (SQLException e) {

        }
    }

    public String getGenre() {
        if(genre == null) {
            addGenre();
        }
        return genre;
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

    public ArrayList<PersonResult> getCastCrew() {
        return castCrew;
    }
}
