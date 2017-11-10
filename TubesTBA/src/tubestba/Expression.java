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
public abstract class Expression {
      public abstract void  appendSB(StringBuffer sb);   // printing - efficiency!


   public static class Ident extends Expression                        // Ident
    { public final String id;                                         // e.g. x
      public Ident(String id) { this.id = id; }
      public void appendSB(StringBuffer sb) { sb.append(id); }
    }

   public static class IntCon extends Expression                      // IntCon
    { public final int n;                                             // e.g. 3
      public IntCon(int n) { this.n = n; }
      public void appendSB(StringBuffer sb)
       { sb.append(String.valueOf(n)); }
    }

   public static class Unary extends Expression                        // Unary
    { public final int opr;  public final Expression e;              // e.g. -7
      public Unary(int opr, Expression e)  { this.e = e; this.opr = opr; }
      public void appendSB(StringBuffer sb)
       { sb.append( "(" + Lexical.Symbol[opr] + " " );
         e.appendSB(sb);  sb.append( ")" );
       }
    }//Unary

   public static class Binary extends Expression                      // Binary
    { public final int opr; public final Expression lft, rgt;       // e.g. x+3
      public Binary(int opr, Expression lft, Expression rgt)
       { this.opr = opr; this.lft = lft; this.rgt = rgt; }
      public void appendSB(StringBuffer sb)
       { sb.append( "(" );
         lft.appendSB(sb);  sb.append( " " + Lexical.Symbol[opr] + " " );
         rgt.appendSB(sb);  sb.append( ")" );
       }
    }//Binary


   /* Using a StringBuffer gives linear rather than quadratic complexity. */
   public String toString()
    { StringBuffer sb = new StringBuffer(); // efficiency!
      appendSB(sb);
      return sb.toString();
    }//toString

   public static void error(String msg)
    { System.out.println("\nError: " + msg);
      System.exit(1);
    }//error

 }

