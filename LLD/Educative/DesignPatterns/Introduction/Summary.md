# Design Patterns — Complete Reference Summary

> *A quick-reference guide to all 23 Gang of Four design patterns, organized by category with purpose, key insight, and Java examples.*

---

## Overview

Design patterns are reusable solutions to commonly occurring problems in software design. The Gang of Four (GoF) catalogued 23 patterns across three categories:

```
Design Patterns (23 total)
│
├── Creational (5)    → HOW objects are created
├── Structural (7)    → HOW classes and objects are composed
└── Behavioral (11)   → HOW objects communicate and delegate responsibility
```

---

## 🏗️ Creational Patterns

*Concerned with object creation — encapsulating the construction process to make systems independent of how objects are created, composed, and represented.*

---

### Builder Pattern

**Purpose:** Construct complex objects step by step, separating construction from representation.

**Key Insight:** When an object requires many parameters or a specific build sequence, telescoping constructors become unmanageable. The Builder encapsulates the construction process and produces different representations using the same steps.

**Use when:** Object construction is complex, multi-step, or needs to produce different variants.

**Java Example:** `java.lang.StringBuilder`

```java
StringBuilder sb = new StringBuilder()
    .append("Hello")
    .append(", ")
    .append("World!");
String result = sb.toString();
```

---

### Singleton Pattern

**Purpose:** Ensure a class has only one instance and provide a global access point to it.

**Key Insight:** Make the constructor private — only the class itself can instantiate. Return the single shared instance via a static method.

**Use when:** Exactly one shared resource is needed (cache, logger, config, thread pool).

**Java Example:** `java.lang.Runtime`

```java
Runtime runtime = Runtime.getRuntime();  // always returns the same instance
```

**Thread-safe implementation:**
```java
private volatile static Singleton instance;
public static Singleton getInstance() {
    if (instance == null) {
        synchronized (Singleton.class) {
            if (instance == null) instance = new Singleton();
        }
    }
    return instance;
}
```

---

### Prototype Pattern

**Purpose:** Create new objects by cloning an existing prototype instance.

**Key Insight:** When object construction is expensive or a class is only known at runtime, cloning a pre-built prototype is cheaper than constructing from scratch. Separate intrinsic (shared) state from extrinsic (unique) state.

**Use when:** Object creation is expensive, or you need many similar objects with slight variations.

**Java Example:** `java.lang.Object.clone()`

```java
public class F16 implements Cloneable {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();  // shallow copy
    }
}
```

---

### Factory Method Pattern

**Purpose:** Define an interface for object creation but let subclasses decide which class to instantiate.

**Key Insight:** Delegates instantiation to subclasses — the parent class controls *when* to create; subclasses control *what* to create. Eliminates tight coupling to concrete classes.

**Use when:** A class can't anticipate the type of objects it needs to create; subclasses should control instantiation.

**Java Example:** `java.util.Calendar.getInstance()`

```java
Calendar cal = Calendar.getInstance();  // returns right subclass for locale
```

---

### Abstract Factory Pattern

**Purpose:** Define an interface to create families of related objects without specifying their concrete classes.

**Key Insight:** A "factory of factories." Ensures that related objects are always created together and remain consistent. Swap the entire product family by swapping the factory.

**Use when:** A system needs to be independent of how its products are created; you need families of related objects.

**Java Example:** `javax.xml.parsers.DocumentBuilderFactory.newInstance()`

```java
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
// Returns the right parser for the platform/config — never exposes concrete class
```

---

## 🧱 Structural Patterns

*Concerned with how classes and objects are composed to form larger structures — using inheritance and composition to create flexible, efficient structures.*

---

### Adapter Pattern

**Purpose:** Allow incompatible interfaces to work together by converting one interface into another expected by the client.

**Key Insight:** Sits between two incompatible classes like a power adapter between different voltage standards. The client sees only the target interface; the adapter handles translation.

**Use when:** You want to use an existing class whose interface doesn't match what you need.

**Java Example:** `java.util.Arrays.asList()`

```java
String[] array = {"a", "b", "c"};
List<String> list = Arrays.asList(array);  // adapts array to List interface
```

---

### Bridge Pattern

**Purpose:** Decouple an abstraction from its implementation so both can vary independently.

**Key Insight:** Splits one class hierarchy into two parallel hierarchies connected by composition — avoiding the combinatorial explosion of `M × N` subclasses when M abstractions each need N implementations.

**Use when:** Both class and implementation need to vary independently; you want to avoid permanent binding between abstraction and implementation.

```
Without Bridge: M × N classes     With Bridge: M + N classes
```

---

### Composite Pattern

**Purpose:** Compose objects into tree structures to represent part-whole hierarchies, letting clients treat individual objects and compositions uniformly.

**Key Insight:** Both leaves and composites implement the same interface. Clients call the same method on a single leaf or an entire subtree — recursion handles the rest.

**Use when:** You need to represent part-whole hierarchies (file systems, UI trees, org charts, military alliances).

**Java Example:** `javax.faces.component.UIComponent`

```java
// Same getPersonnel() call works on one plane or an entire air force
airforce.getPersonnel();  // recursively sums all nested aircraft
```

---

### Decorator Pattern

**Purpose:** Attach additional responsibilities to an object dynamically — a flexible alternative to subclassing.

**Key Insight:** Wraps an object inside another that implements the same interface. Decorators can be stacked arbitrarily. Adding N features doesn't require 2ᴺ subclasses.

**Use when:** You need to add behavior to individual objects without affecting others; feature combinations make subclassing impractical.

**Java Example:** `java.io.BufferedInputStream`

```java
InputStream in = new DataInputStream(
                     new BufferedInputStream(
                         new FileInputStream("file.txt")));
// Each wrapper adds one capability — stacked, never subclassed
```

---

### Facade Pattern

**Purpose:** Provide a single, simplified interface to a complex subsystem.

**Key Insight:** Hides complexity behind a clean surface — like a light switch hiding the entire power grid. Changes to subsystems are quarantined inside the facade.

**Use when:** You want to simplify a complex subsystem; you want to decouple clients from subsystem internals.

**Java Example:** `javax.faces.context.FacesContext`

```java
// One call triggers complex multi-subsystem coordination
autopilot.autopilotOn();  // altitude, engine, navigation, fuel — all coordinated
```

---

### Flyweight Pattern

**Purpose:** Share state among a large number of fine-grained objects to reduce memory usage.

**Key Insight:** Separate intrinsic state (shared, stored once in flyweight) from extrinsic state (unique per instance, passed in by client). One flyweight serves thousands of logical instances.

**Use when:** The application creates vast numbers of similar objects that consume significant memory.

**Java Examples:** `java.lang.Integer.valueOf()`, `java.lang.Boolean.valueOf()`

```java
Integer a = Integer.valueOf(5);
Integer b = Integer.valueOf(5);
System.out.println(a == b);  // true — same flyweight object (cached -128 to 127)
```

---

### Proxy Pattern

**Purpose:** Provide a surrogate or placeholder for another object to control access to it.

**Key Insight:** Proxy and subject implement the same interface — client can't tell them apart. The proxy intercepts calls and adds access control, lazy loading, remote forwarding, or caching.

**Proxy Types:** Remote, Virtual, Protection, Caching, Firewall, Synchronization

**Use when:** You need to control, delay, or augment access to an object.

**Java Example:** `java.rmi.*`, `java.lang.reflect.Proxy`

```java
// RMI — remote method invocation over the network via a proxy stub
MyService service = (MyService) Naming.lookup("rmi://server/MyService");
service.doWork();  // client thinks it's calling locally; proxy forwards over network
```

---

## 🤝 Behavioral Patterns

*Concerned with how objects communicate, collaborate, and distribute responsibility — characterizing complex control flow and object interaction.*

---

### Chain of Responsibility Pattern

**Purpose:** Pass a request along a chain of handlers until one handles it.

**Key Insight:** Decouples sender from receiver. The chain can be reconfigured at runtime. A request either gets handled or falls off the end — no single handler is hardwired.

**Use when:** Multiple objects may handle a request and the handler isn't known in advance; the chain should be configurable.

**Java Example:** `java.util.logging.Logger.log()`

```java
// Log record passed through handlers — each decides to handle or pass up
logger.warning("Low fuel detected");
// ConsoleHandler → FileHandler → RemoteHandler (chain)
```

---

### Observer Pattern (Publisher / Subscriber)

**Purpose:** Define a one-to-many dependency so when one object changes state, all dependents are notified automatically.

**Key Insight:** Publisher and subscriber are decoupled — publisher knows only the Observer interface. Observers can subscribe and unsubscribe at runtime. **Push model** sends data; **Pull model** sends self-reference.

**Use when:** A change in one object requires updating others; the number of dependents is dynamic.

**Java Example:** `java.util.EventListener`

```java
button.addActionListener(e -> System.out.println("Clicked!"));
// addActionListener = subscribe; event fires = publish; all listeners notified
```

---

### Interpreter Pattern

**Purpose:** Define a grammar for a language and provide an interpreter that processes sentences in that language.

**Key Insight:** Each grammar rule maps to a class. Terminal symbols are leaves (base cases); non-terminals are composites (recursive). An Abstract Syntax Tree of these classes interprets any valid sentence.

**Use when:** You need to interpret sentences in a simple, well-defined language with few rules.

**Java Example:** `java.util.regex.Pattern`

```java
Pattern p = Pattern.compile("\\d{3}-\\d{4}");
Matcher m = p.matcher("867-5309");
System.out.println(m.matches());  // true — regex grammar interpreted against input
```

---

### Command Pattern

**Purpose:** Encapsulate a request as an object — enabling queuing, logging, and undo.

**Key Insight:** Decouples the invoker (button, menu item) from the receiver (the object that knows how to do the work). Commands can be stored, queued, replayed, and reversed.

**Capabilities:** Undo/Redo, Macro Commands, Command Queues, Crash Recovery Logging, Transactions

**Use when:** You need undo/redo; you want to queue or schedule operations; you're building UI controls.

**Java Example:** `java.lang.Runnable`

```java
Runnable command = () -> System.out.println("Executing...");
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(command);  // queued, executed later — invoker and receiver decoupled
```

---

### Iterator Pattern

**Purpose:** Traverse elements of a collection sequentially without exposing its underlying implementation.

**Key Insight:** Moves traversal logic out of the collection into a dedicated iterator object. Multiple iterators can traverse the same collection simultaneously, each with independent state.

**External vs. Internal:** External iterators give the client control (`hasNext()`/`next()`); internal iterators take an operation and apply it themselves (`forEach()`).

**Use when:** You want a uniform traversal interface across different collection types.

**Java Example:** `java.util.Iterator`

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) { System.out.println(it.next()); }
// for-each loop is syntactic sugar over this exact pattern
```

---

### Mediator Pattern

**Purpose:** Centralize complex communication among objects — so objects don't refer to each other directly.

**Key Insight:** Reduces M×(M-1)/2 peer-to-peer connections to M connections (all through the mediator). Changes to interaction rules are localized to the mediator.

**Use when:** Many objects communicate in complex ways creating hard-to-understand interdependencies; reusing objects is difficult due to many dependencies.

**Java Example:** `java.util.concurrent.ExecutorService`

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(task1);  // tasks don't know each other — executor mediates
executor.submit(task2);
```

---

### Memento Pattern

**Purpose:** Capture an object's internal state without exposing its structure — so it can be restored later.

**Key Insight:** The originator creates its own snapshot (memento). The caretaker holds it but can't read it. Only the originator can interpret and restore from its own memento — encapsulation preserved.

**Use when:** You need undo, rollback, save points, or snapshot-restore functionality.

**Java Example:** `java.io.Serializable`

```java
// Serialize = create memento; Deserialize = restore from memento
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("state.dat"));
out.writeObject(myObject);  // snapshot

ObjectInputStream in = new ObjectInputStream(new FileInputStream("state.dat"));
MyObject restored = (MyObject) in.readObject();  // restore
```

---

### State Pattern

**Purpose:** Allow an object to alter its behavior when its internal state changes — appearing to change its class.

**Key Insight:** Replaces state-checking conditionals with dedicated state classes. Each state knows its valid transitions and behaviors. Adding a new state = adding a new class, not modifying existing ones.

**Use when:** Object behavior depends on its state and must change at runtime; conditional state logic is growing unwieldy.

**Java Example:** `javax.faces.lifecycle.LifeCycle`

```
Aircraft State Machine:
Parked → Taxi → Airborne → Land → Taxi → Parked
           ↓                ↓
         Crashed          Crashed  (terminal — no exit)
```

---

### Template Method Pattern

**Purpose:** Define the skeleton of an algorithm in an abstract class, letting subclasses fill in specific steps.

**Key Insight:** The template method is `final` — subclasses can't reorder the skeleton. Abstract steps *must* be overridden; hooks *may* be overridden. Factors out common code once; each subclass customizes only what differs.

**Hollywood Principle:** *"Don't call us, we'll call you."* — the abstract class calls subclass methods, not vice versa.

**Use when:** Multiple classes share the same algorithm structure but differ in specific steps.

**Java Example:** `java.io.InputStream.read(byte[], int, int)`

```java
// read(byte[],int,int) is the template method — calls abstract read() internally
// FileInputStream, ByteArrayInputStream each override read()
// The template calls them at the right time
```

---

### Strategy Pattern

**Purpose:** Encapsulate a family of algorithms and make them interchangeable — the client can switch algorithms seamlessly.

**Key Insight:** Uses composition — context holds a strategy reference. Client can swap the entire algorithm at runtime without changing context or client code. Eliminates algorithm-selection conditionals.

**Use when:** Multiple related classes differ only in behavior; you need runtime algorithm selection.

**Java Example:** `java.util.Comparator`

```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
names.sort((a, b) -> a.length() - b.length());  // strategy: sort by length
names.sort(String::compareTo);                   // strategy: sort alphabetically
// Same sort call — algorithm swapped seamlessly
```

---

### Visitor Pattern

**Purpose:** Define new operations on elements of an object structure without changing the classes of those elements.

**Key Insight:** The element accepts a visitor via `accept(visitor)`, which calls `visitor.visitConcreteElement(this)` — simulating double dispatch. New operations = new visitor class. Zero changes to element classes.

**Trade-off:** Easy to add operations (new visitor); hard to add new element types (all visitors must be updated).

**Use when:** Object structure is stable but new operations are frequently needed.

**Java Example:** `java.nio.file.FileVisitor`

```java
Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println("Visiting: " + file);
        return FileVisitResult.CONTINUE;
    }
});
```

---

## Pattern Comparison Quick Reference

### By Problem Type

| Problem | Pattern to Reach For |
|---|---|
| Complex object construction | **Builder** |
| Exactly one instance | **Singleton** |
| Cheap object copies | **Prototype** |
| Subclasses decide what to create | **Factory Method** |
| Families of related objects | **Abstract Factory** |
| Incompatible interfaces | **Adapter** |
| Two independent varying dimensions | **Bridge** |
| Part-whole tree structures | **Composite** |
| Add features without subclassing | **Decorator** |
| Simplify a complex subsystem | **Facade** |
| Many objects, shared state | **Flyweight** |
| Control access to an object | **Proxy** |
| Route request through handlers | **Chain of Responsibility** |
| Broadcast state changes | **Observer** |
| Interpret a language/grammar | **Interpreter** |
| Encapsulate a request as object | **Command** |
| Traverse a collection | **Iterator** |
| Centralize object communication | **Mediator** |
| Save and restore object state | **Memento** |
| Behavior changes with state | **State** |
| Fix algorithm structure, vary steps | **Template Method** |
| Swap entire algorithm at runtime | **Strategy** |
| New operations on stable structure | **Visitor** |

---

### Commonly Confused Pairs

| Pair | Key Distinction |
|---|---|
| **Builder vs. Abstract Factory** | Builder constructs one object step-by-step; Abstract Factory creates a family of related objects in one go |
| **Factory Method vs. Abstract Factory** | Factory Method uses inheritance (subclass decides); Abstract Factory uses composition (factory object injected) |
| **Adapter vs. Facade** | Adapter makes an existing interface compatible; Facade creates a new simpler interface |
| **Adapter vs. Proxy** | Adapter changes the interface; Proxy preserves it (same interface, different behavior) |
| **Decorator vs. Proxy** | Decorator adds behavior (always forwards); Proxy controls access (may restrict or delay) |
| **Composite vs. Decorator** | Composite builds trees for part-whole; Decorator wraps a single object for behavior extension |
| **Strategy vs. Template Method** | Strategy uses composition, swaps entire algorithm; Template Method uses inheritance, fixes structure, varies steps |
| **Strategy vs. State** | Strategy: client consciously swaps algorithm; State: transitions happen automatically based on actions |
| **Observer vs. Chain of Responsibility** | Observer notifies ALL subscribers; Chain passes to ONE handler that accepts it |
| **Mediator vs. Facade** | Facade simplifies one-way access to a subsystem; Mediator coordinates two-way communication between colleagues |
| **Memento vs. Prototype** | Prototype clones the whole object; Memento saves just the state for later restoration |
| **Flyweight vs. Singleton** | Singleton: one instance ever; Flyweight: one per type, multiple types possible, immutable |

---

### Pattern Relationships

```
Factory Method ──── is a specialization of ────► Template Method
Abstract Factory ── uses ──────────────────────► Factory Method
Composite ────────── often used with ──────────► Iterator + Visitor
Decorator ─────────── similar structure to ────► Composite (but different intent)
Command ───────────── uses ─────────────────────► Memento (for undo state)
Observer ──────────── used by ──────────────────► Mediator (for notification)
Flyweight ─────────── combined with ────────────► Composite (shared leaf nodes)
State objects ──────── often implemented as ────► Flyweight
Strategy objects ───── often implemented as ────► Flyweight
```

---

## Java Standard Library Pattern Map

| Java API | Pattern |
|---|---|
| `java.lang.StringBuilder` | Builder |
| `java.lang.Runtime` | Singleton |
| `java.lang.Object.clone()` | Prototype |
| `java.util.Calendar.getInstance()` | Factory Method |
| `javax.xml.parsers.DocumentBuilderFactory.newInstance()` | Abstract Factory |
| `java.util.Arrays.asList()` | Adapter |
| `java.io.BufferedInputStream(InputStream)` | Decorator |
| `javax.faces.context.FacesContext` | Facade |
| `java.lang.Integer.valueOf()` | Flyweight |
| `java.rmi.*`, `java.lang.reflect.Proxy` | Proxy |
| `javax.servlet.Filter.doFilter()` | Chain of Responsibility |
| `java.util.EventListener` | Observer |
| `java.util.regex.Pattern` | Interpreter |
| `java.lang.Runnable`, `javax.swing.Action` | Command |
| `java.util.Iterator` | Iterator |
| `java.util.concurrent.ExecutorService` | Mediator |
| `java.io.Serializable` | Memento |
| `javax.faces.lifecycle.LifeCycle` | State |
| `java.io.InputStream.read(byte[],int,int)` | Template Method |
| `java.util.Comparator` | Strategy |
| `java.nio.file.FileVisitor` | Visitor |