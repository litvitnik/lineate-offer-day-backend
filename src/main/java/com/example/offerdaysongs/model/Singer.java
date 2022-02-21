package com.example.offerdaysongs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Singer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "singer", fetch = FetchType.EAGER)
    Collection<Recording> recordings = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Singer singer = (Singer) o;
        return id != null && Objects.equals(id, singer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
