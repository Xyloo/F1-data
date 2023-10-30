package pl.pollub.f1data.Models.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * PitstopId entity (a composite primary key for {@link Pitstop})
 */
@Getter
@Setter
@Embeddable
public class PitstopId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3315394335165689304L;
    /**
     * Race id
     * @see Race
     */
    @NotNull
    @Column(name = "raceId", nullable = false)
    private Integer raceId;

    /**
     * Driver id
     * @see Driver
     */
    @NotNull
    @Column(name = "driverId", nullable = false)
    private Integer driverId;

    /**
     * Stop number
     */
    @NotNull
    @Column(name = "stop", nullable = false)
    private Integer stop;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PitstopId entity = (PitstopId) o;
        return Objects.equals(this.raceId, entity.raceId) &&
                Objects.equals(this.driverId, entity.driverId) &&
                Objects.equals(this.stop, entity.stop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceId, driverId, stop);
    }

}