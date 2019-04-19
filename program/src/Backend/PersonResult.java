package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonResult extends TupleResult{
    private SQLServer server;
    private String jobTitle;
    private String characterPlayed;
    private String jobID;
    private int birthYear;
    private int deathYear;
    private ArrayList<ProductionResult> knownProductions;

    public PersonResult(String id, String type, String name, String jobTitle
                        , String characterPlayed, String jobID, int birthYear, int deathYear, SQLServer server) {
        super(name, type, id);
        this.server = server;
        this.jobTitle = jobTitle;
        this.characterPlayed = characterPlayed;
        this.jobID = jobID;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    /**
     * Method for devleloping the list of productions this person is involved in.
     */
    public void getKnownProductions() {
        ResultSet result = null;
        ResultSet prodSet = null;
        try {
            knownProductions = new ArrayList<>();
            result = server.executeStatement("SELECT * FROM person INNER JOIN castandcrew " +
                    "ON person.personID = castandcrew.personID WHERE person.personID = " + "'" + getId() + "'" + ";");
            ArrayList<String> productionIDS = new ArrayList<>();
            while(result.next()) {
                productionIDS.add(result.getString("prodID"));
            }


            for(String prodID : productionIDS) {
                prodSet = server.executeStatement("SELECT * FROM production WHERE prodID = "
                        + "'" + prodID + "';");
                while(prodSet.next()) {
                    String type = "Movie";
                    String ID = prodSet.getString( "prodID" );
                    String description = prodSet.getString( "primaryTitle");
                    String originalTitle = prodSet.getString("originalTitle");
                    int isAdult = prodSet.getInt("isAdult");
                    int startYear = prodSet.getInt("startYear");
                    int endYear = prodSet.getInt("endYear");
                    int runTime = prodSet.getInt("runTime");
                    ProductionResult pResult = new ProductionResult(ID, type, description, originalTitle,
                            isAdult, startYear, endYear, runTime, server);
                    knownProductions.add(pResult);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the list of productions for which this person is involved.
     * @return
     */
    public ArrayList<ProductionResult> getProductions() {
        return knownProductions;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCharacterPlayed() {
        return characterPlayed;
    }

    public String getJobID() {
        return jobID;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    @Override
    public String toString(){
        String returnString = getName() + ": " + jobID;
        if (jobTitle != null){
            return returnString + " " + jobTitle;
        }
        if(characterPlayed != null) {
            return returnString + " " + characterPlayed;
        }

        return returnString;
    }
}
