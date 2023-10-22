import {BestLapTimeModel} from "./best-lap-time-model";

export class RaceModel {
    public raceId: number;
    public name: string;
    public year: number;
    public bestLapTime: BestLapTimeModel;
    public averageLapTime: string;
    public lapPitstopMap?: object[];
    constructor(raceId: number, name: string, year: number, bestLapTime: BestLapTimeModel, averageLapTime: string, lapPitstopMap?: object[]) {
        this.raceId = raceId;
        this.name = name;
        this.year = year;
        this.bestLapTime = bestLapTime;
        this.averageLapTime = averageLapTime;
        this.lapPitstopMap = lapPitstopMap;
    }
}
