# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

To run our rule we must run this command:
```
./run.sh pmd -d /home/tbauquin/Téléchargements/pmd-bin-6.42.0/bin/main.java -f text -R if.xml 

/home/tbauquin/Téléchargements/pmd-bin-6.42.0/bin/main.java:6:	3if:	Warning : There is more than 3 if statements
```
We can see than the output of the command warning us about there is a 3if which appears. So our rule detects correctly the 3 if statements.


We have here the rule in the format xml. We can recognize the name and the message than we see in the message above.
Our rule is written in value's balise, we have `//IfStatement//IfSTatement//IfStatement`. This rule isn't really esthetic, we have tried with the function `count`, but it wasn't working. We had this rule `//IfStatement[ count(IfStatement) < #maxSize]` where `#maxSize` is the propriety where we define the number of IfStatement inside the first IfStatement.
```xml
    <ruleset name="truc">
        <rule name="3if"
              language="java"
              message="Warning : There is more than 3 if statements"
              class="net.sourceforge.pmd.lang.rule.XPathRule">
           <description>

           </description>
           <priority>3</priority>
           <properties>
              <property name="version" value="2.0"/>
              <property name="xpath">
                 <value>
        <![CDATA[
        //IfStatement//IfStatement//IfStatement
        ]]>
                 </value>
              </property>
           </properties>
        </rule>
    </ruleset>
```
We test our rule with this example in a first time. After we use the example given on the subject.
```java
public class main{

public static void main(String args[]){
    if(true){
        if(true){
            if(true){
            }
        }
    }
}

}
```

