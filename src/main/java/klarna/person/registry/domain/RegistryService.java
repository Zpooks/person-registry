package klarna.person.registry.domain;

import klarna.person.registry.domain.dto.OldestChildDto;
import klarna.person.registry.domain.model.Person;

public interface RegistryService {

    void savePerson(Person person);

    Person getPerson(String ssn);

    OldestChildDto getOldestChild(String ssn);
}
