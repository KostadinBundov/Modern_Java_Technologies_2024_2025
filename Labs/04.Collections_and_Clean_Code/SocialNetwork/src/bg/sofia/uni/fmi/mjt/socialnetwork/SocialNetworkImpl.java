package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.comparators.UserProfileByFriendsCountComparator;
import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    private Set<UserProfile> users;
    private Collection<Post> posts;

    public SocialNetworkImpl() {
        users = new HashSet<>();
        posts = new LinkedHashSet<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile can't be null!");
        }

        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User profile is already registered!");
        }

        users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile can't be null!");
        }

        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content is null or empty!");
        }

        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("User profile isn't registered!");
        }

        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);
        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableCollection(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post can't be null!");
        }

        Set<UserProfile> reachedUsers = new HashSet<>();
        Set<UserProfile> visited = new HashSet<>();
        Queue<UserProfile> stack = new LinkedList<>();

        stack.add(post.getAuthor());
        visited.add(post.getAuthor());

        while (!stack.isEmpty()) {
            UserProfile currentUser = stack.poll();

            for (UserProfile friend : currentUser.getFriends()) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    stack.add(friend);

                    if (!friend.equals(post.getAuthor()) && hasCommonInterest(friend, post.getAuthor())) {
                        reachedUsers.add(friend);
                    }
                }
            }
        }

        return reachedUsers;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
        throws UserRegistrationException {

        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("User profile can't be null!");
        }

        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException("User profile isn't registered!");
        }

        Set<UserProfile> mutualFriends = new HashSet<>();

        for (UserProfile user : userProfile1.getFriends()) {
            if (userProfile2.getFriends().contains(user)) {
                mutualFriends.add(user);
            }
        }

        return mutualFriends;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedProfiles = new TreeSet<>(new UserProfileByFriendsCountComparator());
        sortedProfiles.addAll(users);
        return sortedProfiles;
    }

    private boolean hasCommonInterest(UserProfile first, UserProfile second) {
        for (Interest interest : first.getInterests()) {
            if (second.getInterests().contains(interest)) {
                return true;
            }
        }

        return false;
    }
}