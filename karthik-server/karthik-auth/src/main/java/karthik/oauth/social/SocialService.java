package karthik.oauth.social;

/*import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.MailService;
*/
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import karthik.oauth.domain.Authority;
import karthik.oauth.domain.User;
import karthik.oauth.domain.UserRepository;
import karthik.repository.AuthorityRepository;

@Service
public class SocialService {
    private final Logger log = LoggerFactory.getLogger(SocialService.class);

    @Inject
    private UsersConnectionRepository usersConnectionRepository;

    
    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;
    @Inject
    private AuthorityRepository authorityRepository;

   /* @Inject
    private MailService mailService;
*/
    public void createSocialUser(Connection<?> connection, String langKey) {
        if (connection == null) {
            log.error("Cannot create social user because connection is null");
            throw new IllegalArgumentException("Connection cannot be null");
        }
        final UserProfile userProfile = connection.fetchUserProfile();
        final String providerId = connection.getKey().getProviderId();
        final User user = createUserIfNotExist(userProfile, langKey, providerId);
        createSocialConnection(user.getLogin(), connection);
       // mailService.sendSocialRegistrationValidationEmail(user, providerId);
    }

    private User createUserIfNotExist(UserProfile userProfile, String langKey, String providerId) {
        final String email = userProfile.getEmail();
        final String userName = userProfile.getUsername();
        if (StringUtils.isBlank(email) && StringUtils.isBlank(userName)) {
            log.error("Cannot create social user because email and login are null");
            throw new IllegalArgumentException("Email and login cannot be null");
        }
        if (StringUtils.isBlank(email) && userRepository.findOneByLogin(userName).isPresent()) {
            log.error("Cannot create social user because email is null and login already exist, login -> {}", userName);
            throw new IllegalArgumentException("Email cannot be null with an existing login");
        }
        final Optional<User> user = userRepository.findOneByEmail(email);
        if (user.isPresent()) {
            log.info("User already exist associate the connection to this account");
            return user.get();
        }

        final String login = getLoginDependingOnProviderId(userProfile, providerId);
        final String encryptedPassword = passwordEncoder.encode(RandomStringUtils.random(10));
        final Set<Authority> authorities = new HashSet<>(1);
        authorities.add(authorityRepository.findOne("ROLE_USER"));

        final User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userProfile.getFirstName());
        newUser.setLastName(userProfile.getLastName());
        newUser.setEmail(email);
        newUser.setActivated(true);
        newUser.setAuthorities(authorities);
        newUser.setLangKey(langKey);

        return userRepository.save(newUser);
    }

    /**
     * @param userProfile
     * @param providerId
     * @return login if provider manage a login like Twitter or Github otherwise email address.
     *         Because provider like Google or Facebook didn't provide login or login like "12099388847393"
     */
    private String getLoginDependingOnProviderId(UserProfile userProfile, String providerId) {
        switch (providerId) {
            case "twitter":
                return userProfile.getUsername();
            default:
                return userProfile.getEmail();
        }
    }

    private void createSocialConnection(String login, Connection<?> connection) {
        final ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
        connectionRepository.addConnection(connection);
    }
}
