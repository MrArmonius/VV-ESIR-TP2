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
            
We can see here that there is a list which is itirated by a for loop. Therefore it should change the for loop by a foreach loop.

False positive : 
