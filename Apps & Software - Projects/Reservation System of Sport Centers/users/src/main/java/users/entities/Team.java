package users.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Team {

    @Id
    @Column(name = "team_uuid")
    private UUID uuid; // String ID, required

    @Column(name = "name")
    private String name; //required

    @ManyToMany
    @Column(name = "members")
    private List<User> members; //required

    /**
     * Instantiates a new Team.
     */
    public Team() {}

    /**
     * Instantiates a new Team.
     *
     * @param teamBuilder the team builder
     */
    public Team(TeamBuilder teamBuilder) {
        this.uuid = teamBuilder.uuid;
        this.name = teamBuilder.name;
        this.members = teamBuilder.members;
    }

    /**
     * Gets the team's uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the team's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the team's name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the team members as a list of users.
     *
     * @return the user [ ]
     */
    public List<User> getMembers() {
        return members;
    }

    /**
     * Sets the team's members.
     *
     * @param members the members
     */
    public void setMembers(List<User> members) throws IllegalArgumentException {
        if (members == null) {
            throw new IllegalArgumentException();
        }
        this.members = members;
    }

    /**
     * Checks if two Team objects are equal.
     *
     * @param o Object
     * @return boolean depending on their equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        return Objects.equals(getUuid(), team.getUuid());
    }

    /**
     * Return the hashcode of the object.
     *
     * @return the int hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getName(), getMembers());
    }

    /**
     * Return the Team in string form.
     *
     * @return the Team in String form.
     */
    @Override
    public String toString() {
        return "Team{"
                + "uuid=" + uuid
                + ", name='" + name + '\''
                + ", members=" + members
                + '}';
    }

    /**
     * The Team builder.
     */
    public static class TeamBuilder {

        private final UUID uuid;

        private String name;

        private List<User> members;

        /**
         * Instantiates a new Team builder.
         */
        public TeamBuilder() {
            this.uuid = UUID.randomUUID();
            members = new ArrayList<>();
        }

        /**
         * Set the name of our team.
         *
         * @param name the name
         * @return the team builder
         */
        public TeamBuilder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the members of the team.
         *
         * @param members the members
         * @return the team builder
         */
        public TeamBuilder withMembers(List<User> members) {
            this.members = members;
            return this;
        }

        /**
         * Build the team object using our TeamBuilder.
         *
         * @return the team
         */
        public Team build() {
            Team team = new Team(this);
            validateTeamObject(team);
            return team;
        }

        /**
         * Checks if we have created an invalid Team object.
         *
         * @param team the team
         * @return whether the object is valid or not
         */
        public boolean validateTeamObject(Team team) throws IllegalArgumentException {
            //Do some basic validations to check
            //if user object does not break any assumption of the system
            if (team.name == null || team.members == null) {
                throw new IllegalArgumentException();
            }
            return true;
        }

    }
}
