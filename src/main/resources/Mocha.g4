grammar Mocha;

// 程序由多个语句组成
program: statement+ ;

// 语句由很多种
statement
    : varStatement
    | returnStatement
    | blockStatement
    | ifStatement
    | forStatement
    | breakStatement
    | continueStatement
    | throwStatement
    | tryCatchStatement
    | classStatement
    | expresstionStatement
    | ';'
    ;

varStatement: 'var' IDENTIFIER '=' expression ';' ;

returnStatement: 'return' expression ';' ;
throwStatement: 'throw' expression ';';

expresstionStatement: expression ';' ;

blockStatement: '{' statement* '}' ;

ifStatement: 'if' '(' cond=expression ')' ifBody = blockStatement ('else' elseBody=blockStatement)? ;

forStatement: 'for' '(' init=statement cond=statement step=expression ')' body=blockStatement;

breakStatement: 'break' ';';

continueStatement: 'continue' ';';

tryCatchStatement: 'try' tryBody=blockStatement 'catch' '(' IDENTIFIER ')' catchBody=blockStatement;

// 类声明
classStatement
    : 'class' className=IDENTIFIER
      ('extends' parentName=IDENTIFIER)?  // 可选的extends
      '{'
      classBody
      '}'
    ;
// 类体
classBody
    : (classElement)* (constructorDeclaration)? (classElement)*  // 允许空类体,或多个元素
    ;

// 类元素可以是字段、方法或构造函数
classElement
    : fieldDeclaration
    | methodDeclaration
    ;

// 字段声明
fieldDeclaration
    : IDENTIFIER (ASSGIN expression)? ';'?
    ;

// 方法声明
methodDeclaration
    : IDENTIFIER '(' params ')' blockStatement ';'?
    ;

// 构造函数声明
constructorDeclaration
    : CONSTRUCTOR '(' params ')' '{' constructorBody '}'
    ;


// 构造函数体
constructorBody
    : superCall statement*  // 在语义分析时检查super()必须存在且为第一句
    ;

// super调用
superCall
    : 'super' '('(expression ','?)* ')' ';'
    ;


expression
    : assignExpression;

// 左值表达式（理论上只能是IDENT，xx.IDENT，xx[exp]）这里left直接用primary匹配。
// 需要在实际代码解析的时候，判断left如果是unary并且不是IDENT的情况，抛个异常，避免1=2这种赋值。
assignExpression
    : orExpression                                 # AndInAssign
    | left=primary op=ASSGIN right=assignExpression # Assign
    ;

orExpression
    : andExpression                                     # AndInOr
    | left=orExpression op=OR  right=andExpression      # Or
    ;
andExpression
    : eqExpression                                      # EqInAnd
    | left=andExpression op=AND right=eqExpression       # And
    ;
eqExpression
    : compExpression    # CompInEq
    | left=eqExpression op=(EQ|NEQ) right=compExpression # Eq
    ;

compExpression
    : additionExpression # AddInComp
    | left=compExpression op=(LTE|GTE|LT|GT) right=additionExpression # Comp
    ;

additionExpression
    : multiplicationExpression                                          # MulInAdd
    | left = additionExpression op=(PLUS|MINUS) right = multiplicationExpression       # Add
    ;

multiplicationExpression
    : prefixExpression                                                          # PreInMul
    | left = multiplicationExpression op=(MULTIPLY|DIVIDE|MODULUS) right = prefixExpression    # Mul
    ;

prefixExpression
    : postfixExpression                                                       # PostfixInPrefix
    | op=(PLUS|MINUS|NOT|BNOT|INCREMENT|DECREMENT) right=postfixExpression    # Prefix
    ;

postfixExpression
    : primary                                             # FunCallInPost
    | left=primary  op=(INCREMENT|DECREMENT)              # Postfix
    ;

// 单元或者递归的求属性、函数调用等
primary
    : unary                                             # UnaryExpr
    | left=primary '[' expression ']'                   # IndexExpr
    | left=primary '(' arguments ')'                    # CallExpr
    | left=primary '.' IDENTIFIER                       # PropertyExpr
;
unary
    : NUMBER    #Number
    | STRING    #String
    | NULL      #Null
    | TRUE      #True
    | FALSE     #False
    | SUPER      #Super
    | IDENTIFIER#Ident
    | array     # ArrInUnary
    | group     #Gro
    | object    #ObjInUnary
    | function  #FunInUnary
    | new       #NewInUnary
;
group: '(' expression ')';
array
    :   '[' (expression (',' expression)* ','?)? ']'
    ;
object: '{' (pair','?)* '}' ;
pair: key=(STRING | IDENTIFIER) ':' value=expression ;

function: FUNCTION '(' params ')' blockStatement;
params: (IDENTIFIER (',' IDENTIFIER)*)?;
new: NEW IDENTIFIER '(' (expression (',' expression)*)? ')';
arguments: (expression (',' expression)* ','?)?;

NULL: 'null';
TRUE: 'true';
FALSE: 'false';
NEW: 'new';
FUNCTION: 'function';
CONSTRUCTOR: 'constructor';
INCREMENT: '++';
DECREMENT: '--';
PLUS: '+';
MINUS: '-';
BNOT: '~';
EQ: '==';
ASSGIN: '=';
MULTIPLY: '*';
DIVIDE: '/';
MODULUS: '%';
POINT: '.';
OR: '||';
BOR: '|';
AND: '&&';
BAND: '&';
NEQ: '!=';
NOT: '!';
LTE: '<=';
LT: '<';
GTE: '>=';
GT: '>';
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';

SUPER: 'super';
NUMBER: [0-9]+('.' [0-9]+)? ;
STRING: ('"' ( ~["\\] | '\\' . )* '"') | ('\'' ( ~['\\] | '\\' . )* '\'');
IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]* ;

EMPTY: [\t\r\n ]+ -> skip;
// 单行注释
LINE_COMMENT: '//' ~[\r\n]* -> skip ;
// 多行注释
BLOCK_COMMENT: '/*' .*? '*/' -> skip ;