# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer

TCC and VCC metrics produce the same value for a given Java class while the methods in the class are not connected to the other. So we have a value of TCC and LCC of 0. An other solution is *All methods are connected directly to each other*.We can see the code below for TCC=LCC=0:
```java
class Group {

  private int weight;
  private String name;
  private Color color;
  
  public Group(String name, Color color, int weight) {
    this.name = name;
    this.color = color;
    this.weight = weight;
  }
  
  public int compareTo(Group other) {
    return weight - other.weight;
  }
  public void draw() {
    Screen.rectangle(color, name);
  }
}
```

So we can see than the method `compareTo` is connected to the other and the same thing for `draw`. The constructor is not used.

No the LCC can't be lower than TCC because TCC is the ratio of directly(so the connection with a range of 1) connected pairs of node in the graph on the number or all pairs of nodes. And LCC is the ratio of directly and indirectly(so the connection with an unlimited range) connected pairs of node in the graph on the number or all pairs of nodes. So it's impossible to have a LCC lower than TCC.

Note: We can compute the total number pairs of nodes with this equation `AllPairsOfNodes = (NumberOfMethods * (NumbersOfMethods-1)) / 2`
