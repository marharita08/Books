package sumdu.edu.ua.entities;

import java.util.Objects;

public class Session {
    private String username;
    private String jwtToken;

    public Session() {
    }

    public Session(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(username, session.username) && Objects.equals(jwtToken, session.jwtToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, jwtToken);
    }

    @Override
    public String toString() {
        return "Session{" +
                "username='" + username + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
