package himma.pendidikan.util;

import himma.pendidikan.model.Karyawan;

public class Session {
    private static Karyawan currentUser;

    public static void setCurrentUser(Karyawan user) {
        currentUser = user;
    }

    public static Karyawan getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
