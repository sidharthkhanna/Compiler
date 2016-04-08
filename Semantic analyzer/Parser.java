package A2;

import com.sun.org.apache.xpath.internal.operations.Equals;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author javiergs
 */
public class Parser {

  private static DefaultMutableTreeNode root;
  private static Vector<Token> tokens;
  private static int currentToken;
  private static Gui gui;

  public static DefaultMutableTreeNode run(Vector<Token> t,Gui gui) {
      A2.SemanticAnalyzer.symbolTable.clear();
     Parser.gui=gui; 
    tokens = t;
    currentToken = 0;
    root = new DefaultMutableTreeNode("program");
    //
    rule_program(root);
    gui.writeSymbolTable(SemanticAnalyzer.getSymbolTable());
    return root;
  }

  public static void error(int err) {
      int n;
      if(currentToken<tokens.size())
n = tokens.get(currentToken).getLine();
      else 
          n=tokens.get(currentToken-1).getLine();
      
switch (err) {
case 1: 
    gui.writeConsole("Line" + n + ": expected {"); break;
case 2: 
    gui.writeConsole("Line" + n + ": expected }"); break;
case 3: gui.writeConsole("Line" + n + ": expected ;"); break;
case 4:gui.writeConsole("Line" +n+": expected identifier or keyword");
break;
case 5:
gui.writeConsole("Line" +n+": expected ="); break;
case 6:
    gui.writeConsole("Line" +n+": expected identifier"); break;
case 7:
gui.writeConsole("Line" +n+": expected )"); break;
case 8:
gui.writeConsole("Line" +n+": expected ("); break;
case 9:
gui.writeConsole("Line" +n+": expected value, identifier, (");
break;
}
}
  
 public static boolean rule_program(DefaultMutableTreeNode parent) {
      boolean error = false;
     
      
      
if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("{")) 
{  
    
  currentToken++;
  
 
  
    DefaultMutableTreeNode node;
    node = new DefaultMutableTreeNode("{");
      parent.add(node);
     node = new DefaultMutableTreeNode("body");
     parent.add(node);
     error=rule_body(node);
}
else
    
{
while(currentToken<tokens.size()&&!tokens.get(currentToken).getWord().equals("print")
        &&!tokens.get(currentToken).getToken().equals("IDENTIFIER")
        &&!tokens.get(currentToken).getWord().equals("return")
        &&!tokens.get(currentToken).getWord().equals("if")
        &&!tokens.get(currentToken).getWord().equals("while")
        &&!tokens.get(currentToken).getWord().equals("int")
        &&!tokens.get(currentToken).getWord().equals("boolean")
        &&!tokens.get(currentToken).getWord().equals("float")
        &&!tokens.get(currentToken).getWord().equals("char")
        &&!tokens.get(currentToken).getWord().equals("void")
        &&!tokens.get(currentToken).getWord().equals("string")
        &&!tokens.get(currentToken).getWord().equals("}")
        )
{
    currentToken++;
}
  error(1);
    DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode("");
     parent.add(node);
     error=rule_body(node);
}
     if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("}"))
{
    currentToken++;
DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode("}");
      parent.add(node);
}
else
{ 
  
  
  
    error(2);
 
}

return error;
}
 
 public static boolean rule_body(DefaultMutableTreeNode parent) {
      boolean error=false;
        DefaultMutableTreeNode node = null;
while(currentToken<tokens.size()&&(!tokens.get(currentToken).getWord().equals("}")))
{ 
    if(tokens.get(currentToken).getToken().equals("IDENTIFIER"))
        
    {  if(A2.SemanticAnalyzer.symbolTable.containsKey(tokens.get(currentToken).getWord()))
           
       {   
              String type = ((SymbolTableItem)(A2.SemanticAnalyzer.symbolTable.get(tokens.get(currentToken).getWord()).get(0))).getType();
       A2.SemanticAnalyzer.pushStack(type);
       }
       else
       {
     A2.SemanticAnalyzer.error(gui, 4, tokens.get(currentToken).getLine());
       A2.SemanticAnalyzer.pushStack("error");
       }
        currentToken++;
     node = new DefaultMutableTreeNode("assignment");
     parent.add(node);
error=RULE_ASSIGNMENT(node);

if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))

    currentToken++;

else
             
    error(3);
    }
    else if(tokens.get(currentToken).getWord().equals("print"))
    {
     currentToken++;
      node = new DefaultMutableTreeNode("print");
     parent.add(node);
     error=rule_print(node);
        if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
       currentToken++;
        else
            error(3);
    
        
    }
    else if(tokens.get(currentToken).getWord().equals("int")||tokens.get(currentToken).getWord().equals("float")||tokens.get(currentToken).getWord().equals("boolean")||tokens.get(currentToken).getWord().equals("char")||tokens.get(currentToken).getWord().equals("string")||tokens.get(currentToken).getWord().equals("void"))
    {    
        currentToken++;
       
     node = new DefaultMutableTreeNode("variable");
     parent.add(node);
     error=rule_variable(node);
     
          if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
        
         currentToken++;
        else
     
            error(3);   
        
    }
    
    else if(tokens.get(currentToken).getWord().equals("while"))
    {
     
      node = new DefaultMutableTreeNode("while");
     parent.add(node);
     currentToken++;
     error=rule_while(node);
  
        
    
        
    }
    
     else if(tokens.get(currentToken).getWord().equals("if"))
    {
     
      node = new DefaultMutableTreeNode("if");
     parent.add(node);
     currentToken++;
     error=rule_if(node);
       
    
        
    }
     else if(tokens.get(currentToken).getWord().equals("return"))
    {
     
      node = new DefaultMutableTreeNode("return");
     parent.add(node);
    rule_return(node);
    if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
         currentToken++;
     else
         error(3);
     
     
    
        
    }

   else
     {
          
             if(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(";"))
             {      
                 currentToken++;
             
               }
             else if(currentToken<tokens.size())
             { currentToken++;
                 error(4);
             }
             
            
               
             
   
}

}

  
        
return error;
}
 static int count=0;
static String x;
static String y;
static String result4;
static String ops;   
static String ops1;  

 
 public static boolean RULE_ASSIGNMENT(DefaultMutableTreeNode parent) {
      boolean error = false;
      
if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("=")) 
{  
    currentToken++;
    DefaultMutableTreeNode node;
    node = new DefaultMutableTreeNode("=");
     parent.add(node);
     node = new DefaultMutableTreeNode("expression");
     parent.add(node);
       error=rule_expression(node);
  String x = SemanticAnalyzer.popStack();
String y = SemanticAnalyzer.popStack();


String result4 = SemanticAnalyzer.calculateCube(x, y, "=" );

if(result4=="error")

     A2.SemanticAnalyzer.error(gui, 2, tokens.get(currentToken).getLine());

    
   
   
     
    
}
     else

  
{




while(currentToken<tokens.size()&& !tokens.get(currentToken).getWord().equals(";")&&!tokens.get(currentToken).getWord().equals("(")&&!tokens.get(currentToken).getWord().equals(")")&& !tokens.get(currentToken).getWord().equals("!")&&!tokens.get(currentToken).getWord().equals("-")&&!tokens.get(currentToken).getToken().equals("INTEGER")&&!tokens.get(currentToken).getToken().equals("OCTAL")&&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")&&!tokens.get(currentToken).getToken().equals("BINARY")&&!tokens.get(currentToken).getWord().equals("TRUE")&&!tokens.get(currentToken).getToken().equals("FALSE")&&!tokens.get(currentToken).getToken().equals("FLOAT")&&!tokens.get(currentToken).getToken().equals("STRING")&&!tokens.get(currentToken).getToken().equals("IDENTIFIER")&&!tokens.get(currentToken).getToken().equals("CHAR"))
{    
currentToken++;
}
error(5);

DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode("");
     parent.add(node);
     error=rule_expression(node);
}
return error;
}
  public static boolean rule_print(DefaultMutableTreeNode parent) {
      boolean error = false;
      
if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("(")) 
{  
    currentToken++;
    DefaultMutableTreeNode node;
      node = new DefaultMutableTreeNode("(");
     parent.add(node);
     node = new DefaultMutableTreeNode("expression");
     parent.add(node);
     error=rule_expression(node);
    
}
     else
{
  

while(currentToken<tokens.size()&& !tokens.get(currentToken).getWord().equals("!")&&!tokens.get(currentToken).getWord().equals(")")&&!tokens.get(currentToken).getWord().equals("-")&&!tokens.get(currentToken).getToken().equals("INTEGER")&&!tokens.get(currentToken).getToken().equals("OCTAL")&&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")&&!tokens.get(currentToken).getToken().equals("BINARY")&&!tokens.get(currentToken).getWord().equals("TRUE")&&!tokens.get(currentToken).getToken().equals("FALSE")&&!tokens.get(currentToken).getToken().equals("FLOAT")&&!tokens.get(currentToken).getToken().equals("STRING")&&!tokens.get(currentToken).getToken().equals("IDENTIFIER")&&!tokens.get(currentToken).getToken().equals("CHAR"))
{    
currentToken++;
}
error(8);

DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode(" ");
parent.add(node);
     error=rule_expression(node);

}
if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(")")) 
{     DefaultMutableTreeNode node;
   node = new DefaultMutableTreeNode(")");
     parent.add(node);
    currentToken++;
  
    
}
else
{
   
    error(7);
}
return error;
}
  public static int flag=0;
   public static  boolean rule_variable(DefaultMutableTreeNode parent) {
      boolean error = false;
      
if (currentToken<tokens.size()&&tokens.get(currentToken).getToken().equals("IDENTIFIER")) 
{  SemanticAnalyzer.checkVariable(
tokens.get(currentToken-1).getWord(),
tokens.get(currentToken).getWord()
);
if(flag==1)
{A2.SemanticAnalyzer.error(gui, 1, tokens.get(currentToken).getLine());
flag=0;
}
    currentToken++;
    DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("identifier");
     parent.add(node);
    
    
}
else
{
error(6);
}

return error;
}
   public static boolean rule_while(DefaultMutableTreeNode parent) {
      boolean error = false;
      
if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("(")) 
{  
    currentToken++;
     
    DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("(");
     parent.add(node);
     node = new DefaultMutableTreeNode("expression");
     parent.add(node);
     
     error=rule_expression(node);
      String x = SemanticAnalyzer.popStack();
     if(!x.equals("boolean"))

{
    A2.SemanticAnalyzer.error(gui, 3, tokens.get(currentToken).getLine());
}
  
     
}

     else
{
  

while(currentToken<tokens.size()&& !tokens.get(currentToken).getWord().equals("!")&&!tokens.get(currentToken).getWord().equals(")")&&!tokens.get(currentToken).getWord().equals("-")&&!tokens.get(currentToken).getToken().equals("INTEGER")&&!tokens.get(currentToken).getToken().equals("OCTAL")&&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")&&!tokens.get(currentToken).getToken().equals("BINARY")&&!tokens.get(currentToken).getWord().equals("TRUE")&&!tokens.get(currentToken).getToken().equals("FALSE")&&!tokens.get(currentToken).getToken().equals("FLOAT")&&!tokens.get(currentToken).getToken().equals("STRING")&&!tokens.get(currentToken).getToken().equals("IDENTIFIER")&&!tokens.get(currentToken).getToken().equals("CHAR"))
{    
currentToken++;
}
error(8);

DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode(" ");
parent.add(node);
     error=rule_expression(node);

}

     if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(")")) 
{  
    currentToken++;
    
   DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode(")");
     parent.add(node);
      node = new DefaultMutableTreeNode("program");
     parent.add(node);
     
     error=rule_program(node);
    
    
}
  
else 
     {      
    
while(currentToken<tokens.size()&&!tokens.get(currentToken).getWord().equals("{"))
{
    currentToken++;
}

error(7);
DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("");
     parent.add(node);
     node = new DefaultMutableTreeNode("program");
     parent.add(node);
     
 error=rule_program(node);
}
    
    




 


return error;
}
   public static boolean rule_if(DefaultMutableTreeNode parent) {
      boolean error = false;
      if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("(")) 
{  
    currentToken++;
     
    DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("(");
     parent.add(node);
     node = new DefaultMutableTreeNode("expression");
     parent.add(node);
    
     error=rule_expression(node);
       String x = SemanticAnalyzer.popStack();
     if(!x.equals("boolean"))

{
    A2.SemanticAnalyzer.error(gui, 3, tokens.get(currentToken).getLine());
}

}
      
     











     else
{
  

while(currentToken<tokens.size()&& !tokens.get(currentToken).getWord().equals("!")&&!tokens.get(currentToken).getWord().equals(")")&&!tokens.get(currentToken).getWord().equals("-")&&!tokens.get(currentToken).getToken().equals("INTEGER")&&!tokens.get(currentToken).getToken().equals("OCTAL")&&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")&&!tokens.get(currentToken).getToken().equals("BINARY")&&!tokens.get(currentToken).getWord().equals("TRUE")&&!tokens.get(currentToken).getToken().equals("FALSE")&&!tokens.get(currentToken).getToken().equals("FLOAT")&&!tokens.get(currentToken).getToken().equals("STRING")&&!tokens.get(currentToken).getToken().equals("IDENTIFIER")&&!tokens.get(currentToken).getToken().equals("CHAR"))
{    
currentToken++;
}
error(8);

DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode(" ");
parent.add(node);
     error=rule_expression(node);

}
     if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(")")) 
{  
    currentToken++;
    
   DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode(")");
     parent.add(node);
      node = new DefaultMutableTreeNode("program");
     parent.add(node);
     
     error=rule_program(node);
 
  
    
}
else 
     {      
    error(7);
while(currentToken<tokens.size()&&!tokens.get(currentToken).getWord().equals("{"))
{
    currentToken++;
}
DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("");
     parent.add(node);
     node = new DefaultMutableTreeNode("program");
     parent.add(node);
     
 error=rule_program(node);
}
    
    




      if(currentToken==tokens.size())
      {
        currentToken--;    
      }
      


if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("else")) 
{  
    currentToken++;
    
    DefaultMutableTreeNode node;
    node = new DefaultMutableTreeNode("else");
     parent.add(node);
    
     node = new DefaultMutableTreeNode("program");
     parent.add(node);
     error=rule_program(node);
    
    
} 

return error;
}
 public static boolean rule_return(DefaultMutableTreeNode parent) 
         
     {  
         currentToken++;
         return true;
         
     }
 public static int count4=0;
 public static boolean rule_expression(DefaultMutableTreeNode parent) {
     boolean error=false;
     DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("X");
     parent.add(node);
error=RULE_X(node);
while (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("|")) {
    count4++;

currentToken++;
node = new DefaultMutableTreeNode("|");
     parent.add(node);
     node = new DefaultMutableTreeNode("X");
     parent.add(node);
error=RULE_X(node);
   if (count4==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "|" );
SemanticAnalyzer.pushStack(result4);

        
          
              
            count4=0;
        
       
        }
}
return error;
}
static int count1=0;
static int count7=0;static int count6=0;static int count8=0;static int count9=0;static int count10=0;static int count11=0;static int count12=0;
static String ops3;
static int count13=0;

 public static boolean RULE_X(DefaultMutableTreeNode parent) {
        boolean error=false;
      DefaultMutableTreeNode node;
     node = new DefaultMutableTreeNode("Y");
     parent.add(node);
error=RULE_Y(node);
while (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("&")) {
    count1++;
      
currentToken++;
node = new DefaultMutableTreeNode("&");
     parent.add(node);
node = new DefaultMutableTreeNode("Y");
     parent.add(node);
error=RULE_Y(node);
 
   if (count1==1)
        {             x = SemanticAnalyzer.popStack();
     
         
            
 y = SemanticAnalyzer.popStack();

 result4 = SemanticAnalyzer.calculateCube (x, y, "&" );
SemanticAnalyzer.pushStack(result4);

      
          
            count1=0;
        
       
        }
 
}
return error;
}
 static int count2=0; 
 public static boolean RULE_Y(DefaultMutableTreeNode parent) {
     boolean error;
if (currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("!")) {
    count2++;
    ops=tokens.get(currentToken).getWord();
currentToken++;
DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode("!");
     parent.add(node);

} 
DefaultMutableTreeNode node;
node = new DefaultMutableTreeNode("R");
     parent.add(node);
error=RULE_R(node);
if(count2==1)
{
                x = SemanticAnalyzer.popStack();
   
        
 
 result4 = SemanticAnalyzer.calculateCube (x, "!" );
SemanticAnalyzer.pushStack(result4);

      
         
            count2=0;
}


return error;
}
 public static boolean RULE_R(DefaultMutableTreeNode parent) {
     boolean error=false;
     DefaultMutableTreeNode node;
    node = new DefaultMutableTreeNode("E");
    parent.add(node);
     error=RULE_E(node);
while ((currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("<"))
||(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals(">"))
|(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("=="))
|(currentToken<tokens.size()&&tokens.get(currentToken).getWord().equals("!="))
) { 
    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("<")) {
        count6++;
    
        node = new DefaultMutableTreeNode("<");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        error = RULE_E(node);
          if (count6==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "<" );
SemanticAnalyzer.pushStack(result4);

        
      
            count6=0;
        
       
        }
      } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(">")) {
          count7++;
            
        node = new DefaultMutableTreeNode(">");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        error = RULE_E(node);
          if (count7==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
                     y = SemanticAnalyzer.popStack();
 
                    result4 = SemanticAnalyzer.calculateCube (x, y, ">" );
                      SemanticAnalyzer.pushStack(result4);

        
       
            count7=0;
        
       
        }
      } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("==")) {
          count8++;
        
        node = new DefaultMutableTreeNode("==");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        error = RULE_E(node);
          if (count8==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "==" );
SemanticAnalyzer.pushStack(result4);

        
           
            count8=0;
        
       
        }
      } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!=")) {
          count9++;
           
        node = new DefaultMutableTreeNode("!=");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        error = RULE_E(node);
          if (count9==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "!=" );
SemanticAnalyzer.pushStack(result4);

        
        
            count9=0;
        
       
        }
      }

}
return error;
}

  private static boolean RULE_E(DefaultMutableTreeNode parent) {
    boolean error;
    DefaultMutableTreeNode node;
    node = new DefaultMutableTreeNode("A");
    parent.add(node);
    error = rule_A(node);
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) {
        
      if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+")) {
          count10++;
          
        node = new DefaultMutableTreeNode("+");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("A");
        parent.add(node);
        error = rule_A(node);
        if(count10==1)
        {             x = SemanticAnalyzer.popStack();
       
        
                      y = SemanticAnalyzer.popStack();
 
                   result4 = SemanticAnalyzer.calculateCube (x, y, "+" );
                   SemanticAnalyzer.pushStack(result4);

        
          
            count10=0;
        
       
        }




      } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) {
          count11++;
         
        node = new DefaultMutableTreeNode("-");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("A");
        parent.add(node);
        error = rule_A(node);
        if (count11==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "-" );
SemanticAnalyzer.pushStack(result4);

        
      
     
            count11=0;
        
       
        }
      }
    }
    return error;
  }
  

  private static boolean rule_A(DefaultMutableTreeNode parent) {
    boolean error;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("B");
    parent.add(node);
    error = rule_B(node);
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) {
     
        if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*")) {
            count12++;
            
        node = new DefaultMutableTreeNode("*");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("B");
        parent.add(node);

        error = rule_B(node);
         if (count12==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "*" );
SemanticAnalyzer.pushStack(result4);

        
          
            count12=0;
        
       
        }

      } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) {
          count13++;
           
        node = new DefaultMutableTreeNode("/");
        parent.add(node);
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
        currentToken++;
        error = rule_B(node);
          if (count13==1)
        {             x = SemanticAnalyzer.popStack();
     
        
        
 y = SemanticAnalyzer.popStack();
 
 result4 = SemanticAnalyzer.calculateCube (x, y, "/" );
SemanticAnalyzer.pushStack(result4);

        
      
            count13=0;
        
       
        }
      }
    }
    return error;
  }
static int count5=0;
  private static boolean rule_B(DefaultMutableTreeNode parent) {
    boolean error;
    DefaultMutableTreeNode node;
    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) {
       count5++;
    ops=tokens.get(currentToken).getWord();
      node = new DefaultMutableTreeNode("-");
      parent.add(node);
      currentToken++;
      
     
    
      
    }
   
    node = new DefaultMutableTreeNode("C");
    parent.add(node);
    error = rule_C(node);
    
    
    if (count5==1)
        {             x = SemanticAnalyzer.popStack();
     
   
        
      
 
 
 result4 = SemanticAnalyzer.calculateCube (x, "-" );
SemanticAnalyzer.pushStack(result4);

     
         
            count5=0;
        
       
        }
    
    
    
    
      return error;
     
  }
static String s1;
  private static boolean rule_C(DefaultMutableTreeNode parent) {
    boolean error;
 
   
    DefaultMutableTreeNode node;
    if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("INTEGER")) {
      node = new DefaultMutableTreeNode("integer" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      s1="int";
       A2.SemanticAnalyzer.pushStack(s1);
       currentToken++;
      
       
    } else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")) {
      node = new DefaultMutableTreeNode("identifier" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
       if(A2.SemanticAnalyzer.symbolTable.containsKey(tokens.get(currentToken).getWord()))
           
       {   
              String type = ((SymbolTableItem)(A2.SemanticAnalyzer.symbolTable.get(tokens.get(currentToken).getWord()).get(0))).getType();
       A2.SemanticAnalyzer.pushStack(type);
       }
       else
       {
     A2.SemanticAnalyzer.error(gui, 4, tokens.get(currentToken).getLine());
       A2.SemanticAnalyzer.pushStack("error");
       }
           currentToken++;
  
    } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("true")) {
      node = new DefaultMutableTreeNode("(");
      parent.add(node);
         s1="boolean";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
    }else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("false")) {
      node = new DefaultMutableTreeNode("(");
      parent.add(node);
         s1="boolean";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
    }  else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("OCTAL")) {
      node = new DefaultMutableTreeNode("octal" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         s1="int";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
    }else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("BINARY")) {
      node = new DefaultMutableTreeNode("binary" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         s1="int";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
       }else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("CHAR")) {
      node = new DefaultMutableTreeNode("char" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         s1="char";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
       }else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("STRING")) {
      node = new DefaultMutableTreeNode("string" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         s1="string";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
       }else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("FLOAT")) {
      node = new DefaultMutableTreeNode("float" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         s1="float";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
       }else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("HEXADECIMAL")) {
      node = new DefaultMutableTreeNode("hexadecimal" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         s1="int";
       A2.SemanticAnalyzer.pushStack(s1);
      currentToken++;
      
    } else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("(")) {
      node = new DefaultMutableTreeNode("(");
      parent.add(node);
      currentToken++;
      //
      node = new DefaultMutableTreeNode("expression");
      parent.add(node);
      error = rule_expression(node);
      //
      node = new DefaultMutableTreeNode(")");
      parent.add(node);
      currentToken++;      
    
    } else {
    
      error(9);
    }
 
    
    return false;
  }

}



