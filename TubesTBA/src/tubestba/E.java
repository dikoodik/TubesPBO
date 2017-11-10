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
public class E {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("--- Simple Expression Parser, L.Allison, CSSE, "
                       + "Monash Uni, .au, 9/2001 ---");

      Syntax syn = new Syntax( new Lexical(System.in) );
      Expression e = syn.exp();                          // parse an Expression
      System.out.println( e.toString() );

      System.out.println("--- done ---");
    }
    
}
