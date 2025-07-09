package br.com.systechmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.systechmanager.entity.Users;
import br.com.systechmanager.exception.PasswordInvalidException;
import br.com.systechmanager.repository.UserRepository;

@Service
public class UserAuth implements UserDetailsService {

    @Autowired
    private PasswordEncoder passencoder;

    @Autowired
    private UserRepository repository;

    @Transactional
    public Users salvar(Users usuario){
    	System.out.println(usuario.getUsername());
        return repository.save(usuario);
    }

    public UserDetails autenticar( Users usuario ){
        UserDetails user = loadUserByUsername(usuario.getUsername());
        boolean senhasBatem = passencoder.matches( usuario.getPassword(), user.getPassword());
        if(!senhasBatem)
        	throw new PasswordInvalidException();
        else
        	return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Users usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha inválidos")); 
      /*  String[] roles = usuario.getUsername() ?
                new String[]{"ADMIN", "USER"} : new String[]{"USER"};*/
    	
    	String[] roles = new String[]{"ADMIN", "USER"};
        return User
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(roles)
                .build();
    }

}
