package ru.nsu.sports.complex.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nsu.sports.complex.backend.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserRepositoryTest {


    @Autowired
    private UserRepository repository;

    private User user1;

    // Это заодно и тестирование метода save()
    @BeforeEach
    void setup() {
        // Очистка таблицы перед каждым тестом
        repository.deleteAll();

        user1 = new User();
        user1.setId(1);
        user1.setName("Kolya");
        user1.setEmail("n.valikov@g.nsu.ru");
        user1.setPassword("qwerty");
        repository.save(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Pivo");
        user2.setEmail("gudvin0203@gmail.com");
        user2.setPassword("1234");
        repository.save(user2);
    }

    private void equalsUsers(User user1, User user2) {
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getUsername(), user2.getUsername());
    }

    @Test
    void testFindByName() {
        User user = repository.findByName(user1.getName());
        equalsUsers(user, user1);
    }

    @Test
    void testFindById() {
        User userByName = repository.findByName(user1.getName());
        assertNotNull(userByName);
        Integer id = userByName.getId();
        Optional<User> sectionById = repository.findById(id);
        assert (sectionById.isPresent());
        equalsUsers(userByName, sectionById.get());
    }

    @Test
    void testFindAll() {
        List<User> users = repository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testDelete() {
        List<User> users = repository.findAll();
        int count = users.size();
        User user = users.get(0);
        repository.delete(user);
        assertNull(repository.findByName(user.getName()));
        assertEquals(count - 1, repository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        int count = repository.findAll().size();
        assertNotEquals(0, count);
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
    }
}
