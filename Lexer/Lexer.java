/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.util.Vector;
import java.io.*;

public class Lexer {
 
  private String text;
  private Vector<Token> tokens; 
  private static final String[] KEYWORD = {"if", "else", "while", "switch", 
    "case", "return", "int", "float", "void", "char", "string", "boolean", 
    "true", "false", "print"};
  //Constants; YOU WILL NEED TO DEFINE MORE CONSTANTS
  private static final int ZERO       =  0;
  private static final int ONE        =  1;
  private static final int B          =  2;
  private static final int digit      =  3;
  private static final int digits     =  4;
   private static final int alphabets =  5;
   private static final int x         =  6;
   private static final int hexa      =  7;
   private static final int inverted  =  8;
   private static final int back      =  9;
   private static final int rtn       = 10;
   private static final int doublinverted = 11;
   private static final int dot       = 12;
   private static final int e         = 13;
   private static final int op        = 14;
      
  private static final int other     =  15;
  
  private static final int DELIMITER =  16;
  private static final int ERROR     =  16;
  private static final int STOP      = -2;
 
  // states table; THIS IS THE TABLE FOR BINARY NUMBERS; YOU SHOLD COMPLETE IT
  private static final int[][] stateTable = { 
              {1, 5, 8, 5,5,8,8,8,9,ERROR,8,13,14,8,ERROR,ERROR, STOP}, 
              {3, 3, 2, 3,ERROR,ERROR,6,ERROR,ERROR,ERROR,ERROR,ERROR,15,18,ERROR,ERROR, STOP}, 
              {4, 4,ERROR, ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP}, 
              {3,3,ERROR,3,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR, STOP}, 
              {4, 4, ERROR, ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR, STOP}, 
              {5,5,ERROR,5,5,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,15,18,ERROR,ERROR,STOP},
              {7,7,7,7,7,ERROR,ERROR,7,ERROR,ERROR,ERROR,ERROR,ERROR,7,ERROR,ERROR,STOP},
              {7,7,7,7,7,ERROR,ERROR,7,ERROR,ERROR,ERROR,ERROR,ERROR,7,ERROR,ERROR,STOP},
              {8,8,8,8,8,8,8,8,ERROR,ERROR,8,ERROR,ERROR,8,ERROR,ERROR,STOP},
              {10,10,10,10,10,10,10,10,ERROR,12,10,10,10,10,10,10,STOP},
              {ERROR,ERROR,ERROR,ERROR, ERROR ,ERROR,ERROR,ERROR,11,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
              {ERROR , ERROR,ERROR,ERROR, ERROR ,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
              {ERROR,ERROR,10,ERROR, ERROR ,ERROR,ERROR,ERROR,10,10,10,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
              {13,13,13,13,13,13,13,13,13,0,13,17,13,13,13,13,STOP},
              {15 , 15,ERROR,15, 15 ,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,18,ERROR,ERROR,STOP},
              {15,15,ERROR,15,15,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,18,ERROR,ERROR,STOP},
              {16 , 16,ERROR,16,16 ,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,18,ERROR,ERROR,STOP},
              {ERROR , ERROR,ERROR,ERROR, ERROR ,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
              {19,19,ERROR,19,19,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,20,ERROR,STOP},
              {19,19,ERROR,19,19,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
              {19,19,ERROR,19,19,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
              {ERROR , ERROR,ERROR,ERROR, ERROR ,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP}
                
      
          
  };
  
  //constructor
  public Lexer(String text) {
    this.text = text;
  }

  //run
  public void run () {
    tokens = new Vector<Token>();
    String line;
    int counterOfLines= 1;
    // split lines
    do {
      int eolAt = text.indexOf(System.lineSeparator());
      if (eolAt >= 0) {
        line = text.substring(0,eolAt); 
        if (text.length()>0) text = text.substring(eolAt+1);  
      } else {
        line = text;
        text = "";
      }
      splitLine (counterOfLines, line);
      counterOfLines++;
    } while ( !text.equals("") );   
  }
  
  //slit line
  private void splitLine(int row, String line) {
    int state = 0;
    int index = 0;
    char currentChar;
    String string="";
    if (line.equals("")) return; 
    //DFA working
    int go; 
    do {   
      currentChar = line.charAt(index);
      go = calculateNextState(state, currentChar);  
      if( go != STOP ) {
        string = string + currentChar;   
        state = go;
      }
      index++;        
    } while (index < line.length() && go != STOP);
    //review final state
    if (state == 3) {
      tokens.add(new Token(string, "OCTAL", row));
    }
     else if(state==4)
    {  tokens.add(new Token(string, "BINARY", row));
    } 
     else if(state==5)
    {  tokens.add(new Token(string, "INTEGER", row));
    } 
     
      
    
    else if(state==7)
    {  tokens.add(new Token(string, "HEXADECIMAL", row));
    } 
    else if(state==8)
    { int f=0;
       for(int x=0;x<KEYWORD.length;x++)
       {
       if(string.contentEquals(KEYWORD[x]))
           f=1;
       }
    if(f==1)
        tokens.add(new Token(string, "KEYWORD", row));
        else
    
        tokens.add(new Token(string, "IDENTIFIER", row));
    }
    
    
    
    
   
    
    else if(state==11)
    {  tokens.add(new Token(string, "CHAR", row));
    } 
    
    else if(state==17)
    {  tokens.add(new Token(string, "STRING", row));
    } 
    
    else if(state==19)
    {  tokens.add(new Token(string, "FLOAT", row));
    } 
    
    else if(state==15)
    {  tokens.add(new Token(string, "FLOAT", row));
    } 
    
    
    
    
    else if  (!string.equals(""))
    {
        tokens.add(new Token(string, "ERROR", row));
    }
    // current char
    if( isDelimiter(currentChar))
      tokens.add(new Token(currentChar+"", "DELIMITER", row));
    else if (isOperator(currentChar)||isOperator1(currentChar) )
      tokens.add(new Token(currentChar+"", "OPERATOR", row));
    // loop
    if (index < line.length()) 
      splitLine(row, line.substring(index));
  }
  
  // calculate state
  private int calculateNextState(int state, char currentChar) {
    if ((isSpace(currentChar) && (state!=13) && (state!=9))  || (isDelimiter(currentChar)&& (state!=9) && (state!=13)) || 
      (isOperator(currentChar)&& (state!=9) && (state!=13)) || (isOperator1(currentChar) && (state!=18) && (state!=9) && (state!=13)) ||isQuotationMark(currentChar) || isnewline(currentChar) )
      return stateTable[state][DELIMITER];
    else if (currentChar == 'b') 
      return stateTable [state][B];
    else if (currentChar == '0')
      return stateTable [state][ZERO];    
    else if (currentChar == '1')
      return stateTable [state][ONE];
    else if (currentChar >='2' && currentChar <='7')
     return stateTable [state][digit];
    else if (currentChar=='8'|| currentChar=='9')
        return stateTable[state][digits];
      
    
    else if ((currentChar >=71 && currentChar<=87) || (currentChar >=103 && currentChar<=109) ||(currentChar >=111 && currentChar<=113) || (currentChar ==95 || currentChar==36  || currentChar==121 ||currentChar==122 || currentChar==89 || currentChar==90)|| currentChar==115 || currentChar==117 || currentChar==118|| currentChar==119)
         
        return stateTable[state][alphabets];
    else if (currentChar== 'x')
        return stateTable[state][x];
    
    else if (currentChar== 92)
        return stateTable[state][back];
    
    else if (currentChar== 34)
        return stateTable[state][doublinverted];
    
     else if (currentChar== 46)
        return stateTable[state][dot];
    
    
    else if (currentChar== 39)
        return stateTable[state][inverted];
    else if (currentChar==110 || currentChar==114|| currentChar==116)
        return stateTable[state][rtn];
    
     else if (currentChar== 69 || currentChar== 'e')
        return stateTable[state][e];
     
      else if (currentChar== 43 || currentChar== 45)
        return stateTable[state][op];
     
    
    else if ((currentChar>=65 && currentChar<=68)||currentChar =='a'|| currentChar =='c'|| currentChar =='d'|| currentChar =='f' || currentChar=='F')
         
        return stateTable[state][hexa];
     
     
    
     
        else 
     return stateTable [state][other];
  }
 
  // isDelimiter
  private boolean isDelimiter(char c) {
     char [] delimiters = {':', ';', '}','{', '[',']','(',')',','};
     for (int x=0; x<delimiters.length; x++) {
      if (c == delimiters[x]) return true;      
     }
     return false;
  }
  
  // isOperator
  private boolean isOperator(char o) {
     // == and != should be handled in splitLine
     char [] operators = {'*','/','<','>','=','!','&','|'};
     for (int x=0; x<operators.length; x++) {
      if (o == operators[x]) return true;      
     }
     return false;
  }
  
  private boolean isOperator1(char o) {
     // == and != should be handled in splitLine
     char [] operators = {'+','-'};
     for (int x=0; x<operators.length; x++) {
      if (o == operators[x]) return true;      
     }
     return false;
  }
  
   private boolean isnewline(char o) {
     // == and != should be handled in splitLine
     char [] operators = {'\n'};
     for (int x=0; x<operators.length; x++) {
      if (o == operators[x]) return true;      
     }
     return false;
  }

  // isQuotationMark
  private boolean isQuotationMark(char o) {
     char [] quote = {};
     for (int x=0; x<quote.length; x++) {
      if (o == quote[x]) return true;      
     }
     return false;
  }

  // isSpace
  private boolean isSpace(char o) {
     return o == ' ';
  }
  
  
  
  // getTokens
  public Vector<Token> getTokens() {
    return tokens;
  }
  
}


