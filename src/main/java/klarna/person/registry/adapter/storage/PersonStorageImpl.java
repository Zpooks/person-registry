package klarna.person.registry.adapter.storage;

import klarna.person.registry.domain.PersonStorage;
import klarna.person.registry.domain.model.Child;
import klarna.person.registry.domain.model.Person;

import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class PersonStorageImpl implements PersonStorage {

    /*
    Assumptions:
    1. Storage is not intended to scale and is reset every time the application restarts
     */
    HashMap<String, Person> personMap;

    public PersonStorageImpl() {
        this.personMap = new HashMap<>();
    }

    @Override
    public void savePerson(Person person) {
        personMap.put(person.ssn(), person);
    }

    /*
    Assumptions:
    1. The REST adapter is expected to handle any errors from the domain service
     */
    @Override
    public Person getPerson(String ssn) {
        if(!personMap.containsKey(ssn))
            throw new NoSuchElementException("Person with the provided SSN does not exist in storage.");

        return personMap.get(ssn);
    }

    /*
    Assumptions:
    1. No children are ever the same age
    2. Requesting the oldest child from a childless person results in an exception, as the behaviour is undefined
    3. The REST adapter is expected to handle any errors from the domain service
     */
    @Override
    public String getOldestChildsName(String ssn) {
        Person person = getPerson(ssn);

        if(person.children().isEmpty())
            throw new IllegalArgumentException("Requested person has no children.");

        return person.children().stream()
                .max(Comparator.comparingInt(Child::age))
                .get()
                .name();
    }

    @Override
    public void clear() {
        personMap.clear();
    }
}
