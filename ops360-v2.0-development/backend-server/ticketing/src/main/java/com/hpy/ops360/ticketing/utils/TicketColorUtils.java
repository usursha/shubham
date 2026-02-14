package com.hpy.ops360.ticketing.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TicketColorUtils {

	public enum TicketStatus {
		OPEN, TIMEOUT, TRAVEL, UPDATE, open, timeout, travel, update, Open, Timeout, Travel, Update
	}

	@Value("${app.color.prefix}")
	private String hexPrefix;
	@Value("${app.ticket.color.timeout.border}")
	private String COLOR_BORDER_TIMEOUT;
	@Value("${app.ticket.color.timeout.fill}")
	private String COLOR_FILL_TIMEOUT;
	@Value("${app.ticket.color.update.border}")
	private String COLOR_BORDER_UPDATE;
	@Value("${app.ticket.color.update.fill}")
	private String COLOR_FILL_UPDATE;
	@Value("${app.ticket.color.open.border}")
	private String COLOR_BORDER_OPEN;
	@Value("${app.ticket.color.open.fill}")
	private String COLOR_FILL_OPEN;
	@Value("${app.ticket.color.travel.border}")
	private String COLOR_BORDER_TRAVEL;
	@Value("${app.ticket.color.travel.fill}")
	private String COLOR_FILL_TRAVEL;

	@Value("${app.ticket.color.default.border}")
	private String COLOR_BORDER_DEFAULT;
	@Value("${app.ticket.color.default.border}")
	private String COLOR_FILL_DEFAULT;

	public TicketColorDto getColor(String status) {
		return getColor(TicketStatus.valueOf(status));
	}

	private String addPrefix(String color) {
		return hexPrefix + color;
	}

	public TicketColorDto getColor(TicketStatus status) {

		switch (status) {
		case OPEN:
		case open:
		case Open: {

			return new TicketColorDto(addPrefix(COLOR_BORDER_OPEN), addPrefix(COLOR_FILL_OPEN));
		}
		case TIMEOUT:
		case timeout:
		case Timeout: {
			return new TicketColorDto(addPrefix(COLOR_BORDER_TIMEOUT), addPrefix(COLOR_FILL_TIMEOUT));
		}
		case TRAVEL:
		case travel:
		case Travel: {
			return new TicketColorDto(addPrefix(COLOR_BORDER_TRAVEL), addPrefix(COLOR_FILL_TRAVEL));
		}
		case UPDATE:
		case update:
		case Update: {
			return new TicketColorDto(addPrefix(COLOR_BORDER_UPDATE), addPrefix(COLOR_FILL_UPDATE));
		}
		default:
			return new TicketColorDto(addPrefix(COLOR_BORDER_DEFAULT), addPrefix(COLOR_FILL_DEFAULT));
		}

	}

	public TicketColorDto getColor(Integer isUpdated, Integer isTimedOut, Integer isTravelling) {
//		if (isUpdated.equals(1) && isTravelling.equals(1))
//			return getColor(TicketStatus.UPDATE);
//		if (isTimedOut.equals(1))
//			return getColor(TicketStatus.TIMEOUT);
//		if (isTravelling.equals(1))
//			return getColor(TicketStatus.TRAVEL);
//		return getColor(TicketStatus.OPEN);
		// Show travel color when both updated and traveling
		if (isUpdated.equals(1) && isTravelling.equals(1))
			return getColor(TicketStatus.TRAVEL);
		if (isUpdated.equals(1))
			return getColor(TicketStatus.UPDATE);
		if (isTimedOut.equals(1))
			return getColor(TicketStatus.TIMEOUT);
		if (isTravelling.equals(1))
			return getColor(TicketStatus.TRAVEL);

		return getColor(TicketStatus.OPEN);
	}
}
