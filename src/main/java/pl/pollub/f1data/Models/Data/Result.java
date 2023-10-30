package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Race result entity
 */
@Getter
@Setter
@Entity
@Table(name = "results", schema = "f1datadb")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resultId", nullable = false)
    @JsonProperty("resultId")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    @JsonBackReference(value = "race-result")
    private Race race;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driverId", nullable = false)
    @JsonBackReference(value = "driver-result")
    private Driver driver;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "constructorId", nullable = false)
    @JsonBackReference(value = "constructor-result")
    private Constructor constructor;

    @Column(name = "number")
    private Integer number;

    @NotNull
    @Column(name = "grid", nullable = false)
    private Integer grid;

    @Column(name = "position")
    private Integer position;

    @Size(max = 255)
    @NotNull
    @Column(name = "positionText", nullable = false)
    private String positionText;

    @NotNull
    @Column(name = "positionOrder", nullable = false)
    private Integer positionOrder;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @NotNull
    @Column(name = "laps", nullable = false)
    private Integer laps;

    @Size(max = 255)
    @Column(name = "time")
    private String time;

    @Column(name = "milliseconds")
    private Integer milliseconds;

    @Column(name = "fastestLap")
    private Integer fastestLap;

    @Column(name = "rank")
    private Integer rank;

    @Size(max = 255)
    @Column(name = "fastestLapTime")
    private String fastestLapTime;

    @Size(max = 255)
    @Column(name = "fastestLapSpeed")
    private String fastestLapSpeed;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusId", nullable = false)
    @JsonBackReference(value = "status-result")
    private Status status;

}