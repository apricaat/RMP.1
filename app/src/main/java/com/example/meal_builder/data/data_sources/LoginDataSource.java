package com.example.meal_builder.data.data_sources;

import com.example.meal_builder.data.model.LoggedInUser;
import com.example.meal_builder.data.model.Result;
import com.example.meal_builder.data.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public static ArrayList<User> users = new ArrayList<User>(){
        {
            add(new User(0, "ad@meal.com", "123123", "moderator"));
            add(new User(1, "user@meal.com", "12341234", "user"));
        }
    };

    public Result<LoggedInUser> login(String username, String password) {

        try {

            User user = users.stream().filter(user1 -> Objects.equals(user1.email, username)).findFirst().orElse(null);

            LoggedInUser loggedUser;

            if (user == null || !Objects.equals(user.password, password)) {
                User newUser = new User(users.size(), username, password, "user");
                users.add(newUser);
                loggedUser = new LoggedInUser(newUser.id, newUser.email);
            } else {
                loggedUser =
                        new LoggedInUser(
                                user.id,
                                user.email);
            }

            return new Result.Success<>(loggedUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}