package task1;

public class Lexeme {
    private String lex;
    private LexemeType type;

    public Lexeme(String lex, LexemeType type) {
        this.lex = lex;
        this.type = type;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public LexemeType getType() {
        return type;
    }

    public void setType(LexemeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "task1.Lexeme{" +
                "lex='" + lex + '\'' +
                ", type=" + type +
                '}';
    }
}
