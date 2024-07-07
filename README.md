# NumbleJSON

NumbleJSON is a lightweight Java library for easy and flexible JSON serialization and deserialization. It provides annotations and utility classes to map Java objects to JSON and vice versa, supporting various customization options.

## Features

- **Annotation Support**: Customize JSON serialization and deserialization using annotations like `@JsonProperty`, `@JsonIgnore`, `@JsonSerialize`, `@JsonDeserialize`, `@JsonDateFormat`, and `@JsonAlias`.
- **Flexible Serialization**: Serialize Java objects to JSON strings with support for nested objects, lists, maps, and custom serializers.
- **Robust Deserialization**: Deserialize JSON strings back into Java objects, handling various data types and optional fields.
- **Date Formatting**: Support for custom date formats using `@JsonDateFormat`.
- **Field Inclusion Control**: Control field inclusion based on conditions like non-null values, non-empty collections, and custom criteria using `@JsonInclude`.
- **Light Weight**: Come on, everything is lightweight nowadays..

## Usage

### Serialization Example

```java
import dev.quantam.numblejson.annotations.*;

public class User {
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("age")
    private int age;

    // Constructor, getters, and setters
}

// Serialize an object to JSON
User user = new User("JohnDoe", 30);
String json = JsonSerializer.serialize(user);
System.out.println(json);
```

### Deserialization Example

```java
import dev.quantam.numblejson.annotations.*;

// JSON: {"user_name":"JohnDoe","age":30}
String json = "{\"user_name\":\"JohnDoe\",\"age\":30}";
User user = JsonDeserializer.deserialize(json, User.class);
System.out.println(user.getUserName()); // Output: JohnDoe
System.out.println(user.getAge()); // Output: 30
```
Feel Free to checkout SerializationTest for more detailed help!

## Annotations

- `@JsonProperty`: Specifies the JSON property name for a field.
- `@JsonIgnore`: Excludes a field from JSON serialization and deserialization.
- `@JsonSerialize`: Customizes field serialization using a specified serializer class.
- `@JsonDeserialize`: Customizes field deserialization using a specified deserializer class.
- `@JsonDateFormat`: Formats date fields during serialization and parses them during deserialization.
- `@JsonAlias`: Specifies alternate names for a JSON property.
- `@JsonInclude`: Specifies when a field should be included in serialization.

## License

This library is licensed under [CC by 4.0](https://creativecommons.org/licenses/by/4.0/deed.en) to quantam!

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## Issues

If you encounter any issues or have suggestions, please report them in the Issues section.



###### ill make a better readme sometime later.