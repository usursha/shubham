package com.hpy.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.oauth.repository.TokenMetadataRepository;

import jakarta.transaction.Transactional;

@Service
public class TokenCleanupService {
	
	
	@Autowired
	private TokenMetadataRepository tokenMetadataRepository;
 
    @Transactional
    public void cleanupToken(String tokenId) {
        tokenMetadataRepository.deleteByTokenId(tokenId);
    }
}