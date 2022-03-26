function Room (name, id, owner){
    this.name = name;
    this.id = id;
    this.owner = owner;
    this.roomPeople = [];
    this.peopleLimit = 2;
    this.status = "available";
    this.private = false;
}

Room.prototype.addPerson = function(personID) {
    if(this.status === "available")
        this.roomPeople.push(personID);
};

Room.prototype.removePerson = function(person) {
    let personIndex = -1
    for(let i = 0; i < this.roomPeople.length; i++){
        if(this.roomPeople[i].id === person.id){
            personIndex = i;
            break;
        }
    }
    this.roomPeople.remove(personIndex);
};

Room.prototype.getPerson = function(personID) {
    let person = null;
    for(let i = 0; i < this.roomPeople.length; i++){
        if(this.roomPeople[i].id === personID){
            person = this.roomPeople[i];
            break;
        }
    }
    return person;
};

Room.prototype.isAvailable = function() {
    return this.available = "available";
};

Room.prototype.isPrivate = function() {
    return this.private;
};

module.exports = Room;