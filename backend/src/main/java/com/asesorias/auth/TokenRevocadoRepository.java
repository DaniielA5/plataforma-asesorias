package com.asesorias.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRevocadoRepository extends JpaRepository<TokenRevocado, Long> {

    boolean existsByJti(String jti);
}
