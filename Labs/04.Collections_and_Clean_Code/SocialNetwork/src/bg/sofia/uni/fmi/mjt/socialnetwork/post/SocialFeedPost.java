package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

public class SocialFeedPost implements Post {
    private String id;
    private UserProfile author;
    private LocalDateTime publishedOn;
    private String content;

    Map<ReactionType, Set<UserProfile>> reactions;

    public SocialFeedPost(UserProfile author, String content) {
        id = UUID.randomUUID().toString();
        this.author = author;
        this.content = content;
        publishedOn = LocalDateTime.now();

        reactions = new EnumMap<>(ReactionType.class);
    }

    @Override
    public String getUniqueId() {
        return id;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null");
        }

        boolean isNewReaction = removeExistingReaction(userProfile);

        if (!reactions.containsKey(reactionType)) {
            reactions.put(reactionType, new HashSet<>());
        }

        Set<UserProfile> users = reactions.get(reactionType);
        users.add(userProfile);

        return isNewReaction;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        boolean isUserFound = false;

        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactions.entrySet()) {
            if (entry.getValue().remove(userProfile)) {
                isUserFound = true;

                if (entry.getValue().isEmpty()) {
                    reactions.remove(entry.getKey());
                }

                break;
            }
        }

        return isUserFound;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(reactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null");
        }

        if (reactions.containsKey(reactionType)) {
            return reactions.get(reactionType).size();
        } else {
            return 0;
        }
    }

    @Override
    public int totalReactionsCount() {
        int totalCount = 0;

        for (Set<UserProfile> users : reactions.values()) {
            totalCount += users.size();
        }

        return totalCount;
    }

    private boolean removeExistingReaction(UserProfile userProfile) {
        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reactions.entrySet()) {
            Set<UserProfile> users = entry.getValue();
            if (users.remove(userProfile)) {
                if (users.isEmpty()) {
                    reactions.remove(entry.getKey());
                }
                return false;
            }
        }
        return true;
    }
}