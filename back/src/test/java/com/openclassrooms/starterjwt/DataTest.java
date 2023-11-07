package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataTest {

    public static List<Teacher> createSampleTeachers() {
        List<Teacher> teachers = new ArrayList<>();

        Teacher teacher1 = new Teacher();
        teacher1.setLastName("Doe");
        teacher1.setFirstName("John");

        Teacher teacher2 = new Teacher();
        teacher2.setLastName("Smith");
        teacher2.setFirstName("Jane");

        Teacher teacher3 = new Teacher();
        teacher3.setLastName("Johnson");
        teacher3.setFirstName("Robert");

        Teacher teacher4 = new Teacher();
        teacher4.setLastName("Williams");
        teacher4.setFirstName("Emily");

        Teacher teacher5 = new Teacher();
        teacher5.setLastName("Brown");
        teacher5.setFirstName("Michael");

        teachers.add(teacher1);
        teachers.add(teacher2);
        teachers.add(teacher3);
        teachers.add(teacher4);
        teachers.add(teacher5);

        return teachers;
    }

    public static User user1() {
       return new User("user1@example.com", "clara", "clara", "motDePasse", false);
    }

    public static List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        users.add(new User("user1@example.com", "Doe", "John", "motDePasse1", false));
        users.add(new User("user2@example.com", "Smith", "Alice", "motDePasse2", false));
        users.add(new User("user3@example.com", "Johnson", "Bob", "motDePasse3", false));
        users.add(new User("user4@example.com", "Brown", "Emma", "motDePasse4", false));
        users.add(new User("user5@example.com", "Lee", "Oliver", "motDePasse5", false));

        return users;
    }

    public static Session session() {
        List<User> userList = generateUsers();
        Teacher teacher = createSampleTeachers().get(0);
        return Session.builder().name("Nom de la session").date(new Date())
                .description("Description de la session")
                .teacher(teacher).users(userList).build();
    }

    public static List<Session> generateSessions() {
        List<Session> sessions = new ArrayList<>();
        List<User> userList1 = generateUsers();
        List<User> userList2 = generateUsers();
        List<User> userList3 = generateUsers();
        Teacher teacher1 = createSampleTeachers().get(0);
        Teacher teacher2 = createSampleTeachers().get(1);
        Teacher teacher3 = createSampleTeachers().get(2);

        sessions.add(Session.builder().name("Session 1").date(new Date()).description("Description de la session 1")
                .teacher(teacher1).users(userList1).build());

        sessions.add(Session.builder().name("Session 2").date(new Date()).description("Description de la session 2")
                .teacher(teacher2).users(userList2).build());

        sessions.add(Session.builder().name("Session 3").date(new Date()).description("Description de la session 3")
                .teacher(teacher3).users(userList3).build());

        return sessions;
    }

}
