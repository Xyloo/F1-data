package pl.pollub.f1data.Models.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "constructorresults")
public class Constructorresult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constructorResultsId", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "raceId", nullable = false)
    private Race race;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "constructorId", nullable = false)
    private Constructor constructor;

    @Column(name = "points")
    private Float points;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

}