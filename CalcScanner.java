

/**
 * csci 431 project 1: CalcScanner.
 * The CalcScanner class uses the java PushbackReader class to scan through a text file and return the
 * codes for each lexeme (as specified by the Tokens Interface).
 *
 * Created by petriccione on 9/12/15.
 */
import java.io.*;

public class CalcScanner implements Tokens {

    String fileName;
    FileReader reader;
    PushbackReader pbReader;

    public CalcScanner(String fileName) throws IOException {
        this.fileName = fileName;

        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " does not exist. Quitting.");
            System.exit(0);
        }

        pbReader = new PushbackReader(reader);

    }

    public int nextToken() throws IOException {

        int c = 0;
        StringBuilder lexeme = new StringBuilder("");
        //int c2;
        while (true) {
            c = pbReader.read();
            if (c == -1) {

                return EOF;
            }

            while (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                c = pbReader.read();
            }

            if (c == '('
                    || c == ')'
                    || c == '+'
                    || c == '-'
                    || c == '*'
                    ) {

                return c;
            }

            if ((char) c == ':') {
                lexeme.append((char)c);
                c = pbReader.read();
                if (c != '=') {
                    return -1;
                }
                return ASSIGN;
            }
            if ((char) c == '$') {
                lexeme.append((char)c);
                c = pbReader.read();
                if (c != '$') {
                    return -1;
                }
                return DOUBLE_DOLLAR;
            }


            /**
             * if cur_char = '/'
             */
            if (c == '/') {
                String comment = "";
                lexeme.append((char) c);

                c = pbReader.read();
                if (c == ' ') return '/';
                if (c == '*') {
                    c = pbReader.read();
                    while ((char) c != '*') {
                        lexeme.append((char) c);
                        comment += lexeme;
                        c = pbReader.read();
                    }
                    c = pbReader.read();
                    if (c == '/') return COMMENT;
                    else {
                       // readComment();
                        return COMMENT;
                    }

                }

            }

            if (Character.isDigit(c)) {
                lexeme.append((char) c);
                c = pbReader.read();
                while(Character.isDigit(c)) {
                    lexeme.append((char)c);
                    c = pbReader.read();
                }
                pbReader.unread(c);
                //if (Character.isLetter(c))
                return NUMBER;
            }

            if (Character.isLetter(c) || Character.isDigit(c)) {
                lexeme.append((char) c);
                c = pbReader.read();
                while(Character.isLetter(c) || Character.isDigit(c)) {
                    lexeme.append((char)c);
                    c = pbReader.read();
                }
                pbReader.unread(c);
                if (lexeme.toString().equals("read")) return READ;
                if (lexeme.toString().equals("write")) return WRITE;
                return ID;
            }

            //if (lexeme.equals("write")) return WRITE;
            // System.out.println("Made it to point a");
            return -1;
            }

        }


    public int readIDs(StringBuilder lexeme, int c) throws IOException {
        lexeme.append((char) c);
        c = pbReader.read();
        while(Character.isLetter(c) || Character.isDigit(c)) {
            lexeme.append((char)c);
            c = pbReader.read();
        }
        pbReader.unread(c);
        if (lexeme.toString().equals("read")) return READ;
        if (lexeme.toString().equals("write")) return WRITE;
        return ID;
    }

/*
            public void readComment() {
                c = pbReader.read();
                String comment = "";
                while ((char) c != '*') {
                    lexeme.append((char) c);
                    comment += lexeme;
                    c = pbReader.read();
                }
            }    
            */

    public static void main(String[] args) throws IOException {
        CalcScanner myCalcScanner = new CalcScanner(args[0]);
        int lookahead = myCalcScanner.nextToken();

        while (lookahead != EOF) {
            System.out.println(lookahead);
            lookahead = myCalcScanner.nextToken();
        }
    }
}
