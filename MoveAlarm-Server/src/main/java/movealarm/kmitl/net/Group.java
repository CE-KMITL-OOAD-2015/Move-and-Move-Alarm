package movealarm.kmitl.net;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Moobi on 01-Nov-15.
 */
public class Group extends Model{
    private String name = null;
    private String status = null;
    private int score = 0;
    private int amountMember = 0;
    private User admin = null;
    private ArrayList<User> members = null;
    private ArrayList<User> temp_addedUserList = null;
    private ArrayList<User> temp_removeUserList = null;
    private ArrayList<HashMap<String, Object>> temp_scoreLogList = null;

    public Group()
    {
        this.tableName = "groups";
        temp_addedUserList = new ArrayList<>();
        temp_removeUserList = new ArrayList<>();
        temp_scoreLogList = new ArrayList<>();
    }

    public static Group find(int id) //find is a static method that will provide object group by enter a group id.
    {
        HashMap<String, Object> temp = modelCollection.find("groups", id); //create temp to catch data from database.
        Group model = new Group();
        //**attribute mapping process**
        model.id = (int) temp.get("id");
        model.createdDate = (Date) temp.get("created_date");
        model.name = "" + temp.get("name");
        model.status = "" + temp.get("status");
        model.score = (int) temp.get("score");
        model.admin = User.find((int) temp.get("admin_id"));
        model.amountMember = (int) temp.get("amount_member");
        model.modifiedDate = (Date) temp.get("modified_date");

        //**object mapping process**, one group can has many members.
        model.members = new ArrayList<>();
        ArrayList<HashMap<String, Object>> user_temp = modelCollection.where("user", "group_id", "=", "" + model.id); //create user_temp to catch data from database.
        for(HashMap<String, Object> item : user_temp) {
            User user = User.find((int) item.get("id"));
            model.members.add(user);
        }
        return model;
    }

    public static Group[] where(String colName, String operator, String value)
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("groups", colName, operator, value);
        ArrayList<Group> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            Group model = new Group();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("created_date");
            model.name = "" + item.get("name");
            model.status = "" + item.get("status");
            model.score = (int) item.get("score");
            model.admin = User.find((int) item.get("admin_id"));
            model.amountMember = (int) item.get("amount_member");
            model.modifiedDate = (Date) item.get("modified_date");

            model.members = new ArrayList<>();
            ArrayList<HashMap<String, Object>> user_temp = modelCollection.where("user", "group_id", "=", "" + model.id); //create user_temp to catch data from database.
            for(HashMap<String, Object> data : user_temp) {
                User user = User.find((int) data.get("id"));
                model.members.add(user);
            }

            collection.add(model);
        }

        return  collection.toArray(new Group[collection.size()]);
    }

    public static Group[] where(String colName, String operator, String value, String extraCondition)
    {
        return where(colName, operator, value + " " + extraCondition);
    }

    public static Group[] all()
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.all("groups");
        ArrayList<Group> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            Group model = new Group();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("created_date");
            model.name = "" + item.get("name");
            model.status = "" + item.get("status");
            model.score = (int) item.get("score");
            model.admin = User.find((int) item.get("admin_id"));
            model.amountMember = (int) item.get("amount_member");
            model.modifiedDate = (Date) item.get("modified_date");

            model.members = new ArrayList<>();
            ArrayList<HashMap<String, Object>> user_temp = modelCollection.where("user", "group_id", "=", "" + model.id); //create user_temp to catch data from database.
            for(HashMap<String, Object> data : user_temp) {
                User user = User.find((int) data.get("id"));
                model.members.add(user);
            }

            collection.add(model);
        }

        return  collection.toArray(new Group[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", "'" + name + "'");
        temp.put("status", "'" + status + "'");
        temp.put("score", "'" + score + "'");
        temp.put("amount_member", "'" + amountMember + "'");
        temp.put("admin_id", "'" + admin.getID() + "'");
        temp.put("modified_date", "'" + sdf.format(modifiedDate) + "'");
        return temp;
    }

    public void setName(String name)
    {
        this.name = name;
        updateModifiedDate();
    }

    public void setStatus(String status)
    {
        this.status = status;
        updateModifiedDate();
    }

    public HashMap<String, Object> setAdmin(User user)
    {
        HashMap<String, Object> status = new HashMap<>();
        if(admin != null) {
            status.put("status", false);
            status.put("description", "The group already has an admin user.");
            return status;
        }
        this.admin = user;
        addMember(user);
        updateModifiedDate();

        status.put("status", true);
        return status;
    }

    public HashMap<String, Object> setScore(int score)
    {
        HashMap<String, Object> status = new HashMap<>();

        if(score < 0) {
            status.put("status", false);
            status.put("description", "The score cannot be under zero.");
            return status;
        }
        this.score = score;

        updateModifiedDate();
        status.put("status", true);
        return status;
    }

    public HashMap<String, Object> increaseScore(int score, String description)
    {
        HashMap<String, Object> status = new HashMap<>();
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changedScore = Math.abs(score);

        this.score += changedScore;
        temp_scoreLog.put("group_id", id);
        temp_scoreLog.put("current_score", this.score);
        temp_scoreLog.put("modified_score", changedScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog);

        updateModifiedDate();
        status.put("status", true);
        return status;
    }

    public HashMap<String, Object> decreaseScore(int score, String description)
    {
        HashMap<String, Object> status = new HashMap<>();
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changedScore = Math.abs(score);

        if(this.score - changedScore < 0) {
            status.put("status", false);
            status.put("description", "The score cannot be under zero.");
            return status;
        }
        this.score -= changedScore;
        temp_scoreLog.put("group_id", id);
        temp_scoreLog.put("current_score", this.score);
        temp_scoreLog.put("modified_score", -changedScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog);

        updateModifiedDate();
        status.put("status", false);

        return status;
    }

    public String getName()
    {
        return name;
    }

    public String getStatus()
    {
        return status;
    }

    public int getAmountMember()
    {
        return amountMember;
    }

    public User getAdmin()
    {
        return admin;
    }

    public HashMap<String, Object> addMember(User user)
    {
        HashMap<String, Object> status = new HashMap<>();
        for(User item : temp_addedUserList) {
            if(item.getID() == user.getID()) {
                status.put("status", false);
                status.put("descriptipon", "This user is already added to the temporary added list.");
                return status;
            }
        }

        if(User.find(user.id) != null) {
            status.put("status", false);
            status.put("descriptipon", "This user is already added to the database.");
            return status;
        }

        temp_addedUserList.add(user);
        updateModifiedDate();
        status.put("status", true);

        return status;
    }

    public HashMap<String, Object> removeMember(User user)
    {
        HashMap<String, Object> status = new HashMap<>();

        if(admin.getID() == user.getID()) {
            status.put("status", false);
            status.put("description", "Cannot remove admin user.");
            return status;
        }

        for(User item : temp_removeUserList) {
            if(item.getID() == user.getID()) {
                status.put("status", false);
                status.put("descriptipon", "This user is already added to the temporary removed list.");
                return status;
            }
        }

        if(User.find(user.id) == null) {
            status.put("status", false);
            status.put("descriptipon", "This user is already removed from the database.");
            return status;
        }

        temp_removeUserList.add(user);
        updateModifiedDate();
        status.put("status", true);

        return status;
    }

    @Override
    public boolean save()
    {
        if(createdDate == null) {
            HashMap<String, Object> temp = modelCollection.create(this);
            if(temp == null)
                return false;
            id = Integer.parseInt("" + temp.get("id"));
            createdDate = (Date) temp.get("created_date");
            return true;
        }

        if(temp_addedUserList.size() != 0) {
            SQLInquirer sqlInquirer = SQLInquirer.getInstance();
            for(User user : temp_addedUserList) {
                try {
                    sqlInquirer.update("user", "group_id='" + id + "'", "id", "=", "" + user.getID());
                    amountMember++;
                } catch (SQLException e) {
                    System.out.println("An error has occurred while adding a member.");
                    e.printStackTrace();
                }
            }
        }

        if(temp_removeUserList.size() != 0) {
            SQLInquirer sqlInquirer = SQLInquirer.getInstance();
            for(User user : temp_removeUserList) {
                try {
                    sqlInquirer.update("user", "group_id=NULL", "id", "=", "" + user.getID());
                    amountMember--;
                } catch (SQLException e) {
                    System.out.println("An error has occurred while removing a member.");
                    e.printStackTrace();
                }
            }
        }

        if(temp_scoreLogList.size() != 0) {
            SQLInquirer sqlInquirer = SQLInquirer.getInstance();
            String[] valuesSet = new String[temp_scoreLogList.size()];

            for(int i = 0; i < temp_scoreLogList.size(); i++) {
                HashMap<String, Object> item = temp_scoreLogList.get(i);
                valuesSet[i] = "" + item.get("group_id") + ", " + item.get("current_score") + ", " + item.get("modified_score") + ", '" + item.get("description") + "'";
            }

            String colNameSet = "group_id, current_score, modified_score, description";

            try {
                sqlInquirer.insertMultiple("group_score", colNameSet, valuesSet);
            } catch (SQLException e) {
                System.out.println("An error has occurred while adding a score log.");
                e.printStackTrace();
            }
        }

        temp_addedUserList = null;
        temp_removeUserList = null;
        temp_scoreLogList = null;

        return modelCollection.save(this);
    }

    @Override
    public HashMap<String, Object>

    @Override
    public void delete()
    {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        try {
            sqlInquirer.delete("group_score", "group_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        temp_removeUserList = members;
        save();
        modelCollection.delete(this);
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }
}
