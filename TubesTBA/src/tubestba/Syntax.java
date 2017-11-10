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
public class Syntax {
     private final Lexical lex;

   private final long                                     // useful Symbol Sets
   unOprs = (1L << Lexical.minus),

   binOprs= (1L << Lexical.plus) | (1L << Lexical.minus)
          | (1L << Lexical.times)| (1L << Lexical.over),

   startsExp = unOprs
             | (1L << Lexical.word) | (1L << Lexical.numeral)
             | (1L << Lexical.open);

   int[] oprPriority = new int[Lexical.eofSy];


   void init()
    { for(int i = 0; i < oprPriority.length; i++) oprPriority[i] = 0;
      oprPriority[Lexical.plus]  = 1;  oprPriority[Lexical.minus] = 1;
      oprPriority[Lexical.times] = 2;  oprPriority[Lexical.over]  = 2;
    }//init


   public Syntax(Lexical lex)                                     //constructor
    { this.lex = lex; init(); }


   private boolean syIs(int sym)  // test and skip symbol
    { if( lex.sy() == sym ) { lex.insymbol(); return true; }
      return false;
    }//syIs

   private void check(int sym)    // check and skip a particular symbol
    { if( lex.sy() == sym ) lex.insymbol();
      else error( Lexical.Symbol[sym] + " Expected" );
    }//check


   public Expression exp()                                             // exp()
    { Expression e = exp(1); check(Lexical.eofSy); return e; }


   private Expression exp(int priority)                             // exp(...)
    { Expression e = null;
      if( priority < 3 )
       { e = exp(priority+1);
         int sym = lex.sy();

         while( member(sym, binOprs) && oprPriority[sym] == priority )
          { lex.insymbol();                                       // e.g. 1+2+3
            e = new Expression.Binary(sym, e, exp(priority+1));
            sym = lex.sy();
          }
       }

      else if( member(lex.sy(), unOprs) )                  // unary op, e.g. -3
       { int sym = lex.sy(); lex.insymbol();
         e = new Expression.Unary(sym, exp(priority));
       }

      else                                                           // operand
       { switch( lex.sy() )
          { case Lexical.word:
               e = new Expression.Ident(lex.theWord); lex.insymbol(); break;
            case Lexical.numeral:
               e = new Expression.IntCon(lex.theInt); lex.insymbol(); break;
            case Lexical.open:                                      // e.g. (e)
               lex.insymbol();
               e = exp(1); check(Lexical.close);
               break;
            default:  error("bad operand");
          }//switch
       }//if

      return e;
    }//exp(...)


   boolean member(int n, long s)            // ? is n a member of the "set" s ?
    { return ((1L << n) & s) != 0; }

   void error(String msg) { lex.error("Syntax: " + msg); }             // error


   // the following allows Syntax to be tested on its own
   public static void main(String[] argv)
    { System.out.println("--- Testing Syntax, L.Allison, CSSE, "
                       + "Monash Uni, .au ---");
      Syntax syn = new Syntax( new Lexical(System.in) );
      Expression e = syn.exp();
      System.out.println( e.toString() );
      System.out.println("--- done ---");
    }//main



}
