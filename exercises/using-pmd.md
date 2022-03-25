# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

We have downloaded Apache Common Maths and ran the command : `./run.sh pmd -d /home/tbauquin/Téléchargements/commons-math3-3.6.1-src/src/main/java/org/apache/commons/math3/ -f text -R rulesets/java/quickstart.xml`


True positive : java:362:	ForLoopCanBeForeach:	This for loop can be replaced by a foreach loop
```java
  List<int[]> row = new ArrayList<int[]>(dRow.length * 2);
  for (int j = 0; j < dRow.length; ++j) {
            row.add(new int[] { dRow[j][0], lowerIndirection[dRow[j][1]], vSize + dRow[j][2] });
            row.add(new int[] { dRow[j][0], vSize + dRow[j][1], lowerIndirection[dRow[j][2]] });
  }
  ```
           
We can see here that there is a list which is itirated by a for loop. Therefore it should change the for loop by a foreach loop (It is not really an issue actually).

   
False positive : java:142:	LocalVariableNamingConventions:	The final local variable name 'b_two_j_p_1' doesn't match '[a-z][a-zA-Z0-9]*'

  ```java
     final BigDecimal b_two_j_p_1 = new BigDecimal(2 * j + 1, mContext);
  ```
     
The problem here is that the name of the variable does not match the coding convention (there are underscores in the name).

We should not change this name as it is probably a convention that the devellopers or Apache have chosen themselves. 

The convention rule in pmd may not be the one they chose. 
