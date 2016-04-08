package A2;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

public class SemanticAnalyzer {
  
  public static final Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<String, Vector<SymbolTableItem>>();
  public static final Stack stack = new Stack();
  
  private static final int INT=0;
    private static final int FLOAT=1;
      private static final int CHAR=2;
        private static final int STRING=3;
          private static final int BOOLEAN=4;
            private static final int VOID=5;
              private static final int error=6;
              private static final int sub=0;
              private static final int multiply=1;
              private static final int divide=2;
              private static final int add=3;
              private static final int subun=0;
              private static final int greater=4;
              private static final int lesser=5;
              private static final int notequal=6;
              private static final int compare=7;
              private static final int and=8;
              private static final int not=9;
              private static final int notun=1;
              private static final int equal=10;
               private static final int error1=2;
              
              
              
                        
                
                
                
                
                private static final int[][][] cube={
                    {
                    
                        {INT,FLOAT,error,error,error,error,error},
                        {FLOAT,FLOAT,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,error,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                           
                },{
                        {INT,FLOAT,error,error,error,error,error},
                        {FLOAT,FLOAT,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,error,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
                {
                        {INT,FLOAT,error,error,error,error,error},
                        {FLOAT,FLOAT,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,error,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                        
                },{
                       {INT,FLOAT,error,STRING,error,error,error},
                        {FLOAT,FLOAT,error,STRING,error,error,error},
                        {error,error,error,STRING,error,error,error},
                        {STRING,STRING,STRING,STRING,STRING,error,error},
                         {error,error,error,STRING,error,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                        
                },
                {
                        {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,error,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
                {
                
                        {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,error,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
                {
                      {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {error,error,BOOLEAN,error,error,error},
                        {error,error,error,BOOLEAN,error,error,error},
                         {error,error,error,error,BOOLEAN,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
                {
                      {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {BOOLEAN,BOOLEAN,error,error,error,error,error},
                        {error,error,BOOLEAN,error,error,error},
                        {error,error,error,BOOLEAN,error,error,error},
                         {error,error,error,error,BOOLEAN,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
                {
                    
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,BOOLEAN,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
                {
                     {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                        {error,error,error,error,error,error,error},
                         {error,error,error,error,BOOLEAN,error,error},
                          {error,error,error,error,error,error,error},
                           {error,error,error,error,error,error,error}
                },
               
                {
                     {INT,error,error,error,error,error,error},
                        {FLOAT,FLOAT,error,error,error,error,error},
                        {error,error,CHAR,error,error,error,error},
                        {error,error,error,STRING,error,error,error},
                         {error,error,error,error,BOOLEAN,error,error},
                          {error,error,error,error,error,VOID,error},
                           {error,error,error,error,error,error,error}
                }
                };
                
                private static final int[][]cube1=
                {
                  {INT,FLOAT,error,error,error,error,error},
                   
                     {error,error,error,error,BOOLEAN,error,error},
                      
                   
                     {error,error,error,error,error,error,error}
                };
                  
                      
                
  
  // create here a data structure for the cube of types
  
  public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() {
    return symbolTable;
  }
  
  public static void checkVariable(String type, String id) {
   
   
    // A. search the id in the symbol table

    // B. if !exist then insert: type, scope=global, value={0, false, "", '')
       if(symbolTable.containsKey(id))
       {   
         A2.Parser.flag=1;
       }
       else
       {  Vector v = new Vector();
    v.add(new SymbolTableItem(type,"global", ""));
   
    symbolTable.put(id, v);
       }
      
 
    // C. else error: “variable id is already defined”
  }
  

          

  public static void pushStack(String type) {
  
    // push type in the stack
      stack.push(type);
  }
  
  public static String popStack() {
    String result="";
    // pop a value from the stack
    result=(String) stack.pop();
    return result;
  }
  
  
  public static String calculateCube(String type, String operator) {
      int op=2; 
              int type1=6;
               String result="";   
      if(type.equals("int"))
              type1=0;
       if(type.equals("float"))
              type1=1;
       if(type.equals("char"))
              type1=2;
         if(type.equals("string"))
              type1=3;
         if(type.equals("boolean"))
              type1=4;
         if(type.equals("void"))
              type1=5;
         if(type.equals("error"))
              type1=6;
         
         
          if(operator.equals("-"))
              op=0;
          if(operator.equals("!"))
              op=1;
          int result1=cube1[op][type1];
          if(result1==0)
              result="int";
          if(result1==1)
              result="float";
             if(result1==4)
             result="boolean";
          
          if(result1==6)
              result="error";
              
          
  
      
    // unary operator ( - and !)
    return result;
  }

  public static  String calculateCube(String type1, String type2, String operator) {
      int op1=0;
      int type3=0;
      int type4=0;
       String result="";
      if(type1.equals("int"))
              type3=0;
       if(type1.equals("float"))
              type3=1;
       if(type1.equals("char"))
              type3=2;
         if(type1.equals("string"))
              type3=3;
         if(type1.equals("boolean"))
              type3=4;
         if(type1.equals("void"))
              type3=5;
          if(type1.equals("error"))
              type3=6;
          if(type2.equals("int"))
              type4=0;
       if(type2.equals("float"))
              type4=1;
       if(type2.equals("char"))
              type4=2;
         if(type2.equals("string"))
              type4=3;
         if(type2.equals("boolean"))
              type4=4;
         if(type2.equals("void"))
              type4=5;
         if(type2.equals("error"))
              type4=6;
         if(operator.equals("-"))
             op1=0;
          if(operator.equals("*"))
             op1=1;
           if(operator.equals("/"))
             op1=2;
            if(operator.equals("+"))
             op1=3;
             if(operator.equals(">"))
             op1=4;
              if(operator.equals("<"))
             op1=5;
               if(operator.equals("!="))
             op1=6;
                if(operator.equals("=="))
             op1=7;
                 if(operator.equals("&"))
             op1=8;
                  if(operator.equals("|"))
             op1=9;
                   if(operator.equals("="))
             op1=10;
         int result2=cube[op1][type3][type4];
              
         if(result2==0)
             result="int";
          if(result2==1)
             result="float";
           if(result2==2)
             result="char";
            if(result2==3)
             result="string";
             if(result2==4)
             result="boolean";
              if(result2==5)
             result="void";
         if(result2==6)
             result="error";
   
    // binary operator ( - and !)
    return result;
  }
  
  public static void error(Gui gui, int err, int n) {
    switch (err) {
      case 1: 
        gui.writeConsole("Line" + n + ": variable id is already defined"); 
        break;
      case 2: 
        gui.writeConsole("Line" + n + ": incompatible types: type mismatch"); 
        break;
      case 3: 
        gui.writeConsole("Line" + n + ": incompatible types: expected boolean"); 
        break;
      case 4:
            gui.writeConsole("Line" + n + ": variable not found"); 
        break;

    }
  }
  
}
