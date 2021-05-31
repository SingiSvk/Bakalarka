package com.example.bakalarka.database;

import androidx.annotation.NonNull;

import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.events.Event;
import com.example.bakalarka.data.room.patient.Illness;
import com.example.bakalarka.data.room.patient.Patient;
import com.example.bakalarka.data.risks.Conditions;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.data.sensor.GasensSensor;
import com.example.bakalarka.database.conditions.ConditionsDB;
import com.example.bakalarka.database.conditions.ConditionsEntity;
import com.example.bakalarka.database.event.EventDB;
import com.example.bakalarka.database.event.EventEntity;
import com.example.bakalarka.database.illness.IllnessDB;
import com.example.bakalarka.database.illness.IllnessEntity;
import com.example.bakalarka.database.person.PersonDB;
import com.example.bakalarka.database.person.PersonEntity;
import com.example.bakalarka.database.room.RoomDB;
import com.example.bakalarka.database.room.RoomEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDatabaseController {
    // Deleting data in DB
    public void deleteRoomsInDB(){
        for (Room room: RoomController.rooms){
            deleteWholeRoom(room.getId());
        }
    }

    public void deleteWholeRoom(int roomId){
        deleteConditionsInDB(roomId);
        deletePersonInDB(roomId);
        deleteEventsInDB(roomId);
        deleteRoomInDB(roomId);
    }

    public void deleteRoomInDB(int roomId){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        roomDB.roomDao().delete(getRoomEntity(roomId));
    }

    public void deletePersonInDB(int roomId){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        PersonEntity personEntity = getPersonEntity(roomId);
        deleteIllnessesInDB(personEntity.getId());
        personDB.personDao().delete(personEntity);
    }

    public void deleteIllnessesInDB(int personId){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);
        List<IllnessEntity> illnessEntities = getIllnessEntities(personId);
        for (IllnessEntity illnessEntity: illnessEntities){
            illnessDB.illnessDao().delete(illnessEntity);
        }
    }

    public void deleteEventsInDB(int roomId){
        EventDB eventDB = EventDB.getDatabase(MainActivity.context);
        List<EventEntity> eventEntities = getEventEntities(roomId);
        for (EventEntity eventEntity: eventEntities){
            eventDB.eventDao().delete(eventEntity);
        }
    }

    public void deleteEventInDB(EventEntity eventEntity){
        EventDB eventDB = EventDB.getDatabase(MainActivity.context);
        eventDB.eventDao().delete(eventEntity);
    }

    public void deleteConditionsInDB(int roomId){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        List<ConditionsEntity> conditionsEntities = getConditionsEntities(roomId);
        for(ConditionsEntity conditionsEntity: conditionsEntities){
            conditionsDB.conditionsDao().delete(conditionsEntity);
        }
    }

    // Update
    public void updateWholeRoomInDB(@NonNull Room room){
        updateConditionsInDB(room.getId(), room.getConditionsMap());
        updatePersonInDB(room.getId(), room.getPatient());
        updateEventsInDB(room.getId(), room.getEvents());
        updateRoomInDB(room);
    }

    public void updateRoomInDB(@NonNull Room room){
        deleteRoomInDB(room.getId());
        saveRoomInDB(room.getId(), room.getRoomName());
    }

    public void updatePersonInDB(int roomId, @NonNull Patient patient){
        updateIllnessesInDB(getPersonEntity(roomId).getId(), patient.getIllnesses());
        deletePersonInDB(roomId);
        savePersonInDB(roomId, patient);
    }

    public void updateIllnessesInDB(int personId, @NonNull List<Illness> illnesses){
        deleteIllnessesInDB(personId);
        saveIllnessesInDB(personId, illnesses);
    }

    public void updateEventsInDB(int personId, @NonNull List<Event> events){
        deleteEventsInDB(personId);
        saveEventsInDB(personId, events);
    }

    public void updateConditionsInDB(int roomId, @NonNull Map<String, Conditions> conditionsMap){
        deleteConditionsInDB(roomId);
        saveConditionsInDB(roomId, conditionsMap);
    }

    // Save
    public void saveWholeRoomInDB(@NonNull Room room){
        saveRoomInDB(room.getId(), room.getRoomName());
        saveConditionsInDB(room.getId(), room.getConditionsMap());
        savePersonInDB(room.getId(), room.getPatient());
        listRooms();
    }

    public void saveRoomInDB(int roomId, String name){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        RoomEntity roomEntity = new RoomEntity(roomId, name);
        roomDB.roomDao().insert(roomEntity);
    }

    public void saveConditionsInDB(int roomId, @NonNull Map<String, Conditions> conditionsMap){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        for(String condition: conditionsMap.keySet()){
            ConditionsEntity conditionsEntity = new ConditionsEntity(roomId, condition, conditionsMap.get(condition));
            conditionsDB.conditionsDao().insert(conditionsEntity);
        }
    }

    public void savePersonInDB(int roomId, @NonNull Patient patient){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        PersonEntity personEntity = new PersonEntity(roomId, patient);
        personDB.personDao().insertPerson(personEntity);
        saveIllnessesInDB(personEntity.getId(), patient.getIllnesses());
    }

    public void saveIllnessesInDB(int personId, @NonNull List<Illness> illnessesList){
        for (Illness illness: illnessesList){
            saveIllnessInDB(personId, illness);
        }
    }

    public void saveIllnessInDB(int personId, Illness illness){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);

        IllnessEntity illnessEntity = new IllnessEntity(personId, illness);
        illnessDB.illnessDao().insert(illnessEntity);
    }

    public void saveEventsInDB(int personId, @NonNull List<Event> eventList){
        for (Event event: eventList){
            saveEventInDB(personId, event);
        }
    }

    public void saveEventInDB(int roomId, Event event){
        EventDB eventDB = EventDB.getDatabase(MainActivity.context);

        EventEntity eventEntity = new EventEntity(roomId, event.getTime(), event.getEvent(), event.getRisk());
        eventDB.eventDao().insert(eventEntity);
    }

    // Getting from DB
    public RoomEntity getRoomEntity(int roomId){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        return roomDB.roomDao().findRoomById(roomId);
    }

    public PersonEntity getPersonEntity(int roomId){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        return personDB.personDao().findPersonByRoomId(roomId);
    }

    @NonNull
    public List<IllnessEntity> getIllnessEntities(int personId){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);
        return illnessDB.illnessDao().findIllnessesByPersonId(personId);
    }

    @NonNull
    public List<EventEntity> getEventEntities(int roomId){
        EventDB eventDB = EventDB.getDatabase(MainActivity.context);
        return eventDB.eventDao().findEventsByRoomId(roomId);
    }

    @NonNull
    public List<ConditionsEntity> getConditionsEntities(int roomId){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        return conditionsDB.conditionsDao().findConditionsByRoomId(roomId);
    }

    @NonNull
    public List<RoomEntity> listRooms(){
        RoomDB roomDB = RoomDB.getDatabase(MainActivity.context);
        for (RoomEntity roomEntity: roomDB.roomDao().getAll()){
            System.out.println(roomEntity.toString());
        }
        return roomDB.roomDao().getAll();
    }

    public void listPersons(){
        PersonDB personDB = PersonDB.getDatabase(MainActivity.context);
        for (PersonEntity personEntity: personDB.personDao().getAll()){
            System.out.println(personEntity.toString());
        }
        personDB.personDao().getAll();
    }

    public void listIllnesses(){
        IllnessDB illnessDB = IllnessDB.getDatabase(MainActivity.context);
        for (IllnessEntity illnessEntity: illnessDB.illnessDao().getAll()){
            System.out.println(illnessEntity.toString());
        }
        illnessDB.illnessDao().getAll();
    }

    public List<EventEntity> listEvents(){
        EventDB eventDB = EventDB.getDatabase(MainActivity.context);
        for (EventEntity eventEntity: eventDB.eventDao().getAll()){
            System.out.println(eventEntity.toString());
        }
        return eventDB.eventDao().getAll();
    }

    public List<EventEntity> list50Events(){
        EventDB eventDB = EventDB.getDatabase(MainActivity.context);
        for (EventEntity eventEntity: eventDB.eventDao().getAll()){
            System.out.println(eventEntity.toString());
        }
        return eventDB.eventDao().get50();
    }

    public void listConditions(){
        ConditionsDB conditionsDB = ConditionsDB.getDatabase(MainActivity.context);
        for (ConditionsEntity conditionsEntity: conditionsDB.conditionsDao().getAll()){
            System.out.println(conditionsEntity.toString());
        }
    }

    @NonNull
    public List<Illness> createIllnessesFromDB(@NonNull List<IllnessEntity> illnessEntities){
        List<Illness> illnesses = new ArrayList<>();
        for (IllnessEntity illnessEntity: illnessEntities){
            Illness illness = new Illness(illnessEntity.getName());
            illnesses.add(illness);
        }
        return illnesses;
    }

    @NonNull
    public List<Event> createEventsFromDB(@NonNull List<EventEntity> eventEntities){
        List<Event> events = new ArrayList<>();
        for (EventEntity eventEntity: eventEntities){
            if (events.size() > 50){
                deleteEventInDB(eventEntity);
            }else{
                Event event = new Event(eventEntity.getRoomId(), eventEntity.getTime(), eventEntity.getEvent(), eventEntity.getRisk());
                events.add(event);
            }
        }
        System.out.println("Num of events in db: " + eventEntities.size());
        System.out.println("Num of events: " + events.size());
        System.out.println("events: " +events.toString());
        return events;
    }

    @NonNull
    public Map<String, Conditions> createConditionsFromDB(@NonNull List<ConditionsEntity> conditionsEntities){
        Map<String, Conditions> conditionsMap = new HashMap<>();

        for (ConditionsEntity conditionsEntity: conditionsEntities){
            Conditions conditions = new Conditions(
                    conditionsEntity.getMax(),
                    conditionsEntity.getHigh(),
                    conditionsEntity.getIdeal(),
                    conditionsEntity.getLow(),
                    conditionsEntity.getMin());
            conditionsMap.put(conditionsEntity.getCondition(), conditions);
        }
        return conditionsMap;
    }

    @NonNull
    public Patient createPersonFromDB(@NonNull PersonEntity personEntity, List<Illness> illnesses){
        return new Patient(personEntity.getName(), personEntity.getAge(), illnesses);
    }

    @NonNull
    public Room createRoomFromDB(int roomId){
        PersonEntity personEntity = getPersonEntity(roomId);
        List<Illness> illnesses = createIllnessesFromDB(getIllnessEntities(personEntity.getId()));
        Patient patient = createPersonFromDB(personEntity, illnesses);
        List<Event> events = createEventsFromDB(getEventEntities(roomId));
        Map<String, Conditions> conditionsMap = createConditionsFromDB(getConditionsEntities(roomId));
        GasensSensor gasensSensor = new GasensSensor(roomId);

        return new Room(gasensSensor.getId(), getRoomEntity(roomId).getName(), patient, conditionsMap, events);
    }

    @NonNull
    public List<Room> getRoomsFromDB(){
        List<Room> rooms = new ArrayList<>();
        for (RoomEntity roomEntity: listRooms()){
            Room room = createRoomFromDB(roomEntity.getId());
            rooms.add(room);
        }
        return rooms;
    }
}
