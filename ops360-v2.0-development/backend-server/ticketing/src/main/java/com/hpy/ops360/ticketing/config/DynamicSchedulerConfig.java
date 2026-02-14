//package com.hpy.ops360.ticketing.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//
//import com.hpy.ops360.ticketing.repository.SchedulerRepository;
//import com.hpy.ops360.ticketing.service.TicketSyncService;
//
//@Configuration
//@EnableScheduling
//public class DynamicSchedulerConfig implements SchedulingConfigurer {
//
//    @Autowired
//    private TicketSyncService ticketSyncService;
//
//    @Autowired
//    private SchedulerRepository schedulerRepository;
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.addCronTask(
//            () -> ticketSyncService.syncTickets(),
//            schedulerRepository.getSchedulerExpression().getSchedulerExpression()
//        );
//    }
//}

