package com.shongon.mini_bank.repository;

import com.shongon.mini_bank.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
