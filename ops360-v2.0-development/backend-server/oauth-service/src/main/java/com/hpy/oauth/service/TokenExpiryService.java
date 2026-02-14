package com.hpy.oauth.service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.hpy.oauth.repository.TokenMetadataRepository;

import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableScheduling
public class TokenExpiryService {
	
	@Autowired
	private TokenCleanupService tokenCleanupService;

    private final TaskScheduler taskScheduler;
    private final TokenMetadataRepository tokenMetadataRepository;

    public TokenExpiryService(TaskScheduler taskScheduler, TokenMetadataRepository tokenMetadataRepository) {
        this.taskScheduler = taskScheduler;
        this.tokenMetadataRepository = tokenMetadataRepository;
    }

    private final ConcurrentHashMap<String, Runnable> scheduledTasks = new ConcurrentHashMap<>();
    
    @Transactional
    public void scheduleTokenCleanup(String tokenId, LocalDateTime expiresAt) {
        if (expiresAt.isBefore(LocalDateTime.now())) {
//            logger.warn("Cannot schedule cleanup for token {}: expiration time is in the past", tokenId);
            return;
        }

        long delay = Duration.between(LocalDateTime.now(), expiresAt).toMillis();
        Runnable cleanupTask = () -> {
            try {
                tokenCleanupService.cleanupToken(tokenId);
                scheduledTasks.remove(tokenId);
            } catch (Exception e) {
//                logger.error("Error during token cleanup for {}: {}", tokenId, e.getMessage(), e);
            }
        };

        // Schedule the task
        scheduledTasks.put(tokenId, cleanupTask);
        taskScheduler.schedule(cleanupTask, new Date(System.currentTimeMillis() + delay));

//        logger.info("Scheduled cleanup for token {} at {}", tokenId, expiresAt);
    }
}
