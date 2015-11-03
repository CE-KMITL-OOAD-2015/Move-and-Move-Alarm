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
        this.requiredFields = new ArrayList<>();
        this.requiredFields.add("name");
        this.requiredFields.add("admin_id");
        members = new ArrayList<>();
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
        if(modifiedDate == null)
            temp.put("modified_date", "'" + null + "'");
        else
            temp.put("modified_date", "'" + sdf.format(modifiedDate) + "'");
        return temp;
    }

    public HashMap<String, Object> setName(String name)
    {
        this.name = name;
        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> setStatus(String status)
    {
        this.status = status;
        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> setAdmin(User user)
    {
        if(createdDate != null)
            return createProcessStatus(false, "The group already has an admin user.");
        this.admin = user;
        addMember(user);

        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> setScore(int score)
    {
        if(score < 0)
            return createProcessStatus(false, "The score cannot be under zero.");
        this.score = score;

        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> increaseScore(int score, String description)
    {
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changedScore = Math.abs(score);

        this.score += changedScore;
        temp_scoreLog.put("group_id", id);
        temp_scoreLog.put("current_score", this.score);
        temp_scoreLog.put("modified_score", changedScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog);

        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> decreaseScore(int score, String description)
    {
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changedScore = Math.abs(score);

        if(this.score - changedScore < 0)
            return createProcessStatus(false, "The score cannot be under zero.");

        this.score -= changedScore;
        temp_scoreLog.put("group_id", id);
        temp_scoreLog.put("current_score", this.score);
        temp_scoreLog.put("modified_score", -changedScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog);

        updateModifiedDate();

        return createProcessStatus(true);
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
        for(User item : temp_addedUserList) {
            if(item.getID() == user.getID())
                return createProcessStatus(false, "This user is already added to the temporary added list.");
        }

        if(User.find(user.id) != null)
            return createProcessStatus(false, "This user is already added to the database.");

        temp_addedUserList.add(user);
        updateModifiedDate();

        return createProcessStatus(true);
    }

    public HashMap<String, Object> removeMember(User user)
    {
        if(admin.getID() == user.getID())
            return createProcessStatus(false, "Cannot remove admin user.");

        for(User item : temp_removeUserList) {
            if(item.getID() == user.getID())
                return createProcessStatus(false, "This user is already added to the temporary removed list.");
        }

        if(User.find(user.id) == null)
            return createProcessStatus(false, "This user is already removed from the database.");

        temp_removeUserList.add(user);
        updateModifiedDate();

        return createProcessStatus(true);
    }

    @Override
    public HashMap<String, Object> save()
    {
        if(admin == null)
            return createProcessStatus(false, "The group must have an admin user before saving.");

        HashMap<String, Object> requiredFields = checkRequiredFields();
        if(requiredFields != null)
            return requiredFields;

        if(createdDate == null) {
            HashMap<String, Object> temp = modelCollection.create(this);
            if(temp == null)
                return createProcessStatus(false, "Cannot save due to a database error.");
            id = Integer.parseInt("" + temp.get("id"));
            createdDate = (Date) temp.get("created_date");
            return createProcessStatus(true);
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
                    return createProcessStatus(false, "An error has occurred while adding a member.");
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
                    return createProcessStatus(false, "An error has occurred while removing a member.");
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
                return createProcessStatus(false, "An error has occurred while adding a score log.");
            }
        }

        temp_addedUserList = null;
        temp_removeUserList = null;
        temp_scoreLogList = null;

        return createProcessStatus(modelCollection.save(this));
    }

    @Override
    public HashMap<String, Object> delete()
    {
        SQLInquirer sqlInquirer = SQLInquirer.getInstance();
        try {
            sqlInquirer.delete("group_score", "group_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        temp_removeUserList = members;
        save();
        return createProcessStatus(modelCollection.delete(this));
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }
}
