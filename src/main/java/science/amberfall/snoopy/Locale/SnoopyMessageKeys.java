package science.amberfall.snoopy.Locale;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

/**
 * Enum Name = MessageKey in lowercase prefixed with snoopy.
 */
@SuppressWarnings("WeakerAccess")
public enum SnoopyMessageKeys implements MessageKeyProvider {
	MESSAGE_PREFIX,
	RELOAD_MESSAGE_FORMAT,
	VERSION_MESSAGE_FORMAT,
	NOTIFY_MESSAGE_FORMAT,
	RELOAD_LANG_ERROR,;

	private final MessageKey key = MessageKey.of("snoopy." + this.name().toLowerCase());

	public MessageKey getMessageKey() {
		return key;
	}
}
