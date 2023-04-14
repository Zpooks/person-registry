package klarna.person.registry.domain;

import klarna.person.registry.domain.dto.OldestChildDto;
import klarna.person.registry.domain.model.Person;

public class RegistryServiceImpl implements RegistryService {


    private final PersonStorage personStorage;

    public RegistryServiceImpl(PersonStorage personStorage) {
        this.personStorage = personStorage;
    }

    /*
    Assumptions:
    1. Person gets deserialized by the REST adapter
    2. Values are validated such that:
        - Each attribute of all objects is set, except Spouse which can be null
        - If Spouse is set, Spouses name cannot be null
        - Children is at least an empty list, never null
     */
    @Override
    public void savePerson(Person person) {
        personStorage.savePerson(person);
    }

    @Override
    public Person getPerson(String ssn) {
        return personStorage.getPerson(ssn);
    }

    @Override
    public OldestChildDto getOldestChild(String ssn) {
        return new OldestChildDto(personStorage.getOldestChildsName(ssn), ssn);
    }
}
