package com.stackclonell.stackclone.core.state;

import com.stackclonell.stackclone.core.model.PostStatus;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * LLD & Spring Integration: Factory to retrieve the correct State object.
 * This uses Spring's DI capabilities to collect all implementations of PostState.
 */
@Component
public class PostStateFactory {

    private final Map<PostStatus, PostState> stateMap;

    /**
     * Spring automatically collects all beans that implement PostState
     * and injects them into this Map, keyed by their class name.
     * We map the state bean to its corresponding PostStatus enum.
     */
    public PostStateFactory(java.util.List<PostState> states) {
        this.stateMap = states.stream()
                .collect(Collectors.toMap(
                        // Key Mapper: Transform "OpenState" to PostStatus.OPEN
                        state -> PostStatus.valueOf(state.getClass().getSimpleName().replace("State", "").toUpperCase()),
                        Function.identity()
                ));
        System.out.println("LLD: PostStateFactory initialized with states: " + this.stateMap.keySet());
    }

    /**
     * Public method to retrieve the state object based on the PostStatus enum.
     */
    public PostState getState(PostStatus status) {
        PostState state = stateMap.get(status);
        if (state == null) {
            throw new IllegalArgumentException("Unknown post status: " + status);
        }
        return state;
    }
}