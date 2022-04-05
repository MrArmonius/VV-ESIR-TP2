package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import com.github.javaparser.ast.expr.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import java.io.File;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;
import java.io.BufferedWriter;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private  Map<String, VariableDeclarator> getterBook = new HashMap<String, VariableDeclarator>();
    private  Map<String, Set<String>> methodVariables = new HashMap<String, Set<String>>();
    private  Map<String, Map<String, Set<String>>> PairNodeClass = new HashMap<String, Map<String, Set<String>>>();

    private int numberMethod = 0;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        String className = (declaration.getFullyQualifiedName().orElse("[Anonymous]")).toString();
        System.out.print(className + " or simply " + declaration.getName() + "\n");

        

        for(FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        PairNodeClass.put(className, calculatePairNodes());
        
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
        
        try {
            FileWriter myWriter = new FileWriter("Report.txt", true);
            BufferedWriter bufWrite = new BufferedWriter(myWriter);
            bufWrite.write(className + " or simply " + declaration.getName() + "\n");
            Set<String> getterKeys = methodVariables.keySet();
            for(String key : getterKeys) {
                bufWrite.write("   " + key + " - "+ methodVariables.get(key) + "\n");
            }
            bufWrite.write("\n" + "TCC score: "+ calculateTCC() + "\n");
            bufWrite.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        
        try {
            File myObj = new File(className);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(className);
            BufferedWriter bufWrite = new BufferedWriter(myWriter);
            bufWrite.write("strict graph {\n");
            Map<String, Set<String>> PairNod = PairNodeClass.get(className);
            Set<String> NodeKeys = PairNod.keySet();
            for(String key : NodeKeys) {
                bufWrite.write(key + ";\n");
            }
            bufWrite.write("}\n");
            bufWrite.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        numberMethod = 0;
        methodVariables.clear();
        getterBook.clear();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        //if(declaration.isPublic()) return;
        for(VariableDeclarator var: declaration.getVariables()) {
            String nomSimple = var.getName().toString();
            getterBook.put(nomSimple, var);
        }
        
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        //if(!declaration.isPublic()) return;
        numberMethod++;
        String nameMethod = declaration.getDeclarationAsString();
        System.out.println("  " + nameMethod);
        //methodVariables.put(nameMethod, new HashSet<String>());
        declaration.findAll(NameExpr.class).stream()
            .filter(name -> getterBook.containsKey(name.getName().toString()))
            .forEach(name -> forEachMethod(name, nameMethod));

        
    }

    private void forEachMethod(NameExpr name, String nameMethod) {
        String nameVariable = name.getName().toString();

        System.out.println("    "+nameVariable);
        if(!methodVariables.containsKey(nameVariable)){
            methodVariables.put(nameVariable, new HashSet<String>());
        }
        methodVariables.get(nameVariable).add(nameMethod);
    }

    private Map<String, Set<String>> calculatePairNodes() {
        Map<String, Set<String>> pairNodes = new HashMap<String, Set<String>>();

        // Create pairNodes
        Set<String> methodKeys = methodVariables.keySet();
        for(String key : methodKeys) {

            Set<String> variables = methodVariables.get(key);
            for(String variable : variables) {

                for(String variable2 : variables) {
                    if(variable.compareTo(variable2) != 0) {
                        String namePair = "\"" + variable + "\" -- \"" + variable2 + "\"";
                        if(!pairNodes.containsKey(namePair)) {
                            pairNodes.put( namePair, new HashSet<String>());
                        }
                        pairNodes.get(namePair).add(key);
                    }
                }
                
            }
            
        }

        return pairNodes;
    }

    private String calculateTotalPairNodes() {
        // Calculate the total numbers of pairs which is possible
        int NumberOfMethods = numberMethod;
        int AllPairsOfNodes = (NumberOfMethods * (NumberOfMethods-1)) / 2;
        String tailleMax = "/"+ AllPairsOfNodes;
        return tailleMax;
    }

    private String calculateTCC() {
        return calculatePairNodes().keySet().size() + calculateTotalPairNodes();
    }

}
