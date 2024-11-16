
# Context

Context is a Java library for creating advanced user interfaces inside a console window

This is a very early proof of concept, nothing to see here (yet)

## Building

To build the project, run the following command:

```bash
mvn clean package
```

## Running

To see a simple demo of panels being drawn randomly on the screen, run the following command:

```bash
java -cp target/context-1.0-SNAPSHOT.jar <example_class>
```

Where `<example_class>` is one of the following:

- examples.Boxes
- examples.FullPanel
- examples.InlinePanel
- examples.SimpleDom
