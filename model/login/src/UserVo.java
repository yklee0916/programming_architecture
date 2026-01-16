package model.login.src;

public final class UserVo {
    private final String username;

    public UserVo(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username cannot be null");
        }
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVo userVo = (UserVo) o;
        return username.equals(userVo.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
