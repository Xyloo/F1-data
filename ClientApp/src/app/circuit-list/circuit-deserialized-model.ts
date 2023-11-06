export class CircuitDeserializedModel {
  public circuitId : number;
  public circuitRef: string;
  public name: string;
  public location: string;
  public country: string;
  public lat: number;
  public lng: number;
  public alt: number;
  public url: string;
  constructor(circuitId: number, circuitRef: string, name: string, location: string, country: string, lat: number, lng: number, alt: number, url: string) {
    this.circuitId = circuitId;
    this.circuitRef = circuitRef;
    this.name = name;
    this.location = location;
    this.country = country;
    this.lat = lat;
    this.lng = lng;
    this.alt = alt;
    this.url = url;
  }
}
