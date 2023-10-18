package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "laptimes", indexes = {
        @Index(name = "raceId", columnList = "raceId"),
        @Index(name = "raceId_2", columnList = "raceId")
})
public class Laptime {
    @EmbeddedId
    private LaptimeId id;

    @MapsId("raceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    private Race race;

    @MapsId("driverId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driverId", nullable = false)
    private Driver driver;

    @Column(name = "position")
    private Integer position;

    @Size(max = 255)
    @Column(name = "time")
    private String time;

    @Column(name = "milliseconds")
    private Integer milliseconds;

}