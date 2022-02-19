package com.example.offerdaysongs.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    @ToString.Exclude
    Singer singer;

    @OneToMany(mappedBy = "recording")
    @ToString.Exclude
    List<Copyright> copyrights;

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
