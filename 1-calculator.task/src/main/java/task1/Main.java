package task1;

import task1.exceptions.BracketsException;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Main {

    private static final String PATTERN = "[0-9]*[.,][0-9]+|[0-9]+|[+]|[-]|[*]|[/]|[(]|[)]";
    private static final String OPEN_BRACKET_PATTERN = "[(]";
    private static final String CLOSE_BRACKET_PATTERN = "[)]";
    private static final String ARITHMETIC_PATTERN = "[+]|[-]|[*]|[/]";
    private static final String NUM_PATTERN = "[0-9]*[.,][0-9]+|[0-9]+";

    private static final String BRACKETS_ERROR_MESSAGE = "Syntax error: Incorrect number of brackets";
    private static final String ARITHMETIC_ERROR_MESSAGE = "Syntax error: Incorrect sequence of operators";

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.######");

    private static Pair<Double, ListIterator<Lexeme>> brackets(ListIterator<Lexeme> lexIterator) {
        ArrayList<Lexeme> localLexemes = new ArrayList<Lexeme>();
        boolean flagCloseBrackets = true;

        while (lexIterator.hasNext() && flagCloseBrackets) {
            Lexeme lex = lexIterator.next();

            // Brackets chek
            if (lex.getType() == LexemeType.OPEN_BRACKET) {
                Pair<Double, ListIterator<Lexeme>> out = brackets(lexIterator);
                lexIterator = out.second;
                localLexemes.add(new Lexeme(out.first.toString(), LexemeType.NUM));
            } else if (lex.getType() == LexemeType.CLOSE_BRACKET) {
                flagCloseBrackets = false;
            } else {
                localLexemes.add(lex);
            }
        }

        // Calculation of priority operations
        ArrayList<Lexeme> newLocalLexemes = new ArrayList<>();
        Double previousDouble = null;
        for (int i = 0; i < localLexemes.size(); i++) {
            Lexeme lex = localLexemes.get(i);
            if(lex.getType() == LexemeType.ARITHMETIC) {
                if (lex.getLex().equals("*") || lex.getLex().equals("/")) {
                    Double x = null;
                    Double y = null;
                    if (previousDouble == null) {
                        x = Double.valueOf(localLexemes.get(i - 1).getLex());
                        y = Double.valueOf(localLexemes.get(i + 1).getLex());
                    } else {
                        x = previousDouble;
                        y = Double.valueOf(localLexemes.get(i + 1).getLex());
                    }

                    if (lex.getLex().equals("*")) {
                        previousDouble = x * y;
                    } else {
                        previousDouble = x / y;
                    }
                } else {
                    if (previousDouble != null) {
                        newLocalLexemes.add(new Lexeme(previousDouble.toString(), LexemeType.NUM));
                        previousDouble = null;
                    } else {
                        newLocalLexemes.add(localLexemes.get(i - 1));
                    }
                    newLocalLexemes.add(lex);
                }
            }
        }
        if (previousDouble != null) {
            newLocalLexemes.add(new Lexeme(previousDouble.toString(), LexemeType.NUM));
        } else {
            newLocalLexemes.add(localLexemes.get(localLexemes.size() - 1));
        }

        // Calculation of non-priority operations
        previousDouble = Double.valueOf(newLocalLexemes.get(0).getLex());
        for (int i = 1; i < newLocalLexemes.size() - 1; i++) {
            Lexeme lex = newLocalLexemes.get(i);
            if (lex.getType() == LexemeType.ARITHMETIC) {
                if (lex.getLex().equals("+")) {
                    previousDouble = previousDouble + Double.parseDouble(newLocalLexemes.get(i + 1).getLex());
                } else {
                    previousDouble = previousDouble - Double.parseDouble(newLocalLexemes.get(i + 1).getLex());
                }
            }
        }

        return  new Pair<>(previousDouble, lexIterator);
    }

    private static double analyser(String expr) throws task1.exceptions.ArithmeticException, BracketsException {
        ArrayList<Lexeme> lexemes = new ArrayList<>();

        String[] matches = Pattern.compile(PATTERN)
                .matcher(expr)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

        Pattern openBracketPattern = Pattern.compile(OPEN_BRACKET_PATTERN);
        Pattern closeBracketPattern = Pattern.compile(CLOSE_BRACKET_PATTERN);
        Pattern arithmeticPattern = Pattern.compile(ARITHMETIC_PATTERN);
        Pattern numPattern = Pattern.compile(NUM_PATTERN);

        boolean flagPreviousMinus = false;
        boolean flagPreviousArithmetic = true;
        int bracketsCounter = 0;

        for (String lex : matches) {
            Lexeme lexeme = null;
            if (arithmeticPattern.matcher(lex).find()) {
                if (flagPreviousArithmetic) {
                    if (lex.equals("-")) {
                        flagPreviousMinus = true;
                    } else {
                        throw new task1.exceptions.ArithmeticException();
                    }
                } else {
                    flagPreviousArithmetic = true;
                    lexeme = new Lexeme(lex, LexemeType.ARITHMETIC);
                }
            } else {
                flagPreviousArithmetic = false;
            }
            if (numPattern.matcher(lex).find()) {
                flagPreviousArithmetic = false;
                if (flagPreviousMinus) {
                    flagPreviousMinus = false;
                    lex = "-" + lex;
                }
                lexeme = new Lexeme(lex, LexemeType.NUM);
            }
            if (openBracketPattern.matcher(lex).find()) {
                flagPreviousArithmetic = true;
                bracketsCounter++;
                lexeme = new Lexeme(lex, LexemeType.OPEN_BRACKET);
            }
            if (closeBracketPattern.matcher(lex).find()) {
                bracketsCounter--;
                lexeme = new Lexeme(lex, LexemeType.CLOSE_BRACKET);
            }

            if (lexeme != null) {
                lexemes.add(lexeme);
            }
        }

        if (bracketsCounter != 0) {
            throw new BracketsException();
        }

        ListIterator<Lexeme> lexIterator = lexemes.listIterator();
        return brackets(lexIterator).first;
    }

    public static void main(String[] args) {
        String expression;
        if (args.length < 2) {
            Scanner in = new Scanner(System.in);
            System.out.print("> ");
            expression = in.nextLine();
            try {
                System.out.println("< " + decimalFormat.format(analyser(expression)));
            } catch (task1.exceptions.ArithmeticException e) {
                System.err.println(ARITHMETIC_ERROR_MESSAGE);
            } catch (BracketsException e) {
                System.err.println(BRACKETS_ERROR_MESSAGE);
            }
        } else {
            String exprFilename = args[0];
            String outFilename = args[1];

            try {
                new File(outFilename).createNewFile();

                FileReader exprFile = new FileReader(exprFilename);
                FileWriter outFile = new FileWriter(outFilename);
                BufferedReader reader = new BufferedReader(exprFile);

                while (reader.ready()) {
                    expression = reader.readLine();
                    if (!(expression.equals("") || expression.equals("\n") || expression.startsWith("#"))) {
                        outFile.write(decimalFormat.format(analyser(expression)) + "\n");
                    }
                }

                outFile.flush();
            } catch (IOException e) {
                System.err.println("Something went wrong :/");
            } catch (task1.exceptions.ArithmeticException e) {
                System.err.println(ARITHMETIC_ERROR_MESSAGE);
            } catch (BracketsException e) {
                System.err.println(BRACKETS_ERROR_MESSAGE);
            }
        }
    }

}
