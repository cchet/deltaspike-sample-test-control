package com.gepardec.samples.deltaspike.test.control.jpa;

import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Thomas Herzog <thomas.herzog@gepardec.com>
 * @since 9/17/18
 */
@Entity
@Table(name = "user_table")
public class UserEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "idGenerator",
            sequenceName = "userIdSequence",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "idGenerator",
            strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name")
    @NotNull
    @Length(min = 1, max = 50)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Length(min = 1, max = 50)
    private String lastName;

    @Column(name = "email")
    @NotNull
    @Email
    @Length(min = 1, max = 50)
    private String email;

    public UserEntity() {
    }

    public UserEntity(String firstName,
                      String lastName,
                      String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        return id != null ? id.equals(that.id) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
