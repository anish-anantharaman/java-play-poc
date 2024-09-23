// UserController.java
package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;
import repository.UserRepositoryException;
import services.UserService;

import javax.inject.Inject;


public class UserController extends Controller {

    private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final UserService userService;

    @Inject
    public UserController(UserRepository userRepository, FormFactory formFactory, UserService userService) {
        this.userRepository = userRepository;
        this.formFactory = formFactory;
        this.userService = userService;
    }

    private ObjectNode buildSuccessResponse(String statusDescription, JsonNode responseData) {
        ObjectNode responseJson = Json.newObject();
        responseJson.put("statusCode", 200);
        responseJson.put("statusDescription", statusDescription);
        responseJson.put("errorDescription", "");
        responseJson.put("responseData", responseData);
        return responseJson;
    }

    private ObjectNode buildErrorResponse(String errorDescription) {
        ObjectNode responseJson = Json.newObject();
        responseJson.put("statusCode", 400);
        responseJson.put("statusDescription", "Bad Request");
        responseJson.put("errorDescription", errorDescription);
        responseJson.put("responseData", Json.newObject());
        return responseJson;
    }

    private ObjectNode buildNotFoundResponse() {
        ObjectNode responseJson = Json.newObject();
        responseJson.put("statusCode", 404);
        responseJson.put("statusDescription", "Not Found");
        responseJson.put("errorDescription", "User not found");
        responseJson.put("responseData", Json.newObject());
        return responseJson;
    }

    private ObjectNode buildInternalServerErrorResponse() {
        ObjectNode responseJson = Json.newObject();
        responseJson.put("statusCode", 500);
        responseJson.put("statusDescription", "Internal Server Error");
        responseJson.put("errorDescription", "User with the same ID already exists");
        responseJson.put("responseData", Json.newObject());
        return responseJson;
    }

    public Result getAllUsers() {
        try {
            return ok(Json.toJson(userRepository.getAllUsers()));
        } catch (UserRepositoryException e) {
            return internalServerError(buildErrorResponse("An error occurred while retrieving users: " + e.getMessage()));
        }
    }


    public Result createUser(Http.Request request) {
        try {
            Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
            User user = userForm.get();

            if (userRepository.getUserById(user.getId()) != null) {
                return internalServerError(buildInternalServerErrorResponse());
            }

            userService.validateUser(user);

            userRepository.addUser(user);

            ObjectNode responseData = Json.newObject();
            responseData.put("id", user.getId());
            responseData.put("name", user.getName());
            responseData.put("email", user.getEmail());

            return ok(buildSuccessResponse("User added successfully", responseData));
        } catch (IllegalArgumentException e) {
            return badRequest(buildErrorResponse(e.getMessage()));
        } catch (UserRepositoryException e) {
            return internalServerError(buildErrorResponse("An error occurred while creating the user: " + e.getMessage()));
        }
    }


    public Result deleteUser(int id) {
        try {
            User deletedUser = userRepository.getUserById(id);

            if (deletedUser != null) {
                userRepository.removeUser(id);

                ObjectNode responseData = Json.newObject();
                responseData.put("id", deletedUser.getId());
                responseData.put("name", deletedUser.getName());
                responseData.put("email", deletedUser.getEmail());

                return ok(buildSuccessResponse("User deleted successfully", responseData));
            } else {
                // Modify this line to throw a NotFound response
                throw new UserRepositoryException("User not found");
            }
        } catch (UserRepositoryException e) {
            // Check if the exception message is "User not found" and return a notFound response
            if ("User not found".equals(e.getMessage())) {
                return notFound(buildNotFoundResponse());
            } else {
                return internalServerError(buildErrorResponse("An error occurred while deleting the user: " + e.getMessage()));
            }
        }
    }

    public Result updateUser(Http.Request request) {
        try {
            Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
            User user = userForm.get();

            if (userRepository.getUserById(user.getId()) == null) {
                return notFound(buildNotFoundResponse());
            }

            userService.validateUser(user);

            userRepository.update(user);

            ObjectNode responseData = Json.newObject();
            responseData.put("id", user.getId());
            responseData.put("name", user.getName());
            responseData.put("email", user.getEmail());

            return ok(buildSuccessResponse("User record updated successfully", responseData));
        } catch (IllegalArgumentException e) {
            return badRequest(buildErrorResponse(e.getMessage()));
        } catch (UserRepositoryException e) {
            return internalServerError(buildErrorResponse("An error occurred while updating the user: " + e.getMessage()));
        }
    }


    public Result getUserById(int id) {
        try {
            User user = userRepository.getUserById(id);

            if (user != null) {
                return ok(Json.toJson(user)).as("application/json");
            } else {
                return notFound(buildNotFoundResponse()).as("application/json");
            }


        } catch (UserRepositoryException e) {
            return internalServerError(buildErrorResponse("An error occurred while retrieving the user: " + e.getMessage()));
        }
    }
}
