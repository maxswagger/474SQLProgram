/**
 * a search result (tuple) from a query
 */
public class TupleResult {

    private String name;
    private String type;
    private String id;

    /**
     * consructor to store information
     * @param name name/title to display in list
     * @param type what table its from
     * @param id to search again for populating more data
     */
    TupleResult(String name, String type, String id){
        this.name = name;
        this.type = type;
        this.id = id;
    }

    /**
     * to string function
     * @return
     */
    public String toString(){
        return "["+type+"] "+name;
    }
}
