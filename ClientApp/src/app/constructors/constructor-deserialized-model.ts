export class ConstructorDeserializedModel{
    public id : number;
    public constructorRef: string;
    public name: string;
    public nationality: string;
    public url: string;
    constructor(id: number, constructorRef: string, name: string, nationality: string, url: string){
        this.id = id;
        this.constructorRef = constructorRef;
        this.name = name;
        this.nationality = nationality;
        this.url = url;
    }
}
