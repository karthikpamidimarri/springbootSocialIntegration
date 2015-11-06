package karthik.service;

import java.util.Optional;

import karthik.auth.repository.AuthorityRepository;
import karthik.oauth.domain.User;
import karthik.oauth.domain.UserRepository;
import karthik.security.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);
    
    @Autowired
    private UserRepository userRepository;
    


    @Autowired
    private AuthorityRepository authorityRepository;
   
    
    
   
}
