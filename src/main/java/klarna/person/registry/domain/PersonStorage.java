package klarna.person.registry.domain;

import klarna.person.registry.domain.model.Person;

public interface PersonStorage {

    void savePerson(Person person);

    Person getPerson(String ssn);

    String getOldestChildsName(String ssn);

    void clear();
}
