# My notes
While implementing the ChessBoard class, I used a 2D array to
create the board. I set the boundaries to 8x8, although I am
still unsure whether I should use 0-7 or 1-8 due to the indexing.

I discussed the structure of my board with a friend, and he suggested me to start by
writing the ChessPiece class first, since nothing really depends on the board itself at
this time. I will start by writing the ChessPiece class and then move on to the
ChessMoves class.

# Next steps:
- Refactor code and take practice Exam.

## Class Notes (January 13th):

### Compiling:
- How to compile and execute files in java:
    - 'java -cp /bin <filenamewithMain> <args>'
- Where to find java built-in classes documentation:
    - Search 'Java 23 api' on Google.

### Data Types:
For every primitive data type in java, there is an associated class that contains methods.
These methods are used to execute methods on the primitive data types 
(e.i toString() on an int). Syntax to use these classes are used the following way:
    - WrapperClass.methodName(primitiveDataType)
    - Example: Integer.toString(5)

### Memory Management:
In Java, all Objects are stored in heap memory. Also, pointers are not used,
instead Java uses references. The runtime stack contains references to objects in the heap.
Primitive types can be stored in both the runtime stack and in the heap.
The only way to create objects is to call 'new'.

### Inmutable objects:
In Java, Strings are inmutable objects. This means that once a string is created, 
it cannot be changed.

### Efficency:
In Java, garbage management is done automatically. However, it takes a toll on effieciency.
Concatenating strings is not efficient because it creates a new string every time. Use strignbuilder instead.

### More Java Class Notes (January 15th):
 Arrays are objects, so always stored on the heap. When creating an array of objects, the elements within
 the array will be null pointers (because objects are references to the heap). Therefore, the array must be
 iterated over creating 'new' objects.

Packages use '.' instead '/' to be found. Packages are basically folders. Packages are useful when two classes
have the same name (common when using other people's code).
 
## Creating Packages and classes:
    Package core;
    
    Year in school class:
    
    public enum YearInSchool {
        NONE,
        FRESHMAN,
        SOPHOMORE,
        JUNIOR,
        SENIOR,
    }
    Person class:
    
    public class Person {
        private String name;
        private int age;
        private YearInSchool year;
    }

## Inheritance:
All Java classes inherit from a common ancestor 'object' class. This 'object' class allows for all classes
to have universal methods. They can be found at 'java.base > java.lan > Object'.

## Overriding Universal Methods such as equals(Object obj) and hasCode():
Example:

    @override
    public String toString() {
        return String.format("[%s] name: %s, age: %d, year: %s , this.getClass().getName(),
            string.name, this.age, this.year);
    }
    @override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Person p = (Person)obj;
        return (this.name.equals(p.name) && this.age == p.age && this.year == p.year);
    }
    @override
    public int hasCode() {
        return (name.hasCode() +*this.age) ^ this.year.hasCode();
    }

An interface just declare methods but does not implement them, the subclasses take care of that, giving control. Use "class x implements".
If it was superclass, use "extends" instead. Use @override to change methods in the subclasses. Once the
subclasses are all created and are implementing the interface, have the chessPiece class call the shared method
from the subclasses (e.i: PieceMoveCalculator.Move()).

## January 22, 2025 notes:

### Static Methods:
static variables are associated with the class not its instances.
Use them in special cases where you won't create instances of a class, or all instances should
share the same values.
Example: If the variable of a Date class were static, all dates in a program would represent the same date.
If the static methods or variables are public, they can be called anywhere (they are not attached to an object).
Static methods can only use other static variables or methods inside itself because there is no object it is
attached to. There is no 'this.something' because it does not hold object copies.
Best practices:
    -Put static variables and methods first in the class, then place the non-static variables and methods
    -to keep things organized.

### Records:

Records are classes that represent data fields.
When using records, the record will produce all the variables and getters from the class you want to use for you.
Records are always inmutable.

### More on Inheritance:

Inheritance is useful for code reuse. Subclasses will inherit variables and methods from the superclass.
Once a subclass is made, things can be added to the subclass. If that subclass gets its own subclass,
the latter subclass will inherit the attributes and methods of both the superclass and the first subclass.
Polymorphism is why inheritance is also useful. 

    -Use 'super(x,y,z)' to initialize the superclass' constructor. If you do not do this, the compiler will by default
    call the empty superclass constructor. If there is not empty superclass cosntructor, the compiler will get upset.
    -Then initialize your own variables.
    -You can overide superclass methods or add to them. If you want to add to them, you return the superclass
    method and then add whatever you want. Remember to say 'super.thesuperclassmethod' when returning it,
    otherwise you will get caught in an infinite loop.

Private methods allow variables and methods to be used only within the class they are declared in.
protected variables and methods are only visible/accesible in the superclass and its subclasses (familiy secrets).
When you use 'Abstract' methods are defined but not implemented, which means that the abstarct methods must be
overidden by the subclasses. An abstract class makes it so that it is the job of subclasses to overide
all of its methods. Abstract means it is not full defined. The opposite is 'concrete' or fully defined.
If you do not want sublasses to overide methods from superclasses, then you must declare those methods
'final' (it cannot be changed). If you make a class final, you cannot inherit from it. 

## January 27, 2025 notes:

### Interfaces:
A class can only have one super class, but it can have multiple interfaces. An interface is a datatype that
contains abstract methods.

# Determining object class:
    o.getClass()
        if (o.getClass() == Person.class) {...}
    instanceOf 
        if (o instanceOf Person) {...}

# Copying Objects:

Almost always, you want to make a deep copy of an object.

    -Shallow copy: Copies the reference to the object, not the object itself. If the object is changed, the copy is changed.
    -Deep copy: Copies the object itself. If the object is changed, the copy is not changed.

Every class, because of the root object class, has a "clone()" method. 
Override this method to make a deep copy.

Another way to make a copy is using a copy constructor. 
This is a constructor that takes an object of the same type and makes a copy of it.

# Error Handling:

Errors: 

    Implies a catastrophic failure. Errors are not meant to be caught, they are thrown by
    the virtual machine (i.e. OutOfMemoryError).

Exceptions:

    runtime exceptions: 
        Unchecked exceptions, The compiler knows it can happen anywhere so you don't have to handle it with a catch block.
        i.e. NullPointerException, indexOutOfBounds.
    checked exceptions:
        Have to be handled with a catch block. 
        The compiler will give you an error if you do not handle it.
        i.e. IOException, FileNotFoundException.

Finally block:

    The finally block is always executed, regardless of whether an exception is thrown or not.
    It is used to close resources that were opened in the try block.

## January 29, 2025 notes:

### Nested Classes:

When using a nested class, if the class is non-static, it means the nested class has access to the
variables of the class it is nested into. On the other hand, if the nested class is static, 
it does not have access to the variables of the class it is nested into. 

A local inner class is a class inside a method. It can only be used in that method. If a class is meant
to be used only in one method, it is best to make it a local inner class or return it as an anonymous class.

### Java Collections:

Data structures library stored in java.util.

Java collections can only store objects, not primitive types.

List:

    -An ordered collection of elements.
    -Allows duplicates.
    -Elements can be accessed by their index.
    -Common implementations: ArrayList, LinkedList, Vector.
    
    Operations:
        -add()
        -remove()
        -get()
        -set()
        -size()
        -contains()
        -indexOf()
        -isEmpty()
        -clear()
        -toArray()

Set:

    -A collection of unique elements.
    -Does not allow duplicates.
    -Common implementations: HashSet, LinkedHashSet, TreeSet (For treeSet, gotta implement a comparable interface to compare objects inside).

    Operations:
        -add()
        -remove()
        -contains()
        -size()
        -isEmpty()
        -clear()

Queue:

    -A collection that orders elements in a FIFO (First In First Out) manner.
    -Common implementations: LinkedList, PriorityQueue.
    -Implementations: ArrayDeque, LinkedList, PriorityQueue (Same as treeSet: have to implement 'comparable' interface).

    Operations:
        -add()
        -remove()
        -element()
        -peek()
        -offer()
        -poll()

Deque:

    -A collection that orders elements in a FIFO (First In First Out) manner.
    -Common implementations: ArrayDeque, LinkedList.

    Operations:
        -addFirst()
        -addLast()
        -removeFirst()
        -removeLast()
        -getFirst()
        -getLast()
        -offerFirst()
        -offerLast()
        -pollFirst()
        -pollLast()

Map:

    -A collection of key-value pairs.
    -Keys are unique.
    -Common implementations: HashMap, LinkedHashMap, TreeMap.

    Operations:
        -put()
        -get()
        -remove()
        -containsKey()
        -containsValue()
        -size()
        -isEmpty()
        -clear()

### Iterable Interface:
    All collections implement the Iterable interface. This interface has only one method: iterator().

### Equality Checking:

Consider the following when checking for equality:

    -Use the equals() method to check if two objects are equal.
    -Override the equals() method in your class to define what it means for two objects to be equal.
    -Override the hashCode() method in your class to define how the object is hashed.

### Hash-Based Collections:

If you change an object after adding it to a hash-based collection, you will not be able to retrieve it
because the hash code will change. This is why it is important to override the hashCode() method.

If you want to change in a hash-based collection, you must remove it, change it, and then add it back.

### Sorted Collections:

Same thing applies to bst-based collections. If an object is changed, it will not be able to be retrieved.
Has to be removed, changed, and then added back.

## February 3rd, 2025 notes:

### Generic Types:

# Instantiating a generic type:

    -List<String> list = new ArrayList<String>();

# Inheritance with generic types:

    -If you have a class that uses a generic type, and you want to extend that class, you must specify the type.
    -Example: class MyList extends ArrayList<String> {}

# Generic Type Wildcards:

    -Use the '?' symbol to specify a wildcard type.
    -Example: List<?> list = new ArrayList<String>();

# Lambda Expressions:

Functions are seen as a piece of data, they can be stored in variables, passed as arguments, and returned from methods.
Lambda expressions are a way to define functions in a more concise way.
In java, lambda expressions can only be used with functional interfaces. A functional interface is an interface that has only one abstract method.
java.util.function package contains functional interfaces that can be used with lambda expressions.

Method Reference:

A method reference is a way to refer to a method without invoking it. It is used to pass a method as an argument to another method.
There are four types of method references:
    -Reference to a static method.
    -Reference to an instance method of an object of a particular type.
    -Reference to an instance method of an existing object.
    -Reference to a constructor.

Example:

    -Function<String, Integer> f = Integer::parseInt;

# IO Streams:

InputStream abstract class/interface has one method: read().
OutputStream abstract class/interface has one method: write().

The System class has tree static variables: in, out and err. These are InputStream, OutputStream and PrintStream objects respectively.

Reader and Writer abstract classes are used for character streams.
Instead of returning bytes, they return characters.
