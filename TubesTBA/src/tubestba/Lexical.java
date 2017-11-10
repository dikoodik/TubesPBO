/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tubestba;

/**
 *
 * @author user
 */
import java.lang.*;
import java.io.*;
public class Lexical {
    InputStream inp;

   int sy = -1;                                      // lexical state variables
   char ch = ' ';  byte[] buffer = new byte[1];  boolean eof = false;
   String theWord = "<?>";  int theInt = 666;

   public static final int  // symbol codes...
   word        = 0,
   numeral     = 1,
   open        = 2,   // (
   close       = 3,   // )
   plus        = 4,   // +
   minus       = 5,   // -
   times       = 6,   // *
   over        = 7,   // /
   eofSy       = 8;

   public static final String[] Symbol = new String[]
    { "<word>", "<numeral>",
      "(", ")",
      "+", "-", "*", "/",
      "<eof>"
    };//Symbol


   public Lexical(InputStream inp)                               // constructor
    { this.inp = inp; insymbol(); }//constructor


   public int     sy()      { return sy; }           // Define
   public boolean eoi()     { return sy == eofSy; }  // what a
   public String  theWord() { return theWord; }      // Lexical
   public int     theInt()  { return theInt; }       // object is


   public void insymbol()                                           // insymbol
   // get the next symbol from the input stream
    { if(sy == eofSy) return;
      while( ch == ' ' ) getch(); // skip white space

      if( eof ) sy = eofSy;

      else if( Character.isLetter(ch) )               // words
       { StringBuffer w = new StringBuffer();
         while( Character.isLetterOrDigit(ch) )
          { w.append(ch); getch(); }
         theWord = w.toString();  sy = word;
       }

      else if( Character.isDigit(ch) )                // numbers
       { theInt = 0;
         while( Character.isDigit(ch) )
          { theInt = theInt*10 + ((int)ch) - ((int)'0'); getch(); }
         sy = numeral;
       }

      else                                            // special symbols
       { int ch2 = ch; getch();
         switch( ch2 )
          { case '+': sy = plus;  break;
            case '-': sy = minus; break;
            case '*': sy = times; break;
            case '/': sy = over;  break;
            case '(': sy = open;  break;
            case ')': sy = close; break;
            default: error("bad symbol");
          }
       }
    }//insymbol


   void getch()                                                        // getch
   // NB. changes variable ch as a side-effect.
    { ch = '.';
      if( sy == eofSy ) return;
      try { int n = 0;
            if( inp.available() > 0 ) n = inp.read(buffer);
            if( n <= 0 ) eof = true; else ch = (char)buffer[0];
          }
      catch(Exception e){ }
      if(ch == '\n' || ch == '\t') ch = ' ';
    }//getch


   void skipRest()
    { if( ! eof ) System.out.print("skipping to end of input...");
      int n = 0;
      while( ! eof )
      { if( n%80 == 0 ) System.out.println(); // break line
        System.out.print(ch);  n++;  getch();
      }
      System.out.println();
    }//skipRest


   public void error(String msg)                                       // error
    { System.out.println("\nError: " + msg +
                         " sy=" + sy + " ch=" + ch +
                         " theWord=" + theWord + " theInt=" + theInt);
      skipRest();
      System.exit(1);
    }//error


   // the following main() allows Lexical to be tested in isolation
   public static void  main(String[] argv)
    { System.out.println("--- Testing Lexical, L.Allison, CSSE, "
                       + "Monash Uni, .au ---");
      for(int i=0; i < argv.length; i++) // command line params if any
        System.out.print("argv[" + i + "]=" + argv[i] + "\n");
      Lexical lex = new Lexical(System.in);
      while( ! lex.eoi() )
       { int sy = lex.sy();
         System.out.print(sy + ": ");
         if(sy == Lexical.word)    System.out.print(lex.theWord());
         if(sy == Lexical.numeral) System.out.print(lex.theInt());
         System.out.println(",");
         lex.insymbol();
       }
      System.out.println("--- end ---");
    }//main

 
}
