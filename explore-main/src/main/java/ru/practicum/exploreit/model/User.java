package ru.practicum.exploreit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @Override
    public boolean equals(Object o) {
        //System.out.println("EQUALS");
        //System.out.println((o instanceof User));
        //System.out.println(o.getClass());
        //System.out.println("this == o ");
        //System.out.println(this == o);
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        //System.out.println("email " + email);
        //System.out.println("user.email" + user.email);
        //System.out.println(email.equals(user.email));
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
