import bg.sofia.uni.fmi.mjt.socialnetwork.SocialNetworkImpl;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            UserProfile alex = new DefaultUserProfile("alex");
            UserProfile bob = new DefaultUserProfile("bob");
            UserProfile slavi = new DefaultUserProfile("slavi");

            alex.addInterest(Interest.MUSIC);
            bob.addInterest(Interest.MUSIC);
            bob.addInterest(Interest.FOOD);
            bob.addInterest(Interest.BOOKS);
            slavi.addInterest(Interest.FOOD);
            slavi.addInterest(Interest.MUSIC);

            alex.addFriend(bob);
            bob.addFriend(slavi);

            SocialNetworkImpl socialNetwork = new SocialNetworkImpl();
            socialNetwork.registerUser(alex);
            socialNetwork.registerUser(bob);
            socialNetwork.registerUser(slavi);

            Post alexPost = socialNetwork.post(alex, "Hello, this is Alex's post!");

            Set<UserProfile> reachedUsers = socialNetwork.getReachedUsers(alexPost);
            System.out.println("Users that can see Alex's post:");
            for (UserProfile user : reachedUsers) {
                System.out.println(user.getUsername());
            }

        } catch (UserRegistrationException e) {
            System.err.println("Error registering user: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument: " + e.getMessage());
        }
    }
}
