package bg.sofia.uni.fmi.mjt.socialnetwork.comparators;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Comparator;

public class UserProfileByFriendsCountComparator implements Comparator<UserProfile> {
    @Override
    public int compare(UserProfile first, UserProfile second) {
        int friendsComparison = Integer.compare(second.getFriends().size(), first.getFriends().size());

        if (friendsComparison == 0) {
            return first.getUsername().compareTo(second.getUsername());
        }

        return friendsComparison;
    }
}
