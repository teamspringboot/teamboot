package com.advert.message;

import java.io.Serializable;

/**
 * this is a tag interface for those object to transport through message channel.
 * @author sunf
 *
 */
public interface Messaging extends Serializable {
	public String getMessageTypeId();
}
