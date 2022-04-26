package ma.emsi.patientmvc.sec.services;

import ma.emsi.patientmvc.sec.entities.AppRole;
import ma.emsi.patientmvc.sec.entities.AppUser;

public interface SecurityService {
    AppUser saveNewUser(String username,String password,String rePassword);
    AppRole saveNewRole(String roleName,String desription );
    void addRoleToUser(String usename,String roleName);
    void removeRoleFromUser(String usename,String roleName);
    AppUser loadUserByUsername(String username);
}
