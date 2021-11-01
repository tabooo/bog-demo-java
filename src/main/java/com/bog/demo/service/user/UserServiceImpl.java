package com.bog.demo.service.user;

import com.bog.demo.domain.shortlinks.ShortLink;
import com.bog.demo.domain.user.User;
import com.bog.demo.domain.user.UserVerification;
import com.bog.demo.model.user.UserDto;
import com.bog.demo.repository.shortlinks.ShortLinkRepository;
import com.bog.demo.repository.user.UserRepository;
import com.bog.demo.repository.user.UserVerificationRepository;
import com.bog.demo.util.Descriptor;
import com.bog.demo.util.ShortURLGenerator;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class, readOnly = true)
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ShortLinkRepository shortLinkRepository;
    private UserVerificationRepository userVerificationRepository;
    @Autowired
    private JavaMailSender emailSender;

    @Transactional
    public Descriptor register(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
            return Descriptor.invalidDescriptor("EMAIL_IS_REQUIRED");
        }

        Optional<User> dbUser = userRepository.findByEmailAndState(userDto.getEmail(), 1);
        if (dbUser.isPresent()) {
            return Descriptor.invalidDescriptor("EMAIL_IS_USED");
        }

        User user = new User();
        user.setAccountNumber(userDto.getAccountNumber());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setState(1);
        user.setEnabled(0);
        user.setBalance(0.0);

        userRepository.save(user);

        ShortLink shortLink = new ShortLink();
        shortLink.setKey("");
        shortLink.setUrl("");
        shortLink.setState(1);
        shortLink.setCreateDate(new Date());
        shortLinkRepository.save(shortLink);

        String key = ShortURLGenerator.idToShortURL(shortLink.getId());
        shortLink.setKey(key);
        shortLink.setUrl("http://localhost:4200/#/confirm/" + key);

        shortLinkRepository.save(shortLink);

        UserVerification userVerification = new UserVerification();
        userVerification.setKey(key);
        userVerification.setState(1);
        userVerification.setCreateDate(new Date());

        Calendar currentTimeNow = Calendar.getInstance();
        currentTimeNow.add(Calendar.MINUTE, 10);
        Date expireDate = currentTimeNow.getTime();

        userVerification.setExpireDate(expireDate);
        userVerification.setUserId(user.getId());

        userVerificationRepository.save(userVerification);


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("info@pixl.ge");
        message.setTo(user.getEmail());
        message.setSubject("Verification");
        message.setText("http://localhost:4200/#/short/" + shortLink.getKey());
        emailSender.send(message);

        return Descriptor.validDescriptor();
    }

    @Transactional
    public Descriptor setPassword(String password, String key) {
        UserVerification userVerification = userVerificationRepository.findByKeyAndState(key, 1);
        Date now = new Date();
        if (userVerification == null || userVerification.getExpireDate().getTime() < now.getTime()) {
            return Descriptor.invalidDescriptor("EXPIRED");
        }
        User user = userRepository.findById(userVerification.getUserId()).get();
        user.setPassword(password);
        user.setEnabled(1);

        userRepository.save(user);

        userVerification.setState(2);
        userVerificationRepository.save(userVerification);

        return Descriptor.validDescriptor();
    }

    public UserVerification checkVerification(String key) {
        UserVerification userVerification = userVerificationRepository.findByKeyAndState(key, 1);
        return userVerification;
    }

    @Autowired
    public void setShortLinkRepository(ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserVerificationRepository(UserVerificationRepository userVerificationRepository) {
        this.userVerificationRepository = userVerificationRepository;
    }
}