package Application.Entities;

import Application.LoggerUtility;

import java.util.logging.Logger;

public class Profile {
    public String name;
    public String phoneNumber;
    public String location;
    public int profileId;

    public Profile() {

    }

    public Profile(int profileId, String name, String phoneNumber, String location) {
        this.profileId = profileId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }





    public boolean areChangesSaved() {
      // TODO: connect to DB
        return true;
    }

    public boolean viewOrderHistory() {
        //TODO: connect to DB
//        for (Order order:this.orderHistory)
//        {
//            logger.info(order.toString());
//        }
        return true;
    }

    public boolean viewInstallationRequests() {
        //TODO: connect to DB
//        for (InstallationRequest requstes :this.installationRequests)
//        {
//            logger.info(requstes.toString());
//        }
        return true;
    }

    public int getProfileId() {
        return profileId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return profileId+"";
    }
}
