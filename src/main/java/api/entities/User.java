package api.entities;

public class User {
    String id;
    String name;
    String phone;
    String role;
    String strikes;
    String location;

    public User() {
    }

    public User(String id, String name, String phone, String role, String location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.location = location;
    }

    public User(String id, String name, String phone, String role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public User(String id, String name, String phone, String role, String strikes, String location) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.strikes = strikes;
        this.location = location;
    }

    public void setId(String id) {
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

    public String getId() {
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
