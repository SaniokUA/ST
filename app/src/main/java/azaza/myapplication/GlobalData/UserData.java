package azaza.myapplication.GlobalData;


import android.graphics.Bitmap;

public class UserData {

    public static String userID = "";
    public static String userName = "";
    public static String firstName = "";
    public static String lastName = "";
    public static String emailId = "";
    public static String Sex = "";
    public static String SocketId = "";
    public static String Email = "";
    public static String Location = "";
    public static String Age = "";
    public static String Language = "";
    public static String userPhoto = "";
    public static Bitmap userPhotoDrawble;
    public static boolean userConnected;

    public static String getLanguage() {
        return Language;
    }

    public static void setLanguage(String language) {
        Language = language;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        UserData.userID = userID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserData.userName = userName;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        UserData.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        UserData.lastName = lastName;
    }

    public static String getEmailId() {
        return emailId;
    }

    public static void setEmailId(String emailId) {
        UserData.emailId = emailId;
    }

    public static String getSex() {
        return Sex;
    }

    public static void setSex(String sex) {
        Sex = sex;
    }

    public static String getSocketId() {
        return SocketId;
    }

    public static void setSocketId(String socketId) {
        SocketId = socketId;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getLocation() {
        return Location;
    }

    public static void setLocation(String location) {
        Location = location;
    }

    public static String getAge() {
        return Age;
    }

    public static void setAge(String age) {
        Age = age;
    }

    public static String getUserPhoto() {
        return userPhoto;
    }

    public static void setUserPhoto(String userPhoto) {
        UserData.userPhoto = userPhoto;
    }

    public static Bitmap getUserPhotoDrawble() {
        return userPhotoDrawble;
    }

    public static void setUserPhotoDrawble(Bitmap userPhotoDrawble) {
        UserData.userPhotoDrawble = userPhotoDrawble;
    }
    public static boolean isUserConnected() {
        return userConnected;
    }

    public static void setUserConnected(boolean userConnected) {
        UserData.userConnected = userConnected;
    }

}
