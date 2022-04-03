package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;
import java.io.BufferedWriter;

// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private  Map<String, VariableDeclarator> getterBook = new HashMap<String, VariableDeclarator>();

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        //if(!declaration.isPublic()) return;
        //System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]") + " or simply " + declaration.getName());
        for(FieldDeclaration field : declaration.getFields()) {
            field.accept(this, arg);
        }

        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }
        
        try {
            FileWriter myWriter = new FileWriter("Report.txt", true);
            BufferedWriter bufWrite = new BufferedWriter(myWriter);
            bufWrite.write(declaration.getFullyQualifiedName().orElse("[Anonymous]") + " or simply " + declaration.getName() + "\n");
            Set<String> getterKeys = getterBook.keySet();
            for(String key : getterKeys) {
                bufWrite.write("   " + getterBook.get(key).getName() + "\n");
            }
            bufWrite.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        
        getterBook.clear();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if(declaration.isPublic()) return;
        //visitTypeDeclaration(declaration, arg);
        for(VariableDeclarator var: declaration.getVariables()) {
            String nomSimple = var.getName().toString();
            String nameUppercase = nomSimple.substring(0, 1).toUpperCase() + nomSimple.substring(1);
            String name = "get" + nameUppercase;
            getterBook.put(name, var);
        }
        
    }

    //@Override
    //public void visit(EnumDeclaration declaration, Void arg) {
    //    visitTypeDeclaration(declaration, arg);
    //}

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if(!declaration.isPublic()) return;
        if(getterBook.containsKey(declaration.getName())){
            getterBook.remove(declaration.getName());
        }

        //System.out.println("  " + declaration.getName());

    }

}
