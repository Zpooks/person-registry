package klarna.person.registry.domain;

import klarna.person.registry.adapter.storage.PersonStorageImpl;
import klarna.person.registry.domain.dto.OldestChildDto;
import klarna.person.registry.domain.model.Child;
import klarna.person.registry.domain.model.Person;
import klarna.person.registry.domain.model.Spouse;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.NoSuchElementException;

import static klarna.person.registry.domain.TestValues.SSN;
import static org.junit.jupiter.api.Assertions.*;

class RegistryServiceImplTest {

    static RegistryService registryService;
    static PersonStorage personStorage;

    @BeforeAll
    static void setUp() {
        personStorage = new PersonStorageImpl();
        registryService = new RegistryServiceImpl(personStorage);
    }

    @AfterEach
    void tearDown() {
        personStorage.clear();
    }

    @Test
    void when_saveFullPerson_personIsSaved() {
        // Given
        Spouse spouse = new Spouse(TestValues.SPOUSE_NAME);

        Child firstChild = new Child(TestValues.FIRST_CHILD_NAME, 9);
        Child secondChild = new Child(TestValues.SECOND_CHILD_NAME, 11);
        List<Child> children = List.of(firstChild, secondChild);

        Person person = new Person(TestValues.PERSON_NAME, SSN, spouse, children);

        // When
        registryService.savePerson(person);

        // Then
        Person expectedPerson = registryService.getPerson(SSN);

        assertEquals(expectedPerson, person);
        assertEquals(TestValues.PERSON_NAME, person.name());
        assertEquals(TestValues.SPOUSE_NAME, person.spouse().name());
        assertEquals(2, person.children().size());
    }

    @Test
    void when_savePersonWithoutSpouse_personIsSaved() {
        // Given
        Child firstChild = new Child(TestValues.FIRST_CHILD_NAME, 9);
        Child secondChild = new Child(TestValues.SECOND_CHILD_NAME, 11);
        List<Child> children = List.of(firstChild, secondChild);

        Person person = new Person(TestValues.PERSON_NAME, SSN, null, children);

        // When
        registryService.savePerson(person);

        // Then
        Person expectedPerson = registryService.getPerson(SSN);

        assertEquals(expectedPerson, person);
        assertNull(person.spouse());
    }

    @Test
    void when_savePersonWithoutChildren_personIsSaved() {
        // Given
        Spouse spouse = new Spouse(TestValues.SPOUSE_NAME);

        Person person = new Person(TestValues.PERSON_NAME, SSN, spouse, List.of());

        // When
        registryService.savePerson(person);

        // Then
        Person expectedPerson = registryService.getPerson(SSN);

        assertEquals(expectedPerson, person);
        assertEquals(TestValues.PERSON_NAME, person.name());
        assertEquals(TestValues.SPOUSE_NAME, person.spouse().name());
        assertEquals(0, person.children().size());
    }

    @Test
    void when_getOldestChildOfManyChildren_oldestChildIsReturned() {
        // Given
        Spouse spouse = new Spouse(TestValues.SPOUSE_NAME);

        Child firstChild = new Child(TestValues.FIRST_CHILD_NAME, 9);
        Child secondChild = new Child(TestValues.SECOND_CHILD_NAME, 11);
        List<Child> children = List.of(firstChild, secondChild);

        Person person = new Person(TestValues.PERSON_NAME, SSN, spouse, children);

        // When
        registryService.savePerson(person);

        // Then
        OldestChildDto dto = registryService.getOldestChild(SSN);

        assertEquals(secondChild.name(), dto.OldestChildName());
        assertEquals(SSN, dto.parentSsn());
    }

    @Test
    void when_getOldestChildOfOneChild_oldestChildIsReturned() {
        // Given
        Spouse spouse = new Spouse(TestValues.SPOUSE_NAME);

        Child firstChild = new Child(TestValues.FIRST_CHILD_NAME, 9);
        List<Child> children = List.of(firstChild);

        Person person = new Person(TestValues.PERSON_NAME, SSN, spouse, children);

        // When
        registryService.savePerson(person);

        // Then
        OldestChildDto dto = registryService.getOldestChild(SSN);

        assertEquals(firstChild.name(), dto.OldestChildName());
        assertEquals(SSN, dto.parentSsn());
    }

    //@Test
    /*
    Assumptions:
    1. No negative tests due to time constraint, weird behaviours like saving null values (or null SSN, technically works
    2. No tests for children with same age
    with a HashMap..)
     */

    @Test
    void when_getPersonThatIsNotSaved_exceptionIsThrown() {
        // Given
        // Nothing!

        // When + Then
        assertThrows(NoSuchElementException.class, () -> {
                registryService.getPerson(SSN);
        });
    }

    @Test
    void when_getOldestChildFromPersonThatIsNotSaved_exceptionIsThrown() {
        // Given
        // Nothing!

        // When + Then
        assertThrows(NoSuchElementException.class, () -> {
            registryService.getOldestChild(SSN);
        });
    }

    @Test
    void when_getOldestChildOfNoChildren_exceptionIsThrown() {
        // Given
        Spouse spouse = new Spouse(TestValues.SPOUSE_NAME);

        Person person = new Person(TestValues.PERSON_NAME, SSN, spouse, List.of());

        registryService.savePerson(person);

        // When + Then
        assertThrows(IllegalArgumentException.class, () -> {
            registryService.getOldestChild(SSN);
        });
    }
}
