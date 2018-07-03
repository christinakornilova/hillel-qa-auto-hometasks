package facebook;

public class FacebookUser {
    private String firstName;
    private String lastName;
    private String id;

//    public FacebookUser(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

    public FacebookUser(String firstName, String lastName, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
