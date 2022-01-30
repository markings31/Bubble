package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentLoader {

	/**
	 * Credit to Tvhee
	 */

	@Getter
	private static final CommentLoader settingsInstance = new CommentLoader();

	private final Map<String, List<String>> comments = new HashMap<>();

	public void setHeader(final List<String> header) {
		setComments("", header);
	}

	public List<String> getHeader() {
		return getComments("");
	}

	public List<String> getComments(final String key) {
		return comments.get(key) == null ? new ArrayList<>() : comments.get(key);
	}

	public void setComments(final String key, final List<String> comments) {
		if (comments == null) {
			this.comments.remove(key);
			return;
		}

		final List<String> checked = new ArrayList<>();

		for (final String comment : comments)
			if (!comment.startsWith("#") && !comment.isEmpty())
				checked.add("#" + comment);
			else
				checked.add(comment);

		this.comments.put(key, checked);
	}

	public void load(final File configFile) {
		comments.clear();

		try {
			final FileReader fileReader = new FileReader(configFile);
			final BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<String> commentsForKeys = new ArrayList<>();
			final Parser parser = new Parser();

			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				final String parsed = parser.parse(line);

				if (parser.isComment()) commentsForKeys.add(parsed);
				else if (!commentsForKeys.isEmpty()) {
					comments.put(parsed, commentsForKeys);
					commentsForKeys = new ArrayList<>();
				}
			}

			fileReader.close();
		} catch (final IOException e) {
			throw new IllegalArgumentException("load", e);
		}
	}

	public void apply(final File configFile) {
		try {
			final FileReader fileReader = new FileReader(configFile);
			final BufferedReader bufferedReader = new BufferedReader(fileReader);
			final StringBuilder newFile = new StringBuilder();
			final Parser parser = new Parser();

			final List<String> header = this.comments.get("");

			/*if (header != null)
				for (final String comment : header)
					newFile.append(parser.withSpaces(comment)).append("\n");*/

			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				final String parsed = parser.parse(line).trim();

				if (comments.containsKey(parsed)) {
					final List<String> comments = this.comments.get(parsed);

					if (header != null)
						header.forEach(comments::remove);

					for (final String comment : comments)
						newFile.append(parser.withSpaces(comment)).append("\n");
				}

				newFile.append(line).append("\n");
			}

			fileReader.close();
			final String newFileContent = newFile.toString();

			final FileWriter fileWriter = new FileWriter(configFile, false);
			fileWriter.write(newFileContent);
			fileWriter.close();
		} catch (final IOException e) {
			throw new IllegalArgumentException("apply", e);
		}
	}

	private static final class Parser {
		private final YamlKey yamlKey = new YamlKey();
		private int spaces;
		private boolean comment;

		public String parse(final String line) {
			spaces = 0;
			comment = false;

			final String withoutSpaces = line.trim();

			if (withoutSpaces.startsWith("#") || withoutSpaces.isEmpty()) {
				this.comment = true;
				return withoutSpaces;
			}

			final String key = line.split(":")[0].replace(" ", "");

			for (int i = 0; i < line.length(); i++)
				if (line.charAt(i) == ' ')
					spaces += 1;
				else
					break;

			return yamlKey.append(key, spaces / 2);
		}

		public boolean isComment() {
			return comment;
		}

		public String withSpaces(final String line) {
			return new String(new char[Math.max(0, spaces)]).replace("\0", " ") + line;
		}
	}

	private static final class YamlKey {
		private String key = "";

		public String append(final String subKey, final int newLength) {
			final String[] subkeys = this.key.split("\\.");

			if (newLength != 0 && subkeys[0] != null) {
				final StringBuilder keyBuilder = new StringBuilder(subkeys[0]);

				for (int i = 1; i < newLength; i++)
					keyBuilder.append(".").append(subkeys[i]);

				this.key = keyBuilder.toString();
			} else this.key = "";

			if (subKey.startsWith(".") || this.key.equals(""))
				this.key = this.key + subKey;
			else
				this.key = this.key + "." + subKey;

			if (this.key.startsWith("\\."))
				this.key = this.key.replaceFirst("\\.", "");

			return this.key;
		}
	}
}
