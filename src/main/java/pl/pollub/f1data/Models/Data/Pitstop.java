package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "pitstops", indexes = {
        @Index(name = "raceId", columnList = "raceId")
})
public class Pitstop {
    @EmbeddedId
    private PitstopId id;

    @MapsId("raceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    private Race race;

    @MapsId("driverId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driverId", nullable = false)
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