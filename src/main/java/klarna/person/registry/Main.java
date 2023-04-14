package klarna.person.registry;

import klarna.person.registry.adapter.storage.PersonStorageImpl;
import klarna.person.registry.domain.PersonStorage;
import klarna.person.registry.domain.RegistryService;
import klarna.person.registry.domain.RegistryServiceImpl;

public class Main {
    public static void main(String[] args) {
        PersonStorage personStorage = new PersonStorageImpl();
        RegistryService registryService = new RegistryServiceImpl(personStorage);
        System.out.println("The service started, but it won't do anything!");
    }
}
