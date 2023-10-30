package pl.pollub.f1data.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pollub.f1data.Models.DTOs.DriverBestTimeDto;
import pl.pollub.f1data.Services.RacingService;

/**
 * This class is responsible for handling requests related to racing.
 */
@RestController
@RequestMapping("api/racing")
public class RacingController {

    @Autowired
    RacingService racingService;

    /**
     * This endpoint returns the best time and driver in a given race.
     * @param raceId race id
     * @return <p>• HTTP 200 with {@link DriverBestTimeDto} response</p>
     * <p>• HTTP 404 if no data was found for given race id (or race id does not exist)</p>
     */
    @GetMapping("/{raceId}/best-time")
    public ResponseEntity<DriverBestTimeDto> getBestRaceTimeByRaceId(@PathVariable Integer raceId) {
        DriverBestTimeDto bestTime = racingService.getBestRaceTimeByRaceId(raceId).orElse(null);
        if (bestTime != null) {
            return ResponseEntity.ok(bestTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint returns the average time of all drivers in a given race.
     * @param raceId race id
     * @return <p>• HTTP 200 with race's average time as String</p>
     * <p>• HTTP 404 if no data was found for given race id (or race id does not exist)</p>
     */
    @GetMapping("/{raceId}/average-time")
    public ResponseEntity<String> getAverageRaceTime(@PathVariable Integer raceId) {
        String averageTime = racingService.getAverageRaceTime(raceId);
        if (averageTime != null) {
            return ResponseEntity.ok(averageTime);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint returns the pitstop map (lap, pitstop count) in a given race.
     * @param raceId race id
     * @return <p>• HTTP 200 with pitstop map</p>
     * <p>• HTTP 404 if no data was found for given race id (or race id does not exist)</p>
     */
    @GetMapping("/{raceId}/pitstops")
    public ResponseEntity<?> getPitstopsCountByLapForRace(@PathVariable Integer raceId) {
        return ResponseEntity.ok(racingService.getPitstopsCountByLapForRace(raceId));
    }

}
