package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class DefaultUserProfile implements UserProfile {
    private final String username;
    Collection<Interest> interests;
    Collection<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        interests = new HashSet<>();
        friends = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultUserProfile that = (DefaultUserProfile) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableCollection(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest can't be null!");
        }

        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest can't be null!");
        }

        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableCollection(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile can't be null!");
        }

        if (userProfile == this) {
            throw new IllegalArgumentException("User profile can't add themselves!");
        }

        boolean isAdded = friends.add(userProfile);

        if (isAdded) {
            userProfile.addFriend(this);
        }

        return isAdded;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile can't be null!");
        }

        boolean isRemoved = friends.remove(userProfile);

        if (isRemoved) {
            userProfile.unfriend(this);
        }

        return isRemoved;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile can't be null!");
        }

        return friends.contains(userProfile);
    }
}