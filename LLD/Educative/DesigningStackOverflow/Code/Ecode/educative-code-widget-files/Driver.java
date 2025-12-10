import java.util.*;

public class Driver {
    public static void main(String[] args) {
        System.out.println("=== Welcome to Stack Overflow Simulation ===\n");

        // ================================
        // ✅ System Initialization
        // ================================
        Guest guest = new Guest();
        guest.registerAccount();
        System.out.println("System initialized with a guest account.\n");

        // ================================
        // 👤 Scenario 1: User Registration and Tag Creation
        // ================================

        // Creating two users: Alice and Bob
        User user1 = new User();
        user1.setReputationPoints(10);
        user1.setName("alice123");
        user1.setStatus(UserStatus.ACTIVE);

        User user2 = new User();
        user2.setReputationPoints(7);
        user2.setName("bob_dev");
        user2.setStatus(UserStatus.ACTIVE);

        System.out.println("=== Scenario 1: Two users registered. ===");
        System.out.println("- User1: Alice (alice123), Reputation: 10");
        System.out.println("- User2: Bob (bob_dev), Reputation: 7\n");

        // Alice creates tags: java and oop
        Tag javaTag = new Tag();
        javaTag.setName("java");
        javaTag.setDescription("Java programming language");

        Tag oopTag = new Tag();
        oopTag.setName("oop");
        oopTag.setDescription("Object-oriented programming");

        List<Tag> tags = new ArrayList<>();
        tags.add(javaTag);
        tags.add(oopTag);

        System.out.println("Alice created two tags: [java, oop]\n");

        // ================================
        // ❓ Scenario 2: Question Creation, Bounty, and Publishing
        // ================================

        Question draftQuestion = user1.create(
            "What is the difference between interface and abstract class in Java?",
            "I am confused about when to use interface and when to use abstract class. Can someone explain?",
            tags
        );

        System.out.println("=== Scenario 2: Alice drafted a question: ===");
        System.out.println("- Title: What is the difference between interface and abstract class in Java?");
        System.out.println("- Tags: java, oop\n");

        user1.addBounty(100);
        System.out.println("Alice added a 100 reputation point bounty to her question.\n");

        user1.publishQuestion();
        System.out.println("Alice published the question to the platform.\n");

        // ================================
        // 💬 Scenario 3: Answering and Accepting the Answer
        // ================================

        Answer answer = new Answer();
        answer.setId(1);
        answer.setContent("Use interface when you want to define a contract. Use abstract class when you want base functionality.");
        answer.setPostedBy(user2);

        user2.addAnswer(draftQuestion, answer);
        System.out.println("=== Scenario 3: Bob submitted an answer to Alice's question. ===");
        System.out.println("- Answer ID: 1");
        System.out.println("- Content: Use interface when you want to define a contract. Use abstract class when you want base functionality.\n");

        user1.acceptAnswer(answer);
        System.out.println("Alice accepted Bob's answer as the correct one.\n");

        System.out.println("=== End of Simulation ===");
    }
}
