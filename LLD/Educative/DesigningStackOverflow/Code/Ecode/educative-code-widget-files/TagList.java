import java.util.*;

public class TagList {
    private HashMap<Tag, Integer> tagsCount = new HashMap<>();

    public void incrementTagCount(Tag tag) {
        tagsCount.put(tag, tagsCount.getOrDefault(tag, 0) + 1);
    }

    public void decrementTagCount(Tag tag) {
        tagsCount.put(tag, tagsCount.getOrDefault(tag, 1) - 1);
        if (tagsCount.get(tag) <= 0) tagsCount.remove(tag);
    }
}
