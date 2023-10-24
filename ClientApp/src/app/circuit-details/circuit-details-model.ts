import {RaceModel} from "./race-model";

export class CircuitDetailsModel {
    public circuitId: number;
    public name: string;
    public circuitRef: string;
    public location: string;
    public country: string;
    public races: RaceModel[];
    constructor(circuitId: number, name: string, circuitRef: string, location: string, country: string, races: RaceModel[]) {
        this.circuitId = circuitId;
        this.name = name;
        this.circuitRef = circuitRef;
        this.location = location;
        this.country = country;
        this.races = races;
    }
}
