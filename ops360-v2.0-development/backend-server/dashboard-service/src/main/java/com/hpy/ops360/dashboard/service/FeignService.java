package com.hpy.ops360.dashboard.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import com.hpy.ops360.dashboard.feign.TicketFeignClient;

import java.util.List;

@Service
public class FeignService {

    private TicketFeignClient ticketfeignclient;
    
    private final String bearerToken;

    public FeignService(TicketFeignClient ticketfeignclient) {
		this.ticketfeignclient = ticketfeignclient;
		this.bearerToken="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJPa1JwdHp1VFA0LXdBdERlWlMzM1B0N3pFUG5VMF9MRV9HalZTU0hDQUI4In0.eyJleHAiOjE3MjAwODE2NTUsImlhdCI6MTcyMDA3ODY1NSwianRpIjoiZjM3ZmI3MzgtZmI5Mi00MmYzLThkZmQtYTM0Mzg2YmY4NTYxIiwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo4MDgwL3JlYWxtcy9IUFkiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMGU1NWNkNzItMmM2Ni00ZTE4LThhNzctNzViYzc3ZTJkMjU3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BzMzYwIiwic2Vzc2lvbl9zdGF0ZSI6ImM0OGY0Y2Q3LWNhZTktNGRlYi04ZGUyLWViYjVjNjhkODUwNCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJkZWZhdWx0LXJvbGVzLWhweSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImM0OGY0Y2Q3LWNhZTktNGRlYi04ZGUyLWViYjVjNjhkODUwNCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6InJpc2hhYmggamFpbiIsInByZWZlcnJlZF91c2VybmFtZSI6InJpc2hhYmguamFpbiIsImdpdmVuX25hbWUiOiJyaXNoYWJoIiwiZmFtaWx5X25hbWUiOiJqYWluIiwiZW1haWwiOiJyaXNoYWJoLmphaW5AaGl0YWNoaS1wYXltZW50cy5jb20ifQ.YC06zcSqjWde74c96rQpyfzrDYhr6i-Uz3n9KyZQpGOkiGflkereXiHSB7dvhV-mMUBb88sMXjAhS0w0_eYqGua-IEW4CCxYa_9V4THtUlDWw6jjw9vmWKYsakfHvRifjCLxd-d3Ui0_EO1vwXL-S6jp4wFKtzCyjvpmukuYDXhp6FcGgznnGPCt4GeFua9wewaPDEX4O9-Fzt2jYatjmelrxWFYtzWZ5wCpHQjKLN9C0Jf2dMrcMwRzx__YHLRZcw9FuiS-6tIdaHCfv9AwYCon5XdEVKG2dldJk0IJq48SVv-nrD7vkfy-fHYCNRkYZbgClfu7n4s0jHVZs0mbtw";
    }

	public List<?> fetchAllTickets() {
		
		HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        ResponseEntity<List<?>> response = ticketfeignclient.fetchAllTickets(headers);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch tickets");
        }
    }
}
