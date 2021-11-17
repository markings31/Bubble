package me.markings.bubble.api;

/**
 * Bubble's API that provides online players'
 * cache data.
 * <p>
 * Setting player information using any of the
 * methods below will automatically save them in
 * the data.db file.
 */
public interface Cache {

	/**
	 * Returns whether the player has enabled or disabled
	 * receiving broadcast messages.
	 *
	 * @return true or false
	 */
	boolean isBroadcastStatus();

	/**
	 * Set the broadcast status of the player.
	 *
	 * @param enabled (true or false)
	 */
	void setBroadcastStatus(boolean enabled);

	/**
	 * Returns whether the player has enabled or disabled the sound
	 * sent when receiving a broadcast message.
	 *
	 * @return true or false
	 */
	boolean isBroadcastSoundStatus();

	// TODO: Finish documentation.
	void setBroadcastSoundStatus(boolean enabled);

	boolean isMotdStatus();

	void setMotdStatus(boolean enabled);

	boolean isMentionsStatus();

	void setMentionsStatus(boolean enabled);

	boolean isMentionSoundStatus();

	void setMentionSoundStatus(boolean enabled);

	boolean isMentionToastStatus();

	void setMentionToastStatus(boolean enabled);

}
