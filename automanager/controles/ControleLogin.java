package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.LoginDto;
import com.autobots.automanager.jwt.ProvedorJwt;

@RestController
@RequestMapping("/login")
public class ControleLogin {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody LoginDto loginDto) {
        try {
            Authentication autenticacao = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            UserDetails usuario = (UserDetails) autenticacao.getPrincipal();
            
            String token = provedorJwt.proverJwt(usuario.getUsername());
            
            return ResponseEntity.ok().body("Token: " + token);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos.");
        }
    }

}
