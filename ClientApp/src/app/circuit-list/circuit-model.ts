export class CircuitModel {
    public id: number;
    public name: string;
    public location: string;
    public url: string;
    public latitude: number;
    public longitude: number;
    public country: string;
    constructor(id: number, name: string, location: string, country: string, url: string, latitude: number, longitude: number) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.country = country;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
      }
}
