package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Season entity
 */
@Getter
@Setter
@Entity
@Table(name = "seasons", schema = "f1datadb", indexes = {
        @Index(name = "url", columnList = "url", unique = true)
})
public class Season {
    @Id
    @Column(name = "year", nullable = false)
    @JsonProperty("year")
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "year")
    @JsonManagedReference(value = "race-year")
    private Set<Race> races = new LinkedHashSet<>();

    /**
     * Constructor
     * @param year year
     */
    public Season(int year) {
        this.id = year;
    }

    /**
     * Parameterless constructor for JPA
     */
    public Season() {
    }
}