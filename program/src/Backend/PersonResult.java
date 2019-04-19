package Backend;

public class PersonResult extends TupleResult{
    private SQLServer server;
    private String jobTitle;
    private String characterPlayed;
    private String jobID;

    public PersonResult(String id, String type, String name, String jobTitle
                        , String characterPlayed, String jobID, SQLServer server) {
        super(name, type, id);
        this.server = server;
        this.jobTitle = jobTitle;
        this.characterPlayed = characterPlayed;
        this.jobID = jobID;
    }

    @Override
    public String toString(){
        String returnString = getName() + ": " + jobID;
        if (jobTitle != null){
            return returnString + jobTitle;
        }
        if(characterPlayed != null) {
            return returnString + characterPlayed;
        }

        return returnString;
    }
}
