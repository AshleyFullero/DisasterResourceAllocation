package org.htech.disasterproject.utilities;

import org.htech.disasterproject.modal.Family;
import org.htech.disasterproject.modal.User;

public class SessionManager {

    private static int CurrentUserId;
    private static String CurrentUserName;
    private static int CurrentBarangayId;
    private static User CurrentUser;
    private static Family selectedFamily;

    private static User.Role selectedRole;


    public static int getCurrentUserId() {
        return CurrentUserId;
    }

    public static void setCurrentUserId(int currentUserId) {
        CurrentUserId = currentUserId;
    }

    public static String getCurrentUserName() {
        return CurrentUserName;
    }

    public static void setCurrentUserName(String currentUserName) {
        CurrentUserName = currentUserName;
    }

    public static int getCurrentBarangayId() {
        return CurrentBarangayId;
    }

    public static void setCurrentBarangayId(int currentBarangayId) {
        CurrentBarangayId = currentBarangayId;
    }

    public static User getCurrentUser() {
        return CurrentUser;
    }

    public static void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }

    public static Family getSelectedFamily() {
        return selectedFamily;
    }

    public static void setSelectedFamily(Family selectedFamily) {
        SessionManager.selectedFamily = selectedFamily;
    }

    public static User.Role getSelectedRole() {
        return selectedRole;
    }

    public static void setSelectedRole(User.Role selectedRole) {
        SessionManager.selectedRole = selectedRole;
    }
}
