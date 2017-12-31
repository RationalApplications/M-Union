package xyz.ratapp.munionagent.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import co.chatsdk.core.dao.User;

/**
 * Created by ben on 10/9/17.
 */

public class UserListItemConverter {


    public List<UserListItem> toUserItemList (List<User> users) {
        ArrayList<UserListItem> userItemList = new ArrayList<>();
        for(User u : users) {
            UserWrapper wrapper = new UserWrapper(u);
            userItemList.add(new UserWrapper(u));
        }
        return userItemList;
    }

    public static List<User> toUserList (List<UserListItem> items) {
        ArrayList<User> users = new ArrayList<>();
        for(UserListItem u : items) {
            if(u instanceof User) {
                users.add((User) u);
            }
        }
        return users;
    }

    public static User toUser (UserListItem item) {
        if(item instanceof User) {
            return (User) item;
        }
        return null;
    }

    public class UserWrapper implements UserListItem,
            co.chatsdk.core.interfaces.UserListItem {

        private User user;

        public UserWrapper(User user) {
            this.user = user;
        }

        @Override
        public String getName() {
            return user.getName();
        }

        @Override
        public String getStatus() {
            return user.getStatus();
        }

        @Override
        public String getAvailability() {
            return user.getAvailability();
        }

        @Override
        public String getAvatarURL() {
            return user.getAvatarURL();
        }

        @Override
        public String getEmail() {
            return user.getEmail();
        }
    }
}
