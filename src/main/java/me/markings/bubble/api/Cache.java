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

	/**
	 * Set whether a sound is played to players when a broadcast
	 * message is sent.
	 *
	 * @param enabled (true or false)
	 */
	void setBroadcastSoundStatus(boolean enabled);

	/**
	 * Returns whether MOTDs are enabled for all players on-join.
	 *
	 * @return true or false
	 */
	boolean isMotdStatus();

	/**
	 * Set whether MOTDs are enabled for all players.
	 *
	 * @param enabled (true or false)
	 */
	void setMotdStatus(boolean enabled);

	/**
	 * Return whether mentions are enabled for all players.
	 *
	 * @return true or false
	 */
	boolean isMentionsStatus();

	/**
	 * Set whether mentions are enabled for all players.
	 *
	 * @param enabled (true or false)
	 */
	void setMentionsStatus(boolean enabled);

	/**
	 * Returns whether a sound will be played when players
	 * are mentioned in the chat.
	 *
	 * @return true or false
	 */
	boolean isMentionSoundStatus();

	/**
	 * Set whether a sound will be played when players
	 * are mentioned in the chat.
	 *
	 * @param enabled (true or false)
	 */
	void setMentionSoundStatus(boolean enabled);

	/**
	 * Returns whether a toast message will appear when
	 * players are mentioned in the chat.
	 *
	 * @return true or false
	 */
	boolean isMentionToastStatus();

	/**
	 * Set whether a toast message will appear when
	 * players are mentioned in the chat.
	 *
	 * @param enabled (true or false)
	 */
	void setMentionToastStatus(boolean enabled);

}
