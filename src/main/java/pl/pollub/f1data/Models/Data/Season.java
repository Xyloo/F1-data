package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "seasons", schema = "f1datadb", indexes = {
        @Index(name = "url", columnList = "url", unique = true)
})
public class Season {
    @Id
    @Column(name = "year", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "year")
    private Set<Race> races = new LinkedHashSet<>();

}