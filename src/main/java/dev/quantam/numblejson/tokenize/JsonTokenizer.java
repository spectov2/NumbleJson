package dev.quantam.numblejson.tokenize;

/**
 * The {@code JsonTokenizer} class provides methods to tokenize a JSON string.
 *
 * @author quantam
 * @version 1.0
 */
public class JsonTokenizer {
    private final String json;
    private int pos = 0;

    /**
     * Constructs a {@code JsonTokenizer} with the specified JSON string.
     *
     * @param json the JSON string to tokenize
     */
    public JsonTokenizer(String json) {
        this.json = json;
    }

    /**
     * Returns the next token from the JSON string.
     * A token can be a JSON string, object delimiter ({}, []), or a key-value separator (:,).
     *
     * @return the next token as a {@code String}
     */
    public String nextToken() {
        StringBuilder sb = new StringBuilder();
        char c = nextChar();
        if (c == '"') {
            while ((c = nextChar()) != '"') {
                if (c == '\\') {
                    sb.append(c);
                    c = nextChar();
                }
                sb.append(c);
            }
            sb.insert(0, '"').append('"');
        } else if (c == '{' || c == '}' || c == '[' || c == ']' || c == ':' || c == ',') {
            sb.append(c);
        } else {
            do {
                sb.append(c);
                c = json.charAt(pos);
                if (",]}:".indexOf(c) != -1) break;
                pos++;
            } while (pos < json.length());
        }
        return sb.toString();
    }

    /**
     * Returns the next token without advancing the position.
     *
     * @return the next token as a {@code String}
     */
    public String peek() {
        int oldPos = pos;
        String token = nextToken();
        pos = oldPos;
        return token;
    }

    /**
     * Returns the next non-whitespace character from the JSON string.
     *
     * @return the next non-whitespace character
     */
    private char nextChar() {
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) pos++;
        return json.charAt(pos++);
    }
}
