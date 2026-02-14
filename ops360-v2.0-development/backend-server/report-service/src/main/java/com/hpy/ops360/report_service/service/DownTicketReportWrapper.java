package com.hpy.ops360.report_service.service;

import com.hpy.ops360.report_service.dto.DownTicketReportDTO;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;


import lombok.Data;


@XmlRootElement(name = "DownTicketReport")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class DownTicketReportWrapper {

    @XmlElement(name = "Ticket")
    private List<DownTicketReportDTO> tickets;

}
