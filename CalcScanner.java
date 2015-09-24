/**
 * csci 431 project 1:
 *
 * Created by petriccione on 9/12/15.
 */
import java.io.*;

public class CalcScanner implements Tokens {

    String fileName;
    FileReader reader;
    PushbackReader pbReader;
    int token;//need to make token the return code, & just return token in the end
    boolean checkEOF = false;

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
        int c2;
        int c3;
        int c4;
        int c5;

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
                    || c == '/') {

                return c;
            }

            if ((char) c == ':') {
                while (true) {
                    c2 = pbReader.read();
                    if (c2 != '=') {
                        return -1;
                    }
                    return ASSIGN;
                }
            }
            /* 
            here's the pseudoCode as Sheaffer explained it:
            
            c <-- read()
            .
            .
            .
            if(c is a letter) {
              lexeme <-- c
              c <-- read()
              while(c is a letter or digit) {
                lexeme += c
                c <-- read()
              }
              unread(c)
              if (lexeme.equals("read")) return READ
              if (lexeme.equals("write")) return WRITE
            else return ID 
            
            
             * **/
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

    public static void main(String[] args) throws IOException {
        CalcScanner myCalcScanner = new CalcScanner(args[0]);
        int lookahead = myCalcScanner.nextToken();

        while (lookahead != EOF) {
            System.out.println(lookahead);
            lookahead = myCalcScanner.nextToken();
        }
    }
}
