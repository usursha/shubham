//package com.hpy.hims_kafka.consumer;
//
//import java.util.Optional;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hpy.hims_kafka.dto.TicketDetailsOpen;
//import com.hpy.hims_kafka.entity.GetTicketDetailsClose;
//import com.hpy.hims_kafka.entity.GetTicketDetailsOpen;
//import com.hpy.hims_kafka.repo.GetTicketDetailsCloseRepository;
//import com.hpy.hims_kafka.repo.GetTicketDetailsOpenRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class KafkaConsumerService {
//
//    @Autowired
//    private GetTicketDetailsOpenRepository openRepository;
//
//    @Autowired
//    private GetTicketDetailsCloseRepository closeRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @KafkaListener(topics = "ticketdetails", groupId = "ticket-details-group")
//    public void consume(String message) {
//        try {
//            TicketDetailsOpen incoming = objectMapper.readValue(message, TicketDetailsOpen.class);
//            String srno = incoming.getSrno();
//
//            log.info("Received ticket update for srno: {}", srno);
//
//            Optional<GetTicketDetailsOpen> existingOpen = openRepository.findById(srno);
//
//            if ("Close".equalsIgnoreCase(incoming.getStatus())) {
//                if (existingOpen.isPresent()) {
//                    openRepository.deleteById(srno);
//                    log.info("Deleted srno {} from Open table", srno);
//                }
//                closeRepository.save(new GetTicketDetailsClose(incoming));
//                log.info("Inserted srno {} into Close table", srno);
//            } else {
//                if (existingOpen.isPresent()) {
//                    GetTicketDetailsOpen updated = existingOpen.get();
//                    BeanUtils.copyProperties(incoming, updated);
//                    openRepository.save(updated);
//                    log.info("Updated srno {} in Open table", srno);
//                } else {
//                    openRepository.save(new GetTicketDetailsOpen(incoming));
//                    log.info("Inserted srno {} into Open table", srno);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("Error processing ticket update: {}", e.getMessage(), e);
//        }
//    }
//}
