package com.hpy.oauth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.oauth.entity.TokenMetadata;
import com.hpy.oauth.enums.TokenStatus;

@Repository
public interface TokenMetadataRepository extends JpaRepository<TokenMetadata, Long> {

    List<TokenMetadata> findByUserIdAndStatus(String userId, TokenStatus status);

    Optional<TokenMetadata> findByTokenId(String tokenId);
    
    Optional<TokenMetadata> findByRefreshToken(String tokenId);
    
    List<TokenMetadata> findAllByUserIdAndStatus(String userId, TokenStatus status);

	void deleteByTokenId(String tokenId);
}