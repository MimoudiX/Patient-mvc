package ma.emsi.patientmvc.sec.services;

import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;
import ma.emsi.patientmvc.sec.entities.AppRole;
import ma.emsi.patientmvc.sec.entities.AppUser;
import ma.emsi.patientmvc.sec.repositories.AppRoleRepository;
import ma.emsi.patientmvc.sec.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class SecurityServiceImpl implements SecurityService {
   private AppUserRepository appUserRepository;
   private AppRoleRepository  appRoleRepository;
   private PasswordEncoder  passwordEncoder;

    @Override
    public AppUser saveNewUser(String username, String password, String rePassword) {
        if(!password.equals(rePassword)) throw  new RuntimeException("the passwords didn't match");
        String hashedPwd=passwordEncoder.encode(password);
        AppUser appUser=new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(hashedPwd);
        appUser.setActive(true);
        appUser.setUserId(UUID.randomUUID().toString());//UUID permet de generer un strin unique
       AppUser savedAppUser= appUserRepository.save(appUser);
        return savedAppUser;
    }

    @Override
    public AppRole saveNewRole(String roleName, String desription) {
        AppRole appRole= appRoleRepository.findByRoleName(roleName);
        if(appRole!=null) new RuntimeException("role"+roleName+"already exists");
        appRole=new AppRole();
        appRole.setRoleName(roleName);
        appRole.setDescription(desription);
         AppRole savedRole = appRoleRepository.save(appRole);
        return savedRole;
    }

    @Override

    public void addRoleToUser(String usename, String roleName) {
        AppUser appUser=appUserRepository.findByUsername(usename);
        if(appUser==null) new RuntimeException("User not found");
        AppRole appRole=appRoleRepository.findByRoleName(roleName);
        if(appRole==null) new RuntimeException("Role not found");
        appUser.getAppRoles().add(appRole);

    }

    @Override
    public void removeRoleFromUser(String usename, String roleName) {
        AppUser appUser=appUserRepository.findByUsername(usename);
        if(appUser==null) new RuntimeException("User not found");
        AppRole appRole=appRoleRepository.findByRoleName(roleName);
        if(appRole==null) new RuntimeException("Role not found");
        appUser.getAppRoles().remove(appRole);

    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);

    }
}
