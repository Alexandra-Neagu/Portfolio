package users.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_uuid")
    private UUID uuid; // String ID, required

    @Column(name = "name")
    private String name; // required

    // basic subscription -> 1 reservation/day
    // premium subscription -> 3 reservations/day
    @Column(name = "hasPremium")
    private boolean hasPremium; // required, if not set, false by default

    @ManyToMany
    @Column(name = "reservations")
    private List<ReducedReservation> reservations;

    public User() {
    }

    /**
     * Instantiates a new User with a default constructor.
     *
     * @param userBuilder the user builder
     */
    public User(UserBuilder userBuilder) {
        this.uuid = userBuilder.uuid;
        this.name = userBuilder.name;
        this.hasPremium = userBuilder.hasPremium;
        this.reservations = userBuilder.reservations;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets reservations.
     *
     * @return the reservations
     */
    public List<ReducedReservation> getReservations() {
        return reservations;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * Gets hasPremium boolean.
     *
     * @return the hasPremium
     */
    public boolean getHasPremium() {
        return hasPremium;
    }

    /**
     * Sets hasPremium boolean.
     *
     * @param hasPremium the hasPremium
     */
    public void setHasPremium(boolean hasPremium) {
        this.hasPremium = hasPremium;
    }

    /**
     * Checks if two User objects are equal.
     *
     * @param o Object
     * @return boolean depending on their equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User other = (User) o;
        return this.uuid.equals(other.uuid);
    }

    /**
     * Returns the hash code of the object.
     *
     * @return the int hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, hasPremium, reservations);
    }

    /**
     * Returns the String representation of the object.
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return "User{"
                + "uuid=" + uuid
                + ", name='" + name + "\'"
                + ", hasPremium=" + hasPremium
                + ", reservations=" + reservations
                + '}';
    }

    /**
     * The type User builder class.
     */
    public static class UserBuilder {
        private UUID uuid;

        private String name;

        private boolean hasPremium;

        private List<ReducedReservation> reservations;

        /**
         * Instantiates a new User builder.
         */
        public UserBuilder() {
            this.hasPremium = false;
            this.reservations = new ArrayList<>();
        }

        /**
         * Sets the UUID for the User builder.
         *
         * @param uuid the UUID of the User
         * @return the user builder
         */
        public UserBuilder withUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        /**
         * Sets the name for the User builder.
         *
         * @param name the name
         * @return the user builder
         */
        public UserBuilder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the hasPremium for the User builder.
         *
         * @param hasPremium the has premium
         * @return the user builder
         */
        public UserBuilder withHasPremium(boolean hasPremium) {
            this.hasPremium = hasPremium;
            return this;
        }

        /**
         * Builds the User object from the User builder.
         *
         * @return the user
         */
        public User build() {
            User user =  new User(this);
            validateUserObject(user);
            return user;
        }

        /**
         * Checks if we have created an invalid User object.
         *
         * @param user the user
         * @return whether the object is valid or not
         */
        private boolean validateUserObject(User user) throws IllegalArgumentException {
            //Do some basic validations to check
            //if user object does not break any assumption of the system
            if (user.uuid == null || user.name == null) {
                throw new IllegalArgumentException();
            }
            return true;
        }
    }
}
