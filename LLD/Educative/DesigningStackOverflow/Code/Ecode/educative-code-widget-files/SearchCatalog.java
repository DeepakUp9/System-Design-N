import java.util.*;

public class SearchCatalog implements Search {
    private Map<String, List<Question>> questionsUsingTags = new HashMap<>();
    private Map<String, List<Question>> questionsUsingUsers = new HashMap<>();
    private Map<String, List<Question>> questionsUsingWords = new HashMap<>();

    @Override
    public List<Question> searchByTags(String tagName) {
        return questionsUsingTags.getOrDefault(tagName, new ArrayList<>());
    }

    @Override
    public List<Question> searchByUsers(String username) {
        return questionsUsingUsers.getOrDefault(username, new ArrayList<>());
    }

    @Override
    public List<Question> searchByWords(String word) {
        return questionsUsingWords.getOrDefault(word, new ArrayList<>());
    }

    // Optional: Methods to add questions to these maps as they are created
    public void indexQuestion(Question question) {
        for (Tag tag : question.getTags()) {
            questionsUsingTags.computeIfAbsent(tag.getName(), k -> new ArrayList<>()).add(question);
        }
        String username = question.getCreatedBy().getName();
        questionsUsingUsers.computeIfAbsent(username, k -> new ArrayList<>()).add(question);

        for (String word : question.getTitle().split("\\s+")) {
            questionsUsingWords.computeIfAbsent(word.toLowerCase(), k -> new ArrayList<>()).add(question);
        }
    }
}
