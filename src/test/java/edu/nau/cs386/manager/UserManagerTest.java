package edu.nau.cs386.manager;

import org.junit.jupiter.api.Test;
import edu.nau.cs386.model.User;
import java.util.UUID;
import edu.nau.cs386.manager.UserManager;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    @Test
    void createUser()
    {
        UserManager userManager = UserManager.getInstance();
        User user = new User("Name", "Email");
        User mock = mock(User.class);
        when(mock.getName()).thenReturn("Name");
        when(mock.getEmail()).thenReturn("Email");
        when(mock.getBio()).thenReturn("");
        user = userManager.createUser("Name", "Email");
        assertEquals(user.getName(), mock.getName());
        assertEquals(user.getEmail(), mock.getEmail());
        assertEquals(user.getBio(), mock.getBio());
    }

    @Test
    void updateBio()
    {
        String newBio = "This is the new bio.";
        UserManager userManager = UserManager.getInstance();
        User mock = mock(User.class);
        when(mock.getBio()).thenReturn("This is the new bio.");
        User testUser = userManager.createUser( "Name", "Email");
        testUser = userManager.updateBio(testUser.getUuid(), newBio);
        assertEquals(testUser.getBio(), mock.getBio());
    }
    @Test
    void getUserByEmail()
    {
        UserManager userManager = UserManager.getInstance();
        User user = userManager.createUser("Name", "Email");
        User mock = mock(User.class);
        when(mock.getName()).thenReturn("Name");
        when(mock.getEmail()).thenReturn("Email");
        when(mock.getBio()).thenReturn("");
        when(mock.getUuid()).thenReturn(user.getUuid());
        user = userManager.updateBio(user.getUuid(), "");
        User testUser = userManager.getUserByEmail("Email");
        assertEquals(testUser.getName(), mock.getName());
        assertEquals(testUser.getEmail(), mock.getEmail());
        assertEquals(testUser.getBio(), mock.getBio());
        assertEquals(testUser.getUuid(), mock.getUuid());
    }

    @Test
    void getUser()
    {
        UserManager userManager = UserManager.getInstance();
        User user = userManager.createUser("Name","Email");
        User mock = mock(User.class);
        when(mock.getName()).thenReturn("Name");
        when(mock.getEmail()).thenReturn("Email");
        when(mock.getBio()).thenReturn("");
        when(mock.getUuid()).thenReturn(user.getUuid());
        User testUser = userManager.getUser( user.getUuid() );
        assertEquals(testUser.getName(), mock.getName());
        assertEquals(testUser.getEmail(), mock.getEmail());
        assertEquals(testUser.getBio(), mock.getBio());
        assertEquals(testUser.getUuid(), mock.getUuid());
    }
}
