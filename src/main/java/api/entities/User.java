package api.entities;

public class User {
    int id;
    String name;
    String phone;
    String role;
    String strikes;
    String location;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStrikes(String strikes) {
        this.strikes = strikes;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getStrikes() {
        return strikes;
    }

    public String getLocation() {
        return location;
    }
}
