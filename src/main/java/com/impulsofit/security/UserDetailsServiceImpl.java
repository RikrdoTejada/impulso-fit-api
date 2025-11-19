package com.impulsofit.security;

import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado: " + email));

        Collection<GrantedAuthority> authorities = Collections.singletonList(
               new SimpleGrantedAuthority(user.getRole().getNombre().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getContrasena(),
                !user.getBloqueado(),
                true, true, true,
                authorities
        );
    }
}
