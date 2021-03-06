package config;

import repositories.UserRepository;

public interface ValidatableUniqueEmail<T> {
    T validateUniqueEmail(UserRepository userRepository, Long currentUserId);
}
