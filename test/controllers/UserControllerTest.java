package controllers;

import org.junit.Test;
import play.Application;
import play.api.libs.json.Json;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class UserControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testGetAllUsers() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/users");
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testGetUserByIdSuccess() {
        int userId = 1001;       // Test user with ID 1001 in  database
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/user/" + userId);
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testGetUserByIdFailureWithInvalidId() {
        int userId = 9000;                 //Giving an invalid user id
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/user/" + userId);
        Result result = route(app, request);
        assertEquals(404, result.status());
    }


    @Test
    public void testCreateUser() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users")
                .bodyJson(Json.parse("{\"id\": \"1000\",\"name\": \"test\", \"email\": \"test@example.com\"}"));
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testCreateUserFailureWithInvalidId() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users")
                .bodyJson(Json.parse("{\"id\": \"-1\",\"name\": \"test\", \"email\": \"test@example.com\"}"));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testCreateUserFailureWithInvalidName() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users")
                .bodyJson(Json.parse("{\"id\": \"2000\",\"name\": \"76876\", \"email\": \"test@example.com\"}"));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testCreateUserFailureWithInvalidEmail() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users")
                .bodyJson(Json.parse("{\"id\": \"2000\",\"name\": \"test\", \"email\": \"test\"}"));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testCreateUserFailureWithAllInvalidData() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/users")
                .bodyJson(Json.parse("{\"id\": \"-1\",\"name\": \"985\", \"email\": \"test\"}"));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testUpdateUserSuccess() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri("/update")
                .bodyJson(Json.parse("{\"id\": \"1000\", \"name\": \"updatedtest\", \"email\": \"updatedtest@example.com\"}"));
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testUpdateUserFailureWithNonExistentUserId() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri("/update")
                .bodyJson(Json.parse("{\"id\": \"2000\", \"name\": \"updatedtest\", \"email\": \"updatedtest@example.com\"}"));
        Result result = route(app, request);
        assertEquals(404, result.status());
    }

    @Test
    public void testUpdateUserFailureWithInvalidName() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri("/update")
                .bodyJson(Json.parse("{\"id\": \"1001\", \"name\": \"\", \"email\": \"updatedtest@example.com\"}"));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testUpdateUserFailureWithInvalidEmail() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri("/update")
                .bodyJson(Json.parse("{\"id\": \"1001\", \"name\": \"updatedtest\", \"email\": \"updatedtest\"}"));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testDeleteUserSuccess() {
        int userId = 1000;
        Http.RequestBuilder request = new Http.RequestBuilder().method(DELETE).uri("/delete/" + userId);
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testDeleteUserFailureWithNonExistentUserId() {
        int userId = 8000;
        Http.RequestBuilder request = new Http.RequestBuilder().method(DELETE).uri("/delete/" + userId);
        Result result = route(app, request);
        assertEquals(404, result.status());
    }
}
