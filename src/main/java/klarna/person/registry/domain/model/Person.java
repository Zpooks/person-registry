package klarna.person.registry.domain.model;

import java.util.List;

public record Person(String name, String ssn, Spouse spouse, List<Child> children) {
}
