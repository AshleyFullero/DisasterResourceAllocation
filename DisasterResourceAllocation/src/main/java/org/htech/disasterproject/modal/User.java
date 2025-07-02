package org.htech.disasterproject.modal;

public class User {

    public enum Role { ADMIN, CHAIRMAN, VOLUNTEER }

    private int userId;
    private String username;
    private String password;
    private String email;
    private Role role;
    private int barangayId;

    public User() {
    }

    public User(int id, String username, String passwordHash, Role role, int barangayId) {
        this.userId = id;
        this.username = username;
        this.password = passwordHash;
        this.role = role;
        this.barangayId = barangayId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getBarangayId() {
        return barangayId;
    }

    public void setBarangayId(int barangayId) {
        this.barangayId = barangayId;
    }
}