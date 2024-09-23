// UserService.java
package services;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public void validateUser(User user) {
        List<String> validationErrors = new ArrayList<>();

        validateId(user.getId(), validationErrors);
        validateName(user.getName(), validationErrors);
        validateEmail(user.getEmail(), validationErrors);

        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("Invalid user data: " + String.join(", ", validationErrors));
        }
    }

    private void validateId(Integer id, List<String> validationErrors) {
        if (id == null || id <= 0) {
            validationErrors.add("ID must be a positive integer");
        }
    }

    private void validateName(String name, List<String> validationErrors) {
        if (name == null || !name.matches("[A-Za-z ]+")) {
            validationErrors.add("Invalid characters in name");
        }
    }

    private void validateEmail(String email, List<String> validationErrors) {
        if (email == null || !email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
            validationErrors.add("Invalid email address");
        }
    }
}
