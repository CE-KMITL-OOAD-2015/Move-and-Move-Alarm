package movealarm.kmitl.net;

/**
 * Created by Moobi on 16-Oct-15.
 */
public interface ModelCollection {
    public Model find(int id);
    public Model[] where(String colName, String operator, String value);
    public Model[] all();
    public boolean save(Model m);
    public boolean delete(Model m);
}
