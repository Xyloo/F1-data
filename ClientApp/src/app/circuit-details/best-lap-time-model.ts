export class BestLapTimeModel {
    public forename: string;
    public surname: string;
    public number?: number;
    public bestLapTime: string;

    constructor(forename: string, surname: string, number: number, bestLapTime: string) {
        this.forename = forename;
        this.surname = surname;
        this.number = number;
        this.bestLapTime = bestLapTime;
    }
}
