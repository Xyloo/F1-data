package pl.pollub.f1data.Models.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Pitstop entity
 */
@Getter
@Setter
@Entity
@Table(name = "pitstops", schema = "f1datadb", indexes = {
        @Index(name = "raceId", columnList = "raceId")
})
public class Pitstop {
    @EmbeddedId
    private PitstopId id;

    @MapsId("raceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    @JsonBackReference("race-pitstop")
    private Race race;

    @MapsId("driverId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driverId", nullable = false)
    @JsonBackReference("driver-pitstop")
    private Driver driver;

    @NotNull
    @Column(name = "lap", nullable = false)
    private Integer lap;

    @NotNull
    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Size(max = 255)
    @Column(name = "duration")
    private String duration;

    @Column(name = "milliseconds")
    private Integer milliseconds;

}