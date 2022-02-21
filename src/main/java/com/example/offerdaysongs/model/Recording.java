package com.example.offerdaysongs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Recording {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String version;
    ZonedDateTime releaseTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "singer_id", insertable = false, updatable = false)
    @ToString.Exclude
    Singer singer;

    @OneToMany(mappedBy = "recording", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    Collection<Copyright> copyrights = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recording recording = (Recording) o;
        return id != null && Objects.equals(id, recording.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
