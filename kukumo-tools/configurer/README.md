# Configurer

This library is a convenient wrapper around
[Apache Commons Configuration](https://commons.apache.org/proper/commons-configuration)
with some extra tweaks/features:



- The `Configuration` class is immutable
- Configurations can be attach directly to Java classes using the `@Configurator`
 and `@Property` annotations
- Single-value getter methods without a default value return an `Optional` instance
- Multiple-value getter methods return an empty list when the property is not defined
- Configuration builders can detect automatically the type of content of a file
(regarding its extension)

---

[Requirements] | [Usage] | [Contributing] | [Authors] | [Acknowledgment] | [License]

---

## Requirements

- Java 8 or later.

## Usage

### Importing dependency

#### Maven

```xml
<dependency>
   <groupId>iti.commons</groupId>
   <artifactId>configurer</artifactId>
   <version>1.0.0</version>
</dependency>
```

### Configuring via annotations

TODO

### Loading configurations

In order to obtain a `Configuration` object, first an instance of `ConfigurationBuilder`
must be created. Then, use any of the methods available to load the configuration from
the desired source:

```java
   ConfigurationBuilder builder = new ConfigurationBuilder();
   Configuration conf = builder.buildFromURI("file://home/user/myConfig.yaml");
```

Two or more configurations can be composed:

```java
   ConfigurationBuilder builder = new ConfigurationBuilder();
   Configuration confFromEnv = builder.buildFromEnvironment();
   Configuration confFromFile = builder.buildFromURI("file://home/user/myConfig.yaml");
   Configuration confComposed = builder.compose(confFromEnv,confFromFile);
```
or, more fluently:

```java
  Configuration conf = new ConfigurationBuilder()
     .buildFromEnvironment()
     .appendFromURI("file://home/user/myConfig.yaml");
```

## Contributing

## Authors

- Luis Iñesta Gelabert  | :email: <linesta@iti.es> | :email: <luiinge@gmail.com>


## Acknowledgment

## License


