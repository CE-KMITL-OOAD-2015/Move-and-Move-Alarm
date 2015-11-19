package movealarm.kmitl.net;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Group extends Model{
    private String name = null;
    private String status = null;
    private int score = 0;
    private int amountMember = 0;
    private User admin = null;
    private Converter converter = Converter.getInstance();
    private ArrayList<User> members;
    private ArrayList<User> temp_addedUserList; //create temp list to keep temporary added users until next saving model
    private ArrayList<User> temp_removeUserList; //create temp list to keep temporary removed users until next saving model
    private ArrayList<HashMap<String, Object>> temp_scoreLogList; //create temp list to keep temporary score logs until next saving model

    public Group()
    {
        this.tableName = "groups"; //table name in the database
        this.addRequiredField("name"); //this model cannot be saved until the required fields were filled
        this.addRequiredField("adminID");
        members = new ArrayList<>(); //create list of members
        temp_addedUserList = new ArrayList<>();
        temp_removeUserList = new ArrayList<>();
        temp_scoreLogList = new ArrayList<>();
    }

    public static Group find(int id) //a static method that will provide model type 'group' by enter a group id.
    {
        HashMap<String, Object> temp = modelCollection.find("groups", id); //create a temporary HashMap to keep data from the database.
        if(temp == null) //if no data
            return null;

        Group model = new Group(); //create new model
        //**process of attribute mapping**
        model.id = (int) temp.get("id");
        model.createdDate = (Date) temp.get("createdDate");
        model.name = "" + temp.get("name");
        model.status = "" + temp.get("status");
        model.score = (int) temp.get("score");
        model.admin = User.find((int) temp.get("adminID"));
        model.amountMember = (int) temp.get("amountMember");
        model.modifiedDate = (Date) temp.get("modifiedDate");

        //**process of object mapping**, one group can has many members.
        model.members = new ArrayList<>();
        ArrayList<HashMap<String, Object>> user_temp = modelCollection.where("user", "groupID", "=", "" + model.id); //create user_temp to keep data from the database.
        for(HashMap<String, Object> item : user_temp) {
            User user = User.find((int) item.get("id"));
            model.members.add(user);
        }
        return model;
    }

    public static Group[] where(String colName, String operator, String value) //a static method that will provide a group of models of type 'group' by enter a condition such as 'a group that has at least 60 points'
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.where("groups", colName, operator, value); //query data from the database
        ArrayList<Group> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) { //if data objects > 0
            Group model = new Group(); //create new model to make data mapping
            //**data mapping process**
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("createdDate");
            model.name = "" + item.get("name");
            model.status = "" + item.get("status");
            model.score = (int) item.get("score");
            model.admin = User.find((int) item.get("adminID"));
            model.amountMember = (int) item.get("amountMember");
            model.modifiedDate = (Date) item.get("modifiedDate");
            //**map user models to group**
            model.members = new ArrayList<>();
            ArrayList<HashMap<String, Object>> user_temp = modelCollection.where("user", "groupID", "=", "" + model.id); //create user_temp to catch data from database.
            for(HashMap<String, Object> data : user_temp) {
                User user = User.find((int) data.get("id"));
                model.members.add(user);
            }

            collection.add(model); //add to ArrayList of group
        }

        return  collection.toArray(new Group[collection.size()]);
    }

    public static Group[] where(String colName, String operator, String value, String extraCondition) //where method with more conditions
    {
        return where(colName, operator, value + " " + extraCondition);
    }

    public static Group[] all() //get all of groups in the database
    {
        ArrayList<HashMap<String, Object>> temp = modelCollection.all("groups");
        ArrayList<Group> collection = new ArrayList<>();
        for(HashMap<String, Object> item : temp) {
            Group model = new Group();
            model.id = (int) item.get("id");
            model.createdDate = (Date) item.get("createdDate");
            model.name = "" + item.get("name");
            model.status = "" + item.get("status");
            model.score = (int) item.get("score");
            model.admin = User.find((int) item.get("adminID"));
            model.amountMember = (int) item.get("amountMember");
            model.modifiedDate = (Date) item.get("modifiedDate");

            model.members = new ArrayList<>();
            ArrayList<HashMap<String, Object>> user_temp = modelCollection.where("user", "groupID", "=", "" + model.id); //create user_temp to catch data from database.
            for(HashMap<String, Object> data : user_temp) {
                User user = User.find((int) data.get("id"));
                model.members.add(user);
            }

            collection.add(model);
        }

        return  collection.toArray(new Group[collection.size()]);
    }

    @Override
    public HashMap<String, Object> getValues() //transform all object's values to HashMap format
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //define date format
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", name); //map attribute name with value
        temp.put("status", status);
        temp.put("score", score);
        temp.put("amountMember", amountMember);
        temp.put("adminID", admin.getID());
        if(modifiedDate == null)
            temp.put("modifiedDate", null);
        else
            temp.put("modifiedDate", sdf.format(modifiedDate)); //convert date to defined format
        return temp;
    }

    @Override
    public HashMap<String, Object> getGeneralValues() //return only common values, external uses
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HashMap<String, Object> temp = new HashMap<>();
        Converter converter = Converter.getInstance();
        User[] arrayOfMembers = members.toArray(new User[members.size()]);
        temp.put("id", id);
        temp.put("name", name);
        temp.put("status", status);
        temp.put("score", score);
        temp.put("members", converter.ModelArrayToHashMapArray(arrayOfMembers));
        temp.put("amountMember", amountMember);
        temp.put("admin", admin.getGeneralValues());
        return temp;
    }

    public HashMap<String, Object> setName(String name)
    {
        this.name = name;
        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> setStatus(String status)
    {
        this.status = status;
        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> setAdmin(User user)
    {
        if(createdDate != null)
            return StatusDescription.createProcessStatus(false, "The group already has an admin user.");
        this.admin = user;
        addMember(user);

        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> setScore(int score)
    {
        if(score < 0)
            return StatusDescription.createProcessStatus(false, "The score cannot be under zero.");
        this.score = score;

        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> increaseScore(int score, String description)
    {
        HashMap<String, Object> temp_scoreLog = new HashMap<>(); //create HashMap to keep fields of 1 unsaved score log
        int changeScore = Math.abs(score); //get only positive integer

        this.score += changeScore; //change the current score
        //one score log consists of group id, current score(score after change), change score and description of changing score
        temp_scoreLog.put("id", id);
        temp_scoreLog.put("currentScore", this.score);
        temp_scoreLog.put("modifiedScore", changeScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog); //add to the temporary list

        updateModifiedDate();

        return StatusDescription.createProcessStatus(true); //return status of process is true when process is success
    }

    public HashMap<String, Object> decreaseScore(int score, String description)
    {
        HashMap<String, Object> temp_scoreLog = new HashMap<>();
        int changedScore = Math.abs(score);

        if(this.score - changedScore < 0) //a score after changing must be positive integer
            return StatusDescription.createProcessStatus(false, "The score cannot be under zero.");

        this.score -= changedScore;
        temp_scoreLog.put("id", id);
        temp_scoreLog.put("currentScore", this.score);
        temp_scoreLog.put("modifiedScore", -changedScore);
        temp_scoreLog.put("description", description);
        temp_scoreLogList.add(temp_scoreLog);

        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
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

    public int getScore()
    {
        return score;
    }

    public HashMap<String, Object> addMember(User user)
    {
        if(amountMember > 9)
            return StatusDescription.createProcessStatus(false, "The group has reached the maximum members limit now.");

        for(User item : temp_addedUserList) { //loop to check duplicated user
            if(item.getID() == user.getID())
                return StatusDescription.createProcessStatus(false, "This user is already added to the temporary added list.");
        }

        for(User item : members) { //loop to check duplicated user
            if (user.getID() == item.getID())
                return StatusDescription.createProcessStatus(false, "This user is already added to this group.");
        }

        temp_addedUserList.add(user);
        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    public HashMap<String, Object> removeMember(User user)
    {
        if(admin.getID() == user.getID())
            return StatusDescription.createProcessStatus(false, "Cannot remove admin user.");

        for(User item : temp_removeUserList) {
            if(item.getID() == user.getID())
                return StatusDescription.createProcessStatus(false, "This user is already added to the temporary removed list.");
        }

        if(User.find(user.id) == null)
            return StatusDescription.createProcessStatus(false, "This user is already removed from the database.");

        temp_removeUserList.add(user);
        updateModifiedDate();

        return StatusDescription.createProcessStatus(true);
    }

    @Override
    public HashMap<String, Object> save() //all processes will not be affected until this method is called
    {
        if(admin == null)
            return StatusDescription.createProcessStatus(false, "The group must have an admin user before saving.");

        if(checkRequiredFields() != null) //if some required fields are still not filled
            return checkRequiredFields(); //break saving process and return error with required fields name

        if(createdDate == null) { //if this model has never been saved
            HashMap<String, Object> temp = modelCollection.create(this); //create new row on the database and get id, created date

            if(temp == null)
                return StatusDescription.createProcessStatus(false, "Cannot save due to a database error."); //return error description with break saving process

            id = converter.toInt(temp.get("id")); //set id
            createdDate = (Date) temp.get("createdDate"); //set created date
            //return StatusDescription.createProcessStatus(true);
        }

        HashMap<String, Object> addGroupIDStatus = addGroupIDToUser(); //change group id of each user
        if(addGroupIDStatus != null && (boolean) addGroupIDStatus.get("status") == false) //if an error has occurred while changing group id of each user on the database
            return addGroupIDStatus; //break saving process and return error description

        HashMap<String, Object> removeGroupIDStatus = removeGroupIDFromUser(); //remove group id of each user
        if(removeGroupIDStatus != null && (boolean) removeGroupIDStatus.get("status") == false) //if an error has occurred while removing group id of each user on the database
            return removeGroupIDStatus; //break saving process and return error description

        HashMap<String, Object> addScoreLogStatus = addScoreLogToDatabase(); //add score log to database
        if(addScoreLogStatus != null && (boolean) addScoreLogStatus.get("status") == false) //if an error has occurred while adding score logs to the database
            return addScoreLogStatus; //break saving process and return error description

        return StatusDescription.createProcessStatus(modelCollection.save(this)); //if saving process is success
    }

    @Override
    public HashMap<String, Object> delete()
    {
        if(!modelCollection.manualDeleteData("group_score", "groupID = " + id)) //if deleting data on the database is not success
            return StatusDescription.createProcessStatus(false, "An error has occurred while deleting data on the database.");

        temp_removeUserList = members; //remove all members of group
        save(); //save to let process affects
        return StatusDescription.createProcessStatus(modelCollection.delete(this)); //return delete status
    }

    public Date getModifiedDate()
    {
        return modifiedDate;
    }

    public HashMap<String, Object> addGroupIDToUser()
    {
        if(temp_addedUserList.size() != 0) { //if temporary list is not empty
            for(User user : temp_addedUserList) {
                if(modelCollection.manualEditData("user", "groupID='" + id + "'", "id", "=", converter.toString(user.getID()))) {//if updating data on database is success
                    amountMember++; //increase amount of members
                    members.add(user);
                }
                else {
                    System.out.println("An error has occurred while adding a member."); //or database error
                    return StatusDescription.createProcessStatus(false, "An error has occurred while adding a member.");
                }

                temp_addedUserList.clear(); //clear temp list
                return StatusDescription.createProcessStatus(true);
            }
        }

        return null;
    }

    public HashMap<String, Object> removeGroupIDFromUser()
    {
        if(temp_removeUserList.size() != 0) { //if temporary list is not empty
            for(User user : temp_removeUserList) {
                if(modelCollection.manualEditData("user", "groupID=NULL", "id", "=", "" + user.getID())) {
                    amountMember--;

                    for(User item : members) {
                        if(item.getID() == user.getID()){
                            members.remove(item);
                            break;
                        }
                    }
                }
                else {
                    System.out.println("An error has occurred while removing a member.");
                    return StatusDescription.createProcessStatus(false, "An error has occurred while removing a member.");
                }
            }

            temp_removeUserList.clear(); //clear temp list
            return StatusDescription.createProcessStatus(true);
        }

        return null;
    }

    public HashMap<String, Object> addScoreLogToDatabase()
    {
        if(temp_scoreLogList.size() != 0) { //if temporary list is not empty
            String[] valuesSet = new String[temp_scoreLogList.size()]; //create array to keep a set of values of each score log

            for(int i = 0; i < temp_scoreLogList.size(); i++) {
                HashMap<String, Object> item = temp_scoreLogList.get(i);
                valuesSet[i] = "" + item.get("id") + ", " + item.get("currentScore") + ", " + item.get("modifiedScore") + ", '" + item.get("description") + "'"; //concat string
            }

            String colNameSet = "group_id, currentScore, modifiedScore, description"; //set of column name of values

            if(!modelCollection.manualInsertDataMultiple("group_score", colNameSet, valuesSet)) { //if inserting data to the database is error
                System.out.println("An error has occurred while adding a score log.");
                return StatusDescription.createProcessStatus(false, "An error has occurred while adding a score log.");
            }

            temp_scoreLogList.clear(); //clear temp score log list
            return StatusDescription.createProcessStatus(true);
        }

        return null;
    }
}
