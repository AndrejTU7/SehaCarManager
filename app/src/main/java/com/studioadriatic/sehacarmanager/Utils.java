package com.studioadriatic.sehacarmanager;

import com.studioadriatic.sehacarmanager.models.Chats;
import com.studioadriatic.sehacarmanager.models.User;

/**
 * Created by kristijandraca@gmail.com
 */
public class Utils {

    public static User getOtherUser(Chats chat) {
        User currentUser = App.getCurrentUser();
        if (currentUser.getId() == chat.getUser_1().getId()) {
            return chat.getUser_2();
        }
        return chat.getUser_1();
    }

}
