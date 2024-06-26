package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Constructor results entity

 */
@Getter
@Setter
@Entity
@Table(name = "constructorresults", schema = "f1datadb")
public class Constructorresult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constructorResultsId", nullable = false)
    @JsonProperty("constructorResultsId")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    @JsonBackReference(value = "race-constructorresult")
    private Race race;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "constructorId", nullable = false)
    @JsonBackReference(value = "constructor-constructorresult")
    private Constructor constructor;

    @Column(name = "points")
    private Float points;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

}